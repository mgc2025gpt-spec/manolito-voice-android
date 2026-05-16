package com.manolito.voice.core.network.dto

import com.manolito.voice.core.model.history.ConversationSummary
import com.manolito.voice.core.model.memory.MemoryItem
import kotlinx.serialization.Serializable

@Serializable
data class ConversationSummaryDto(
    val conversationId: String,
    val title: String,
    val summary: String,
    val messageCount: Int,
    val updatedAt: String? = null,
)

@Serializable
data class ConversationListDto(
    val items: List<ConversationSummaryDto>,
)

@Serializable
data class MemoryItemDto(
    val id: String,
    val type: String,
    val content: String,
    val createdAt: String,
)

@Serializable
data class MemoryListDto(
    val items: List<MemoryItemDto>,
)

fun ConversationSummaryDto.toModel() = ConversationSummary(
    conversationId = conversationId,
    title = title,
    summary = summary,
    messageCount = messageCount,
    updatedAt = updatedAt,
)

fun MemoryItemDto.toModel() = MemoryItem(
    id = id,
    type = type,
    content = content,
    createdAt = createdAt,
)
