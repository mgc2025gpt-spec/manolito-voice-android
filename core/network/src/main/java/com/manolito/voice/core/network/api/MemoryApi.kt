package com.manolito.voice.core.network.api

import com.manolito.voice.core.model.memory.MemoryItem
import com.manolito.voice.core.network.client.NetworkConfig
import com.manolito.voice.core.network.dto.MemoryListDto
import com.manolito.voice.core.network.dto.toModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.Request

class MemoryApi(
    private val client: OkHttpClient = OkHttpClient(),
    private val json: Json = Json { ignoreUnknownKeys = true },
) {
    suspend fun listMemory(): Result<List<MemoryItem>> = withContext(Dispatchers.IO) {
        runCatching {
            val request = Request.Builder()
                .url("${NetworkConfig.BASE_URL}/memory")
                .get()
                .build()

            client.newCall(request).execute().use { response ->
                check(response.isSuccessful) { "HTTP ${response.code}" }
                val body = response.body?.string().orEmpty()
                val dto = json.decodeFromString<MemoryListDto>(body)
                dto.items.map { it.toModel() }
            }
        }
    }
}
