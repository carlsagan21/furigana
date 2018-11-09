package com.sookiwi.furigana

import com.linecorp.bot.client.LineMessagingClient
import com.linecorp.bot.model.event.MessageEvent
import com.linecorp.bot.model.event.message.ImageMessageContent
import com.linecorp.bot.model.event.message.LocationMessageContent
import com.linecorp.bot.model.event.message.StickerMessageContent
import com.linecorp.bot.model.event.message.TextMessageContent
import com.linecorp.bot.model.message.LocationMessage
import com.linecorp.bot.model.message.Message
import com.linecorp.bot.spring.boot.annotation.EventMapping
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler
import mu.KotlinLogging

@LineMessageHandler
class FuriganaController(
        private val lineMessagingClient: LineMessagingClient
) {
    private val logger = KotlinLogging.logger {}

    @EventMapping
    fun handleTextMessageEvent(event: MessageEvent<TextMessageContent>) {
        handleTextContent(event.replyToken, event, event.message)
    }

    @EventMapping
    fun handleStickerMessageEvent(event: MessageEvent<StickerMessageContent>) {
        handleStickerContent(event.replyToken, event.message)
    }

    @EventMapping
    fun handleLocationMessageEvent(event: MessageEvent<LocationMessageContent>) {
        val requestLocationMessageContent: LocationMessageContent = event.message
                ?: throw IllegalArgumentException("Message is null")
        val responseLocationMessage: LocationMessage? = LocationMessage.builder()
                .title(requestLocationMessageContent.title)
                .address(requestLocationMessageContent.address)
                .latitude(requestLocationMessageContent.latitude)
                .longitude(requestLocationMessageContent.longitude)
                .build()
        reply(event.replyToken, responseLocationMessage)
    }

    @EventMapping
    fun handleImageMessageEvent(event: MessageEvent<ImageMessageContent>) {
//        handleHeavyContent(
//                event.replyToken,
//                event.message.id,
//                responseBody -> {
//
//        }
//        )
        // TODO
    }

    private fun reply(replyToken: String?, message: Message?) {
//        reply(replyToken, Collections.singletonList(message))
    }

    private fun reply(replyToken: String?, messages: List<Message?>?) {
//        TODO()
    }

    private fun handleTextContent(replyToken: String?, event: MessageEvent<TextMessageContent>, message: TextMessageContent?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun handleStickerContent(replyToken: String?, message: StickerMessageContent?) {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}
