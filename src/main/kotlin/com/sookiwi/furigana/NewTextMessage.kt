package com.sookiwi.furigana

import com.linecorp.bot.model.event.MessageEvent
import com.linecorp.bot.model.event.message.TextMessageContent
import com.linecorp.bot.model.event.source.Source
import org.springframework.core.convert.converter.Converter

sealed class NewTextMessage
object Buttons : NewTextMessage()
data class Bye(val source: Source) : NewTextMessage()
object Carousel : NewTextMessage()
object Confirm : NewTextMessage()
object Flex : NewTextMessage()
object ImageCarousel : NewTextMessage()
object ImageMap : NewTextMessage()
data class Profile(val userId: String) : NewTextMessage()
object QuickReply : NewTextMessage()
object Others : NewTextMessage()

class TextMessageEventConverter
    : Converter<MessageEvent<TextMessageContent>, MessageEvent<NewTextMessageContent>> {
    override fun convert(messageEvent: MessageEvent<TextMessageContent>): MessageEvent<NewTextMessageContent> =
            replaceEventMessage(messageEvent,
                    when (messageEvent.message.text ?: throw IllegalArgumentException()) {
                        "buttons" -> Buttons
                        "bye" -> Bye(messageEvent.source)
                        "carousel" -> Carousel
                        "confirm" -> Confirm
                        "flex" -> Flex
                        "imageCarousel" -> ImageCarousel
                        "imageMap" -> ImageMap
                        "profile" -> Profile(messageEvent.source.userId)
                        "quickReply" -> QuickReply
                        else -> Others
                    })

    private fun replaceEventMessage(origin: MessageEvent<TextMessageContent>, message: NewTextMessage)
            : MessageEvent<NewTextMessageContent> =
            MessageEvent(
                    origin.replyToken,
                    origin.source,
                    NewTextMessageContent(origin.message.id, message),
                    origin.timestamp
            )
}

