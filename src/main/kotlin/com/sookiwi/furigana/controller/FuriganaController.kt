package com.sookiwi.furigana.controller

import com.google.common.io.ByteStreams
import com.jayway.jsonpath.JsonPath
import com.linecorp.bot.client.LineMessagingClient
import com.linecorp.bot.client.MessageContentResponse
import com.linecorp.bot.model.ReplyMessage
import com.linecorp.bot.model.action.DatetimePickerAction
import com.linecorp.bot.model.action.MessageAction
import com.linecorp.bot.model.action.PostbackAction
import com.linecorp.bot.model.action.URIAction
import com.linecorp.bot.model.event.*
import com.linecorp.bot.model.event.message.*
import com.linecorp.bot.model.event.source.GroupSource
import com.linecorp.bot.model.event.source.RoomSource
import com.linecorp.bot.model.message.*
import com.linecorp.bot.model.message.imagemap.ImagemapArea
import com.linecorp.bot.model.message.imagemap.ImagemapBaseSize
import com.linecorp.bot.model.message.imagemap.MessageImagemapAction
import com.linecorp.bot.model.message.imagemap.URIImagemapAction
import com.linecorp.bot.model.message.template.*
import com.linecorp.bot.spring.boot.annotation.EventMapping
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler
import com.sookiwi.furigana.LineBotProperties
import com.sookiwi.furigana.downloadedContentDir
import com.sookiwi.furigana.dto.ResultSet
import com.sookiwi.furigana.exception.YahooFuriganaException
import com.sookiwi.furigana.supplier.flexMessageSupplier
import com.sookiwi.furigana.supplier.messageWithQuickReplySupplier
import com.sookiwi.furigana.textMessageModel.*
import mu.KotlinLogging
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.WebClientResponseException
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import java.io.IOException
import java.io.UncheckedIOException
import java.net.URI
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Path
import java.time.LocalDateTime
import java.util.*
import java.util.concurrent.ExecutionException

private val logger = KotlinLogging.logger {}

@Suppress("unused", "UnstableApiUsage")
@LineMessageHandler
class FuriganaController(
    private val lineMessagingClient: LineMessagingClient,
    private val lineBotProperties: LineBotProperties,
    private val textMessageEventConverter: TextMessageEventConverter,
    webClientBuilder: WebClient.Builder
) {
    private val webClient = webClientBuilder.build()

    @EventMapping
    fun handleAudioMessageEvent(event: MessageEvent<AudioMessageContent>) {
        logger.info { "event=$event" }
        replyText(event.replyToken, "MessageEvent<AudioMessageContent>; $event")
    }

    @EventMapping
    fun handleFileMessageEvent(event: MessageEvent<FileMessageContent>) {
        logger.info { "event=$event" }
        replyText(event.replyToken, "MessageEvent<FileMessageContent>; $event")
    }

    @EventMapping
    fun handleImageMessageEvent(event: MessageEvent<ImageMessageContent>) {
        logger.info { "event=$event" }
        handleHeavyContent(
            event.replyToken,
            event.message.id,
            messageConsumer = { responseBody ->
                val jpg = saveContent(ext = "jpg", responseBody = responseBody)
                reply(event.replyToken, ImageMessage(jpg.uri, jpg.uri))
            }
        )
    }

    @EventMapping
    fun handleLocationMessageEvent(event: MessageEvent<LocationMessageContent>) {
        logger.info { "event=$event" }
        checkNotNullLocationMessageEvent(event)

        val locationMessageContent = event.message
        val replyLocationMessage = LocationMessage.builder()
            .title(locationMessageContent.title ?: "No title")
            .address(locationMessageContent.address)
            .latitude(locationMessageContent.latitude)
            .longitude(locationMessageContent.longitude)
            .build()
        reply(event.replyToken, replyLocationMessage)
    }

    @EventMapping
    fun handleStickerMessageEvent(event: MessageEvent<StickerMessageContent>) {
        logger.info { "event=$event" }
        checkNotNullStickerMessageEvent(event)

        reply(event.replyToken, StickerMessage(event.message.packageId, event.message.stickerId))
    }

    @EventMapping
    fun handleTextMessageEvent(event: MessageEvent<TextMessageContent>) {
        logger.info { "event=$event" }
        checkNotNullTextMessageEvent(event)

        val commandEvent = textMessageEventConverter.convert(event)
        logger.info { commandEvent }

        val replyToken = commandEvent.replyToken
        when (val command = commandEvent.message.command) {
            is Buttons -> {
                val imageUrl =
                    createStaticFileUri("/static/buttons/1040.jpg")
                val buttonsTemplate = ButtonsTemplate(
                    imageUrl,
                    "My button sample",
                    "Hello, my button",
                    listOf(
                        URIAction("Go to line.me", URI("https://line.me"), null),
                        PostbackAction("Say hello1", "hello こんにちは"),
                        PostbackAction("言 hello2", "hello こんにちは", "hello こんにちは"),
                        MessageAction("Say message", "Rice=米")
                    )
                )
                val templateMessage = TemplateMessage("Button alt text", buttonsTemplate)
                reply(replyToken, templateMessage)
            }
            is Bye -> {
                when (val source = command.source) {
                    is GroupSource -> {
                        replyText(replyToken, "Leaving group")
                        lineMessagingClient.leaveGroup(source.groupId).get()
                    }
                    is RoomSource -> {
                        replyText(replyToken, "Leaving room")
                        lineMessagingClient.leaveRoom(source.roomId).get()
                    }
                    else -> replyText(replyToken, "Bot can't leave from 1:1 chat")
                }
            }
            is Carousel -> {
                val imageUrl =
                    createStaticFileUri("/static/buttons/1040.jpg")
                val carouselTemplate = CarouselTemplate(
                    listOf(
                        CarouselColumn(
                            imageUrl, "hoge", "fuga", listOf(
                                URIAction(
                                    "Go to line.me",
                                    URI("https://line.me"),
                                    null
                                ),
                                URIAction(
                                    "Go to line.me",
                                    URI("https://line.me"),
                                    null
                                ),
                                PostbackAction(
                                    "Say hello1",
                                    "hello こんにちは"
                                )
                            )
                        ),
                        CarouselColumn(
                            imageUrl, "hoge", "fuga", listOf(
                                PostbackAction(
                                    "言 hello2",
                                    "hello こんにちは",
                                    "hello こんにちは"
                                ),
                                PostbackAction(
                                    "言 hello2",
                                    "hello こんにちは",
                                    "hello こんにちは"
                                ),
                                MessageAction(
                                    "Say message",
                                    "Rice=米"
                                )
                            )
                        ),
                        CarouselColumn(
                            imageUrl, "Datetime Picker",
                            "Please select a date, time or datetime", listOf(
                                DatetimePickerAction(
                                    "Datetime",
                                    "action=sel",
                                    "datetime",
                                    "2017-06-18T06:15",
                                    "2100-12-31T23:59",
                                    "1900-01-01T00:00"
                                ),
                                DatetimePickerAction(
                                    "Date",
                                    "action=sel&only=date",
                                    "date",
                                    "2017-06-18",
                                    "2100-12-31",
                                    "1900-01-01"
                                ),
                                DatetimePickerAction(
                                    "Time",
                                    "action=sel&only=time",
                                    "time",
                                    "06:15",
                                    "23:59",
                                    "00:00"
                                )
                            )
                        )
                    )
                )
                val templateMessage = TemplateMessage("Carousel alt text", carouselTemplate)
                this.reply(replyToken, templateMessage)
            }
            is Confirm -> {
                val confirmTemplate = ConfirmTemplate(
                    "Do it?",
                    MessageAction("Yes", "Yes!"),
                    MessageAction("No", "No!")
                )
                val templateMessage = TemplateMessage("Confirm alt text", confirmTemplate)
                reply(replyToken, templateMessage)
            }
            is Flex -> {
                reply(replyToken, flexMessageSupplier())
            }
            is ImageCarousel -> {
                val imageUrl =
                    createStaticFileUri("/static/buttons/1040.jpg")

                val imageCarouselTemplate = ImageCarouselTemplate(
                    listOf(
                        ImageCarouselColumn(
                            imageUrl,
                            URIAction("Goto line.me", URI("https://line.me"), null)
                        ),
                        ImageCarouselColumn(
                            imageUrl,
                            MessageAction("Say message", "Rice=米")
                        ),
                        ImageCarouselColumn(
                            imageUrl,
                            PostbackAction("言 hello2", "hello こんにちは", "hello こんにちは")
                        )
                    )
                )

                val templateMessage = TemplateMessage("ImageCarousel alt text", imageCarouselTemplate)

                reply(replyToken, templateMessage)
            }
            is ImageMap -> {
                reply(
                    replyToken, ImagemapMessage(
                        createStaticFileUri("/static/rich"),
                        "This is alt text",
                        ImagemapBaseSize(1040, 1040),
                        listOf(
                            URIImagemapAction(
                                "https://store.line.me/family/manga/en",
                                ImagemapArea(
                                    0, 0, 520, 520
                                )
                            ),
                            URIImagemapAction(
                                "https://store.line.me/family/music/en",
                                ImagemapArea(
                                    520, 0, 520, 520
                                )
                            ),
                            URIImagemapAction(
                                "https://store.line.me/family/play/en",
                                ImagemapArea(
                                    0, 520, 520, 520
                                )
                            ),
                            MessageImagemapAction(
                                "URANAI!",
                                ImagemapArea(
                                    520, 520, 520, 520
                                )
                            )
                        )
                    )
                )
            }
            is Profile -> {
                val userId = command.userId
                if (userId != null) {
                    lineMessagingClient
                        .getProfile(userId)
                        .whenComplete { profile, throwable ->
                            if (throwable != null) {
                                replyText(
                                    replyToken,
                                    throwable.message ?: "Fail to get profile without error message."
                                )
                                return@whenComplete
                            }

                            reply(
                                replyToken,
                                Arrays.asList<Message>(
                                    TextMessage(
                                        "Display name: " + profile.displayName
                                    ),
                                    TextMessage("Status message: " + profile.statusMessage)
                                )
                            )
                        }
                } else {
                    replyText(replyToken, "Bot can't use profile API without user ID")
                }
            }
            is QuickReply -> {
                reply(replyToken, messageWithQuickReplySupplier())
            }
            is Furi -> {
                webClient.get()
                    .uri {
                        it.scheme("https")
                            .host("jlp.yahooapis.jp")
                            .path("/FuriganaService/V1/furigana")
                            .queryParam("appid", lineBotProperties.yahoojpAppid)
                            .queryParam(
                                "sentence",
                                URLEncoder.encode(command.message, "UTF-8")
                            )
                            .build()
                    }
                    .accept(MediaType.TEXT_XML)
                    .acceptCharset(StandardCharsets.UTF_8)
                    .retrieve()
                    .bodyToMono(ResultSet::class.java)
                    .subscribe({ resultSet ->
                        val stringJoiner = StringJoiner(" ")
                        resultSet.result.forEach { result ->
                            result.wordList.forEach { wordList ->
                                wordList.word.forEach { word ->
                                    when {
                                        word.furigana == null -> stringJoiner.add(word.surface)
                                        word.furigana == word.surface -> stringJoiner.add(word.surface)
                                        else -> stringJoiner.add("${word.surface}(${word.furigana})")
                                    }
                                }
                            }
                        }

                        val resultString = stringJoiner.toString()

                        logger.info { resultString }
                        replyText(replyToken, resultString)
                    }, { exception ->
                        when (exception) {
                            is WebClientResponseException.ServiceUnavailable -> replyText(
                                replyToken,
                                command.message
                            )
                            else -> throw YahooFuriganaException("Yahoo API returned an unexpected status code.")
                        }
                    })
            }
            is Gif -> {

                webClient.get()
                    .uri {
                        it.scheme("https")
                            .host("api.giphy.com")
                            .path("/v1/gifs/translate")
                            .queryParam("api_key", lineBotProperties.giphyAppKey)
                            .queryParam("s", URLEncoder.encode(command.message, "UTF-8"))
                            .build()
                    }
                    .retrieve()
                    .bodyToMono(String::class.java)
                    .subscribe { jsonString ->
                        val fixedWidthUrl = JsonPath.read<String>(jsonString, "$.data.images.fixed_width.url")
                        val fixedWidthMp4 = JsonPath.read<String>(jsonString, "$.data.images.fixed_width.mp4")

                        reply(
                            event.replyToken,
                            VideoMessage(
                                URI(fixedWidthMp4),
                                URI(fixedWidthUrl)
                            )
                        )
                    }
            }
        }
    }

    @EventMapping
    fun handleUnknownMessageEvent(event: MessageEvent<UnknownMessageContent>) {
        logger.info { "event=$event" }
        replyText(event.replyToken, "MessageEvent<UnknownMessageContent>; $event")
    }

    @EventMapping
    fun handleVideoMessageEvent(event: MessageEvent<VideoMessageContent>) {
        logger.info { "event=$event" }
        replyText(event.replyToken, "MessageEvent<VideoMessageContent>; $event")
    }

    // Indicates that the user has linked their LINE account with a provider's (your) service account.
    // You can reply to this events. For more information, see Linking user accounts.
    @EventMapping
    fun handleAccountLinkEvent(event: AccountLinkEvent) {
        logger.info { "event=$event" }
        replyText(event.replyToken, "AccountLinkEvent; $event")
    }

    // Indicates that the user performed a postback action. You can reply to this events.
    @EventMapping
    fun handleBeaconEvent(event: BeaconEvent) {
        logger.info { "event=$event" }
        replyText(event.replyToken, "BeaconEvent; $event")
    }

    // Indicates that your account was added as a friend (or unblocked). You can reply to this events.
    @EventMapping
    fun handleFollowEvent(event: FollowEvent) {
        logger.info { "event=$event" }
        replyText(event.replyToken, "FollowEvent; $event")
    }

    // Indicates that your bot joined a group chat.
    @EventMapping
    fun handleJoinEvent(event: JoinEvent) {
        logger.info { "event=$event" }
        replyText(event.replyToken, "JoinEvent; $event")
    }

    // Indicates that a user deleted your bot from a group or that your bot left a group or room.
    @EventMapping
    fun handleLeaveEvent(event: LeaveEvent) {
        logger.info { "event=$event" }
    }

    @EventMapping
    fun handleMemberJoinedEvent(event: MemberJoinedEvent) {
        logger.info { "event=$event" }
        replyText(event.replyToken, "MemberJoinedEvent; $event")
    }

    @EventMapping
    fun handleMemberLeftEvent(event: MemberLeftEvent) {
        logger.info { "event=$event" }
    }

    // Indicates that the user performed a postback action. You can reply to this events.
    @EventMapping
    fun handlePostbackEvent(event: PostbackEvent) {
        logger.info { "event=$event" }
        replyText(event.replyToken, "PostbackEvent; $event")
    }

    @EventMapping
    fun handleThingsEvent(event: ThingsEvent) {
        logger.info { "event=$event" }
        replyText(event.replyToken, "ThingsEvent; $event")
    }

    // Indicates that your account was blocked.
    @EventMapping
    fun handleUnfollowEvent(event: UnfollowEvent) {
        logger.info { "event=$event" }
    }

    @EventMapping
    fun handleUnknownEvent(event: UnknownEvent) {
        logger.info { "event=$event" }
    }

    @EventMapping
    fun handleOtherEvent(event: Event) {
        logger.info { "event=$event" }
    }

    private fun handleHeavyContent(
        replyToken: String,
        messageId: String,
        messageConsumer: (MessageContentResponse) -> Unit
    ) {
        val response: MessageContentResponse
        try {
            response = lineMessagingClient.getMessageContent(messageId).get()
        } catch (e: InterruptedException) {
            reply(replyToken, TextMessage("Cannot get image: ${e.message}"))
            throw RuntimeException(e)
        } catch (e: ExecutionException) {
            reply(replyToken, TextMessage("Cannot get image: ${e.message}"))
            throw RuntimeException(e)
        }

        messageConsumer(response)
    }

    private fun reply(replyToken: String, message: Message) {
        reply(replyToken, listOf(message))
    }

    private fun reply(replyToken: String, messages: List<Message>) {
        try {
            val apiResponse = lineMessagingClient
                .replyMessage(ReplyMessage(replyToken, messages))
                .get()
            logger.info("Sent messages: {}", apiResponse)
        } catch (e: InterruptedException) {
            throw RuntimeException(e)
        } catch (e: ExecutionException) {
            throw RuntimeException(e)
        }
    }

    private fun replyText(replyToken: String, message: String) {
        var messageToReply = message
        if (message.length > 1000) {
            messageToReply = message.substring(0, 1000 - 2) + "……"
        }

        reply(replyToken, TextMessage(messageToReply))
    }
}

private fun createStaticFileUri(path: String): URI {
    return ServletUriComponentsBuilder.fromCurrentContextPath()
        .path(path).build().toUri()
}

// later, platform types have to be wrap out recursively in one command / annotation / ...
// may name it, type fixer.
private fun checkNotNullEvent(event: Event) {
    checkNotNull(event.source)
    checkNotNull(event.timestamp)
}

private fun checkNotNullMessageEvent(event: MessageEvent<*>) {
    checkNotNullEvent(event)
    checkNotNull(event.replyToken)
    checkNotNull(event.message)
}

private fun checkNotNullLocationMessageEvent(event: MessageEvent<LocationMessageContent>) {
    checkNotNullMessageEvent(event)
    checkNotNull(event.message.id)
    checkNotNull(event.message.address)
}

private fun checkNotNullStickerMessageEvent(event: MessageEvent<StickerMessageContent>) {
    checkNotNullMessageEvent(event)
    checkNotNull(event.message.id)
    checkNotNull(event.message.packageId)
    checkNotNull(event.message.stickerId)
}

private fun checkNotNullTextMessageEvent(event: MessageEvent<TextMessageContent>) {
    checkNotNullMessageEvent(event)
    checkNotNull(event.message.id)
    checkNotNull(event.message.text)
}

private fun createTempFile(ext: String): DownloadedContent {
    val fileName =
        LocalDateTime.now().toString() + '-'.toString() + UUID.randomUUID().toString() + '.'.toString() + ext
    val tempFile = downloadedContentDir.resolve(fileName)
    tempFile.toFile().deleteOnExit()
    return DownloadedContent(tempFile, createUri("/downloaded/" + tempFile.fileName))
}

private fun createUri(path: String): URI {
    return ServletUriComponentsBuilder.fromCurrentContextPath().path(path).build().toUri()
}

private fun saveContent(ext: String, responseBody: MessageContentResponse): DownloadedContent {
    logger.info { "Got content-type: $responseBody" }

    val tempFile = createTempFile(ext)
    try {
        Files.newOutputStream(tempFile.path).use { outputStream ->
            ByteStreams.copy(responseBody.stream, outputStream)
            logger.info("Saved $ext: $tempFile")
            return tempFile
        }
    } catch (e: IOException) {
        throw UncheckedIOException(e)
    }
}

data class DownloadedContent(
    val path: Path,
    val uri: URI
)
