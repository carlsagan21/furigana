package com.sookiwi.furigana

import com.linecorp.bot.client.LineMessagingClient
import com.linecorp.bot.model.ReplyMessage
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
import com.linecorp.bot.model.message.ImagemapMessage
import com.linecorp.bot.model.message.Message
import com.linecorp.bot.model.message.TemplateMessage
import com.linecorp.bot.model.message.TextMessage
import com.linecorp.bot.model.message.imagemap.ImagemapArea
import com.linecorp.bot.model.message.imagemap.ImagemapBaseSize
import com.linecorp.bot.model.message.imagemap.MessageImagemapAction
import com.linecorp.bot.model.message.imagemap.URIImagemapAction
import com.linecorp.bot.model.message.template.ImageCarouselColumn
import com.linecorp.bot.model.message.template.ImageCarouselTemplate
import com.linecorp.bot.spring.boot.annotation.EventMapping
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler
import mu.KotlinLogging
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import java.util.concurrent.ExecutionException

@LineMessageHandler
class FuriganaController(
    private val lineMessagingClient: LineMessagingClient
) {
    private val log = KotlinLogging.logger {}
    private val textMessageEventConverter: TextMessageEventConverter = TextMessageEventConverter()

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
//        val requestLocationMessageContent: LocationMessageContent = event.message
//                ?: throw IllegalArgumentException("Message is null")
//        val responseLocationMessage: LocationMessage? = LocationMessage.builder()
//                .title(requestLocationMessageContent.title)
//                .address(requestLocationMessageContent.address)
//                .latitude(requestLocationMessageContent.latitude)
//                .longitude(requestLocationMessageContent.longitude)
//                .build()
//        reply(event.replyToken, responseLocationMessage)
    }

    @EventMapping
    fun handleStickerMessageEvent(event: MessageEvent<StickerMessageContent>) {
        TODO()
    }

    @EventMapping
    fun handleTextMessageEvent(event: MessageEvent<TextMessageContent>) {
        checkNotNullTextMessageEvent(event)

        val commandEvent = textMessageEventConverter.convert(event)
        log.info { commandEvent }

        val replyToken = commandEvent.replyToken
        when (val command = commandEvent.message.command) {
            is Buttons -> {
            }
            is Bye -> {
            }
            is Carousel -> {
            }
            is Confirm -> {
            }
            is Flex -> {
                reply(replyToken, ExampleFlexMessageSupplier().get())
            }
            is ImageCarousel -> {
                val imageUrl = createUri("/static/buttons/1040.jpg")
                val imageCarouselTemplate = ImageCarouselTemplate(
                    listOf(
                        ImageCarouselColumn(
                            imageUrl,
                            URIAction("Goto line.me", "https://line.me")
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
                        createUri("/static/rich"),
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
            }
            is QuickReply -> {
                reply(replyToken, MessageWithQuickReplySupplier().get())
            }
            is Others -> {
                replyText(replyToken, command.message)
            }
        }
    }

    private fun createUri(path: String): String {
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

    private fun checkNotNullTextMessageEvent(event: MessageEvent<TextMessageContent>) {
        checkNotNullMessageEvent(event)
        checkNotNull(event.message.id)
        checkNotNull(event.message.text)
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

        this.reply(replyToken, TextMessage(messageToReply))
    }
}
