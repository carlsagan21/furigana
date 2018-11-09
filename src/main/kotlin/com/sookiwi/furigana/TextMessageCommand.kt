package com.sookiwi.furigana

import com.linecorp.bot.model.event.MessageEvent
import com.linecorp.bot.model.event.message.TextMessageContent
import com.linecorp.bot.model.event.source.Source
import org.springframework.core.convert.converter.Converter

sealed class TextMessageCommand
object Buttons : TextMessageCommand()
data class Bye(val source: Source) : TextMessageCommand()
object Carousel : TextMessageCommand()
object Confirm : TextMessageCommand()
object Flex : TextMessageCommand()
object ImageCarousel : TextMessageCommand()
object ImageMap : TextMessageCommand()
data class Profile(val userId: String?) : TextMessageCommand()
object QuickReply : TextMessageCommand()
data class Others(val message: String) : TextMessageCommand()

class TextMessageEventConverter
    : Converter<MessageEvent<TextMessageContent>, MessageEvent<TextMessageCommandContent>> {
    override fun convert(messageEvent: MessageEvent<TextMessageContent>): MessageEvent<TextMessageCommandContent> =
        replaceEventMessage(
            messageEvent,
            when (messageEvent.message.text.toLowerCase()) {
                "buttons" -> Buttons
                "bye" -> Bye(messageEvent.source)
                "carousel" -> Carousel
                "confirm" -> Confirm
                "flex" -> Flex
                "imagecarousel" -> ImageCarousel
                "imagemap" -> ImageMap
                "profile" -> Profile(messageEvent.source.userId)
                "quickreply" -> QuickReply
                else -> Others(messageEvent.message.text)
            }
        )

    private fun replaceEventMessage(
        origin: MessageEvent<TextMessageContent>,
        command: TextMessageCommand
    ): MessageEvent<TextMessageCommandContent> = MessageEvent(
        origin.replyToken,
        origin.source,
        TextMessageCommandContent(origin.message.id, command),
        origin.timestamp
    )
}
