package com.manolito.voice.core.network.api

import com.manolito.voice.core.model.history.ConversationSummary
import com.manolito.voice.core.network.client.NetworkConfig
import com.manolito.voice.core.network.dto.ConversationListDto
import com.manolito.voice.core.network.dto.toModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.Request

class HistoryApi(
    private val client: OkHttpClient = OkHttpClient(),
    private val json: Json = Json { ignoreUnknownKeys = true },
) {
    suspend fun listConversations(): Result<List<ConversationSummary>> = withContext(Dispatchers.IO) {
        runCatching {
            val request = Request.Builder()
                .url("${NetworkConfig.BASE_URL}/conversation")
                .get()
                .build()

            client.newCall(request).execute().use { response ->
                check(response.isSuccessful) { "HTTP ${response.code}" }
                val body = response.body?.string().orEmpty()
                val dto = json.decodeFromString<ConversationListDto>(body)
                dto.items.map { it.toModel() }
            }
        }
    }
}
