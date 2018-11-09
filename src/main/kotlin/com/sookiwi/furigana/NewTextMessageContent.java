package com.sookiwi.furigana;

import com.linecorp.bot.model.event.message.MessageContent;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
public class NewTextMessageContent implements MessageContent {
    @NonNull
    String id;
    @NonNull
    NewTextMessage message;

    public NewTextMessageContent(final String id, final NewTextMessage message) {
        this.id = id;
        this.message = message;
    }
}
