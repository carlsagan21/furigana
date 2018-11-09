package com.sookiwi.furigana;

import com.linecorp.bot.model.event.message.MessageContent;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Getter
@ToString
@EqualsAndHashCode
@Slf4j
public class NewTextMessageContent implements MessageContent {
    @NonNull
    String id;
    @NonNull
    NewTextMessage message;

    public NewTextMessageContent(final String id, final NewTextMessage message) {
        log.debug("NewTextMessageContent");

        this.id = id;
        this.message = message;
    }
}
