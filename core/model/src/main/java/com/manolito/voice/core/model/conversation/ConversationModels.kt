package com.manolito.voice.core.model.conversation

data class ConversationRequest(
    val text: String,
    val conversationId: String? = null,
)

data class AssistantReply(
    val text: String,
    val audioUrl: String? = null,
)

data class ConversationResponse(
    val conversationId: String,
    val transcript: String? = null,
    val reply: AssistantReply,
)
