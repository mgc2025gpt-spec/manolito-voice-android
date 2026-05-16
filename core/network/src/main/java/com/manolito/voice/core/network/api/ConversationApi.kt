package com.manolito.voice.core.network.api

import com.manolito.voice.core.model.conversation.ConversationRequest
import com.manolito.voice.core.model.conversation.ConversationResponse
import com.manolito.voice.core.network.client.NetworkConfig
import com.manolito.voice.core.network.dto.ConversationResponseDto
import com.manolito.voice.core.network.dto.toDto
import com.manolito.voice.core.network.dto.toModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class ConversationApi(
    private val client: OkHttpClient = OkHttpClient(),
    private val json: Json = Json { ignoreUnknownKeys = true },
) {
    suspend fun sendMessage(input: ConversationRequest): Result<ConversationResponse> = withContext(Dispatchers.IO) {
        runCatching {
            val requestBody = json.encodeToString(input.toDto())
                .toRequestBody("application/json".toMediaType())

            val request = Request.Builder()
                .url("${NetworkConfig.BASE_URL}/conversation/message")
                .post(requestBody)
                .build()

            executeConversationRequest(request)
        }
    }

    suspend fun sendAudio(filePath: String, conversationId: String? = null): Result<ConversationResponse> = withContext(Dispatchers.IO) {
        runCatching {
            val file = File(filePath)
            check(file.exists()) { "Audio file not found" }

            val body = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart(
                    name = "audio",
                    filename = file.name,
                    body = file.asRequestBody("audio/mp4".toMediaType()),
                )
                .apply {
                    if (conversationId != null) {
                        addFormDataPart("conversationId", conversationId)
                    }
                }
                .build()

            val request = Request.Builder()
                .url("${NetworkConfig.BASE_URL}/conversation/audio")
                .post(body)
                .build()

            executeConversationRequest(request)
        }
    }

    private fun executeConversationRequest(request: Request): ConversationResponse {
        client.newCall(request).execute().use { response ->
            check(response.isSuccessful) { "HTTP ${response.code}" }
            val body = response.body?.string().orEmpty()
            val dto = json.decodeFromString<ConversationResponseDto>(body)
            return dto.toModel()
        }
    }
}
