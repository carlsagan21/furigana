package com.sookiwi.furigana.textMessageModel

import com.linecorp.bot.model.event.MessageEvent
import com.linecorp.bot.model.event.message.TextMessageContent
import com.linecorp.bot.model.event.source.Source
import com.sookiwi.furigana.exception.NoArgumentCommand
import org.springframework.core.convert.converter.Converter
import org.springframework.stereotype.Component

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
data class Furi(val message: String) : TextMessageCommand()
data class Gif(val message: String) : TextMessageCommand()

@Component
class TextMessageEventConverter
    : Converter<MessageEvent<TextMessageContent>, MessageEvent<TextMessageCommandContent>> {
    override fun convert(messageEvent: MessageEvent<TextMessageContent>): MessageEvent<TextMessageCommandContent> {
        val splitMessage = messageEvent.message.text.split(regex = "\\s+".toRegex(), limit = 2)
        return replaceEventMessage(
            messageEvent,
            when (splitMessage.first().toLowerCase()) {
                "/buttons" -> Buttons
                "/bye" -> Bye(messageEvent.source)
                "/carousel" -> Carousel
                "/confirm" -> Confirm
                "/flex" -> Flex
                "/imagecarousel" -> ImageCarousel
                "/imagemap" -> ImageMap
                "/profile" -> Profile(messageEvent.source.userId)
                "/quickreply" -> QuickReply
                "/furi" -> {
                    if (splitMessage.size != 2) {
                        throw NoArgumentCommand("Furigana command")
                    }
                    Furi(splitMessage.last())
                }
                "/gif" -> {
                    if (splitMessage.size != 2) {
                        throw NoArgumentCommand("Gif command")
                    }
                    Gif(splitMessage.last())
                }
                else -> Furi(messageEvent.message.text)
            }
        )
    }

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
