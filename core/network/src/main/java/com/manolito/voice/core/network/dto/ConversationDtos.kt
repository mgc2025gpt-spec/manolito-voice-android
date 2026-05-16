package com.manolito.voice.core.network.dto

import com.manolito.voice.core.model.conversation.AssistantReply
import com.manolito.voice.core.model.conversation.ConversationRequest
import com.manolito.voice.core.model.conversation.ConversationResponse
import kotlinx.serialization.Serializable

@Serializable
data class ConversationRequestDto(
    val text: String,
    val conversationId: String? = null,
)

@Serializable
data class AssistantReplyDto(
    val text: String,
    val audioUrl: String? = null,
)

@Serializable
data class ConversationResponseDto(
    val conversationId: String,
    val transcript: String? = null,
    val reply: AssistantReplyDto,
)

fun ConversationRequest.toDto() = ConversationRequestDto(
    text = text,
    conversationId = conversationId,
)

fun ConversationResponseDto.toModel() = ConversationResponse(
    conversationId = conversationId,
    transcript = transcript,
    reply = AssistantReply(
        text = reply.text,
        audioUrl = reply.audioUrl,
    ),
)
