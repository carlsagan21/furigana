package com.sookiwi.furigana

import com.linecorp.bot.model.event.Event
import com.linecorp.bot.model.event.MessageEvent
import com.linecorp.bot.model.event.message.TextMessageContent
import com.linecorp.bot.model.message.Message
import com.linecorp.bot.model.message.TextMessage
import com.linecorp.bot.spring.boot.annotation.EventMapping
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler
import mu.KotlinLogging
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
@LineMessageHandler
class FuriganaApplication {
    private val logger = KotlinLogging.logger {}

    @EventMapping
    fun handleTextMessageEvent(event: MessageEvent<TextMessageContent>): Message {
        logger.debug("event: $event")
        val originalMessageText = event.message.text
        return TextMessage(originalMessageText)
    }

    @EventMapping
    fun handleDefaultMessageEvent(event: Event) {
        logger.debug("event: $event")
    }
}

fun main(args: Array<String>) {
    runApplication<FuriganaApplication>(*args)
}
