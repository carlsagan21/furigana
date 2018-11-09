package com.sookiwi.furigana

import com.linecorp.bot.model.action.*
import com.linecorp.bot.model.message.Message
import com.linecorp.bot.model.message.TextMessage
import com.linecorp.bot.model.message.quickreply.QuickReply
import com.linecorp.bot.model.message.quickreply.QuickReplyItem
import java.util.function.Supplier

class MessageWithQuickReplySupplier : Supplier<Message> {
    override fun get(): Message {
        val items = listOf(
                QuickReplyItem.builder()
                        .action(MessageAction("MessageAction", "MessageAction"))
                        .build(),
                QuickReplyItem.builder()
                        .action(CameraAction.withLabel("CameraAction"))
                        .build(),
                QuickReplyItem.builder()
                        .action(CameraRollAction.withLabel("CameraRollAction"))
                        .build(),
                QuickReplyItem.builder()
                        .action(LocationAction.withLabel("Location"))
                        .build(),
                QuickReplyItem.builder()
                        .action(PostbackAction.builder()
                                .label("PostbackAction")
                                .text("PostbackAction clicked")
                                .data("{PostbackAction: true}")
                                .build())
                        .build()
        )

        val quickReply = QuickReply.items(items)

        return TextMessage
                .builder()
                .text("Message with QuickReply")
                .quickReply(quickReply)
                .build()
    }
}
