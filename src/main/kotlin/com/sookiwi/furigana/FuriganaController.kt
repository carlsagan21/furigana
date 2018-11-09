package com.sookiwi.furigana

import com.linecorp.bot.client.LineMessagingClient
import com.linecorp.bot.model.event.*
import com.linecorp.bot.model.event.message.*
import com.linecorp.bot.model.message.LocationMessage
import com.linecorp.bot.model.message.Message
import com.linecorp.bot.spring.boot.annotation.EventMapping
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler
import mu.KotlinLogging

@LineMessageHandler
class FuriganaController(
        private val lineMessagingClient: LineMessagingClient
) {
    private val log = KotlinLogging.logger {}

    @EventMapping
    fun handleAudioMessageEvent(event: MessageEvent<AudioMessageContent>) {
        TODO()
    }

    @EventMapping
    fun handleFileMessageEvent(event: MessageEvent<FileMessageContent>) {
        TODO()
    }

    @EventMapping
    fun handleImageMessageEvent(event: MessageEvent<ImageMessageContent>) {
        TODO()
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
    fun handleStickerMessageEvent(event: MessageEvent<StickerMessageContent>) {
        TODO()
    }

    @EventMapping
    fun handleTextMessageEvent(event: MessageEvent<TextMessageContent>) {
        log.info { "=================" }
        log.info { "TextMessageContent" }
        log.info { event }
        log.info { "=================" }
    }

    @EventMapping
    fun handleNewTextMessageEvent(event: MessageEvent<NewTextMessageContent>) {
        log.info { "=================" }
        log.info { "NewTextMessageContent" }
        log.info { event }
        log.info { "=================" }
    }

    @EventMapping
    fun handleUnknownMessageEvent(event: MessageEvent<UnknownMessageContent>) {
        TODO()
    }

    @EventMapping
    fun handleVideoMessageEvent(event: MessageEvent<VideoMessageContent>) {
        TODO()
    }

    // Indicates that the user has linked their LINE account with a provider's (your) service account. You can reply to this events. For more information, see Linking user accounts.
    @EventMapping
    fun handleAccountLinkEvent(event: AccountLinkEvent) {
        TODO()
    }

    // Indicates that the user performed a postback action. You can reply to this events.
    @EventMapping
    fun handleBeaconEvent(event: BeaconEvent) {
        TODO()
    }

    // Indicates that your account was added as a friend (or unblocked). You can reply to this events.
    @EventMapping
    fun handleFollowEvent(event: FollowEvent) {
        TODO()
    }

    // Indicates that your bot joined a group chat.
    @EventMapping
    fun handleJoinEvent(event: JoinEvent) {
        TODO()
    }

    // Indicates that a user deleted your bot from a group or that your bot left a group or room.
    @EventMapping
    fun handleLeaveEvent(event: LeaveEvent) {
        TODO()
    }

    // Indicates that the user performed a postback action. You can reply to this events.
    @EventMapping
    fun handlePostbackEvent(event: PostbackEvent) {
        TODO()
    }

    // Indicates that your account was blocked.
    @EventMapping
    fun handleUnfollowEvent(event: UnfollowEvent) {
        TODO()
    }

    @EventMapping
    fun handleUnknownEvent(event: UnknownEvent) {
        TODO()
    }

    @EventMapping
    fun handleOtherEvent(event: Event) {
        TODO()
    }

    private fun reply(replyToken: String?, message: Message?) {
        TODO()
    }

    private fun reply(replyToken: String?, messages: List<Message?>?) {
        TODO()
    }

}
