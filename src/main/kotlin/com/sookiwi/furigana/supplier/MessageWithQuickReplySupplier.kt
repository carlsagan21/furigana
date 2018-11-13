package com.sookiwi.furigana.supplier

import com.linecorp.bot.model.action.CameraAction
import com.linecorp.bot.model.action.CameraRollAction
import com.linecorp.bot.model.action.LocationAction
import com.linecorp.bot.model.action.MessageAction
import com.linecorp.bot.model.action.PostbackAction
import com.linecorp.bot.model.message.Message
import com.linecorp.bot.model.message.TextMessage
import com.linecorp.bot.model.message.quickreply.QuickReply
import com.linecorp.bot.model.message.quickreply.QuickReplyItem

fun messageWithQuickReplySupplier(): Message {
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
            .action(
                PostbackAction.builder()
                    .label("PostbackAction")
                    .text("PostbackAction clicked")
                    .data("{PostbackAction: true}")
                    .build()
            )
            .build()
    )

    val quickReply = QuickReply.items(items)

    return TextMessage
        .builder()
        .text("Message with QuickReply")
        .quickReply(quickReply)
        .build()
}
