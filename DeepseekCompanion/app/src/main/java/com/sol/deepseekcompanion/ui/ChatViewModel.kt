package com.sol.deepseekcompanion.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sol.deepseekcompanion.data.model.Message
import com.sol.deepseekcompanion.data.repository.ChatRepository
import com.sol.deepseekcompanion.util.NetworkConnectivity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val repository: ChatRepository,
    private val networkConnectivity: NetworkConnectivity
) : ViewModel() {

    private val _messages = MutableStateFlow<List<Message>>(emptyList())
    val messages: StateFlow<List<Message>> = _messages

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun sendMessage(content: String) {
        if (!networkConnectivity.isNetworkAvailable()) {
            _error.value = "No internet connection"
            return
        }

        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null
                val userMessage = Message("user", content)
                _messages.value = _messages.value + userMessage

                val response = repository.sendMessage(_messages.value)
                val assistantMessage = response.choices.first().message
                _messages.value = _messages.value + assistantMessage
            } catch (e: Exception) {
                _error.value = e.message ?: "An error occurred"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun clearError() {
        _error.value = null
    }
} 