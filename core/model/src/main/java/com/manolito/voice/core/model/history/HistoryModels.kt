package com.manolito.voice.core.model.history

data class ConversationSummary(
    val conversationId: String,
    val title: String,
    val summary: String,
    val messageCount: Int,
    val updatedAt: String?,
)
