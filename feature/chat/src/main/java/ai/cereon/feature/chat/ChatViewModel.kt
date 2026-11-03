package ai.cereon.feature.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ai.cereon.core.database.MessageEntity
import ai.cereon.domain.ChatRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

data class ChatUiState(
    val conversationId: String? = null,
    val messages: List<MessageEntity> = emptyList(),
    val draft: String = "",
    val isGenerating: Boolean = false,
    val error: String? = null
)

class ChatViewModel(private val repository: ChatRepository) : ViewModel() {
    private val _state = MutableStateFlow(ChatUiState())
    val state: StateFlow<ChatUiState> = _state.asStateFlow()

    fun start(model: String) = viewModelScope.launch {
        val id = _state.value.conversationId ?: repository.newConversation(model).also { _state.value = _state.value.copy(conversationId = it) }
        repository.messages(id).collect { _state.value = _state.value.copy(messages = it) }
    }

    fun onDraftChanged(value: String) { _state.value = _state.value.copy(draft = value) }

    fun send() = viewModelScope.launch {
        val prompt = _state.value.draft.trim()
        if (prompt.isEmpty() || _state.value.isGenerating) return@launch
        val id = _state.value.conversationId ?: repository.newConversation("default").also { _state.value = _state.value.copy(conversationId = it) }
        _state.value = _state.value.copy(draft = "", isGenerating = true, error = null)
        try {
            repository.send(id, prompt) { /* Room emits persisted streamed response after completion */ }
        } catch (e: Exception) {
            _state.value = _state.value.copy(error = e.message ?: "The request failed")
        } finally {
            _state.value = _state.value.copy(isGenerating = false)
        }
    }
}
