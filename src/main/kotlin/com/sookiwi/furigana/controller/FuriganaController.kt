package com.sookiwi.furigana.controller

import com.jayway.jsonpath.JsonPath
import com.linecorp.bot.client.LineMessagingClient
import com.linecorp.bot.model.ReplyMessage
import com.linecorp.bot.model.action.DatetimePickerAction
import com.linecorp.bot.model.action.MessageAction
import com.linecorp.bot.model.action.PostbackAction
import com.linecorp.bot.model.action.URIAction
import com.linecorp.bot.model.event.AccountLinkEvent
import com.linecorp.bot.model.event.BeaconEvent
import com.linecorp.bot.model.event.Event
import com.linecorp.bot.model.event.FollowEvent
import com.linecorp.bot.model.event.JoinEvent
import com.linecorp.bot.model.event.LeaveEvent
import com.linecorp.bot.model.event.MessageEvent
import com.linecorp.bot.model.event.PostbackEvent
import com.linecorp.bot.model.event.UnfollowEvent
import com.linecorp.bot.model.event.UnknownEvent
import com.linecorp.bot.model.event.message.AudioMessageContent
import com.linecorp.bot.model.event.message.FileMessageContent
import com.linecorp.bot.model.event.message.ImageMessageContent
import com.linecorp.bot.model.event.message.LocationMessageContent
import com.linecorp.bot.model.event.message.StickerMessageContent
import com.linecorp.bot.model.event.message.TextMessageContent
import com.linecorp.bot.model.event.message.UnknownMessageContent
import com.linecorp.bot.model.event.message.VideoMessageContent
import com.linecorp.bot.model.event.source.GroupSource
import com.linecorp.bot.model.event.source.RoomSource
import com.linecorp.bot.model.message.ImagemapMessage
import com.linecorp.bot.model.message.LocationMessage
import com.linecorp.bot.model.message.Message
import com.linecorp.bot.model.message.StickerMessage
import com.linecorp.bot.model.message.TemplateMessage
import com.linecorp.bot.model.message.TextMessage
import com.linecorp.bot.model.message.VideoMessage
import com.linecorp.bot.model.message.imagemap.ImagemapArea
import com.linecorp.bot.model.message.imagemap.ImagemapBaseSize
import com.linecorp.bot.model.message.imagemap.MessageImagemapAction
import com.linecorp.bot.model.message.imagemap.URIImagemapAction
import com.linecorp.bot.model.message.template.ButtonsTemplate
import com.linecorp.bot.model.message.template.CarouselColumn
import com.linecorp.bot.model.message.template.CarouselTemplate
import com.linecorp.bot.model.message.template.ConfirmTemplate
import com.linecorp.bot.model.message.template.ImageCarouselColumn
import com.linecorp.bot.model.message.template.ImageCarouselTemplate
import com.linecorp.bot.spring.boot.annotation.EventMapping
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler
import com.sookiwi.furigana.LineBotProperties
import com.sookiwi.furigana.dto.ResultSet
import com.sookiwi.furigana.exception.YahooFuriganaException
import com.sookiwi.furigana.supplier.FlexMessageSupplier
import com.sookiwi.furigana.supplier.MessageWithQuickReplySupplier
import com.sookiwi.furigana.textMessageModel.Buttons
import com.sookiwi.furigana.textMessageModel.Bye
import com.sookiwi.furigana.textMessageModel.Carousel
import com.sookiwi.furigana.textMessageModel.Confirm
import com.sookiwi.furigana.textMessageModel.Flex
import com.sookiwi.furigana.textMessageModel.Furi
import com.sookiwi.furigana.textMessageModel.Gif
import com.sookiwi.furigana.textMessageModel.ImageCarousel
import com.sookiwi.furigana.textMessageModel.ImageMap
import com.sookiwi.furigana.textMessageModel.Profile
import com.sookiwi.furigana.textMessageModel.QuickReply
import com.sookiwi.furigana.textMessageModel.TextMessageEventConverter
import mu.KotlinLogging
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.WebClientResponseException
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.util.Arrays
import java.util.StringJoiner
import java.util.concurrent.ExecutionException

@LineMessageHandler
class FuriganaController(
    private val lineMessagingClient: LineMessagingClient,
    private val lineBotProperties: LineBotProperties
) {
    private val log = KotlinLogging.logger {}
    private val textMessageEventConverter: TextMessageEventConverter = TextMessageEventConverter()

    private val webClient = WebClient.builder()
        .build()

    @EventMapping
    fun handleAudioMessageEvent(event: MessageEvent<AudioMessageContent>) {
        TODO()
    }

    @EventMapping
    fun handleFileMessageEvent(event: MessageEvent<FileMessageContent>) {
        TODO()
    }

    @EventMapping
    fun handleImageMessageEvent(event: MessageEvent<ImageMessageContent>) {
        TODO()
    }

    @EventMapping
    fun handleLocationMessageEvent(event: MessageEvent<LocationMessageContent>) {
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
        checkNotNullStickerMessageEvent(event)

        reply(event.replyToken, StickerMessage(event.message.packageId, event.message.stickerId))
    }

    @EventMapping
    fun handleTextMessageEvent(event: MessageEvent<TextMessageContent>) {
        checkNotNullTextMessageEvent(event)

        val commandEvent = textMessageEventConverter.convert(event)
        log.info { commandEvent }

        val replyToken = commandEvent.replyToken
        when (val command = commandEvent.message.command) {
            is Buttons -> {
                val imageUrl =
                    createStaticFileUri("/buttons/1040.jpg")
                val buttonsTemplate = ButtonsTemplate(
                    imageUrl,
                    "My button sample",
                    "Hello, my button",
                    listOf(
                        URIAction("Go to line.me", "https://line.me"),
                        PostbackAction("Say hello1", "hello こんにちは"),
                        PostbackAction("言 hello2", "hello こんにちは", "hello こんにちは"),
                        MessageAction("Say textMessageModel", "Rice=米")
                    )
                )
                val templateMessage = TemplateMessage("Button alt text", buttonsTemplate)
                reply(replyToken, templateMessage)
            }
            is Bye -> {
                val source = command.source
                when (source) {
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
                    createStaticFileUri("/buttons/1040.jpg")
                val carouselTemplate = CarouselTemplate(
                    Arrays.asList(
                        CarouselColumn(
                            imageUrl, "hoge", "fuga", listOf(
                                URIAction(
                                    "Go to line.me",
                                    "https://line.me"
                                ),
                                URIAction(
                                    "Go to line.me",
                                    "https://line.me"
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
                                    "Say textMessageModel",
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
                reply(replyToken, FlexMessageSupplier().get())
            }
            is ImageCarousel -> {
                val imageUrl =
                    createStaticFileUri("/buttons/1040.jpg")

                val imageCarouselTemplate = ImageCarouselTemplate(
                    listOf(
                        ImageCarouselColumn(
                            imageUrl,
                            URIAction("Goto line.me", "https://line.me")
                        ),
                        ImageCarouselColumn(
                            imageUrl,
                            MessageAction("Say textMessageModel", "Rice=米")
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
                        createStaticFileUri("/rich"),
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
                                    throwable.message ?: "Fail to get profile without error textMessageModel."
                                )
                                return@whenComplete
                            }

                            reply(
                                replyToken,
                                Arrays.asList<Message>(
                                    TextMessage(
                                        "Display name: " + profile.displayName
                                    ),
                                    TextMessage("Status textMessageModel: " + profile.statusMessage)
                                )
                            )
                        }
                } else {
                    replyText(replyToken, "Bot can't use profile API without user ID")
                }
            }
            is QuickReply -> {
                reply(replyToken, MessageWithQuickReplySupplier().get())
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
                                    stringJoiner.add(word.furigana ?: word.surface)
                                }
                            }
                        }

                        val resultString = stringJoiner.toString()

                        log.info { resultString }
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
                                fixedWidthMp4,
                                fixedWidthUrl
                            )
                        )
                    }
            }
        }
    }

    @EventMapping
    fun handleUnknownMessageEvent(event: MessageEvent<UnknownMessageContent>) {
        TODO()
    }

    @EventMapping
    fun handleVideoMessageEvent(event: MessageEvent<VideoMessageContent>) {
        TODO()
    }

    // Indicates that the user has linked their LINE account with a provider's (your) service account.
    // You can reply to this events. For more information, see Linking user accounts.
    @EventMapping
    fun handleAccountLinkEvent(event: AccountLinkEvent) {
        TODO()
    }

    // Indicates that the user performed a postback action. You can reply to this events.
    @EventMapping
    fun handleBeaconEvent(event: BeaconEvent) {
        TODO()
    }

    // Indicates that your account was added as a friend (or unblocked). You can reply to this events.
    @EventMapping
    fun handleFollowEvent(event: FollowEvent) {
        TODO()
    }

    // Indicates that your bot joined a group chat.
    @EventMapping
    fun handleJoinEvent(event: JoinEvent) {
        TODO()
    }

    // Indicates that a user deleted your bot from a group or that your bot left a group or room.
    @EventMapping
    fun handleLeaveEvent(event: LeaveEvent) {
        TODO()
    }

    // Indicates that the user performed a postback action. You can reply to this events.
    @EventMapping
    fun handlePostbackEvent(event: PostbackEvent) {
        TODO()
    }

    // Indicates that your account was blocked.
    @EventMapping
    fun handleUnfollowEvent(event: UnfollowEvent) {
        TODO()
    }

    @EventMapping
    fun handleUnknownEvent(event: UnknownEvent) {
        TODO()
    }

    @EventMapping
    fun handleOtherEvent(event: Event) {
        TODO()
    }

    private fun reply(replyToken: String, message: Message) {
        reply(replyToken, listOf(message))
    }

    private fun reply(replyToken: String, messages: List<Message>) {
        try {
            val apiResponse = lineMessagingClient
                .replyMessage(ReplyMessage(replyToken, messages))
                .get()
            log.info("Sent messages: {}", apiResponse)
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

    companion object {
        private fun createStaticFileUri(path: String): String {
            return ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(path).build()
                .toUriString()
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
    }
}
