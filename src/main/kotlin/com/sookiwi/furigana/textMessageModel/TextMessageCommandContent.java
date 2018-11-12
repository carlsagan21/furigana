package com.sookiwi.furigana.textMessageModel;

import com.linecorp.bot.model.event.message.MessageContent;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
public class TextMessageCommandContent implements MessageContent {
    @NonNull
    public String id;
    @NonNull
    public TextMessageCommand command;

    public TextMessageCommandContent(final String id, final TextMessageCommand command) {
        this.id = id;
        this.command = command;
    }
}
