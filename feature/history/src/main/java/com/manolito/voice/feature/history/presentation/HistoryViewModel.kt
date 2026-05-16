package com.manolito.voice.feature.history.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.manolito.voice.core.model.history.ConversationSummary
import com.manolito.voice.core.network.api.HistoryApi
import kotlinx.coroutines.launch

data class HistoryUiState(
    val isLoading: Boolean = true,
    val items: List<ConversationSummary> = emptyList(),
    val error: String? = null,
)

class HistoryViewModel(
    private val historyApi: HistoryApi = HistoryApi(),
) : ViewModel() {
    var uiState by mutableStateOf(HistoryUiState())
        private set

    init {
        load()
    }

    fun load() {
        uiState = uiState.copy(isLoading = true, error = null)
        viewModelScope.launch {
            val result = historyApi.listConversations()
            uiState = result.fold(
                onSuccess = { HistoryUiState(isLoading = false, items = it) },
                onFailure = { HistoryUiState(isLoading = false, error = "No pude cargar el historial todavía.") },
            )
        }
    }
}
