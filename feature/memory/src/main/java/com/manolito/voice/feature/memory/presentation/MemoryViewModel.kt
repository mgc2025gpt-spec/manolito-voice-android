package com.manolito.voice.feature.memory.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.manolito.voice.core.model.memory.MemoryItem
import com.manolito.voice.core.network.api.MemoryApi
import kotlinx.coroutines.launch

data class MemoryUiState(
    val isLoading: Boolean = true,
    val items: List<MemoryItem> = emptyList(),
    val error: String? = null,
)

class MemoryViewModel(
    private val memoryApi: MemoryApi = MemoryApi(),
) : ViewModel() {
    var uiState by mutableStateOf(MemoryUiState())
        private set

    init {
        load()
    }

    fun load() {
        uiState = uiState.copy(isLoading = true, error = null)
        viewModelScope.launch {
            val result = memoryApi.listMemory()
            uiState = result.fold(
                onSuccess = { MemoryUiState(isLoading = false, items = it) },
                onFailure = { MemoryUiState(isLoading = false, error = "No pude cargar la memoria todavía.") },
            )
        }
    }
}
