package com.sol.deepseekcompanion.data.repository

import com.sol.deepseekcompanion.data.api.DeepseekApi
import com.sol.deepseekcompanion.data.model.ChatRequest
import com.sol.deepseekcompanion.data.model.ChatResponse
import com.sol.deepseekcompanion.data.model.Message
import javax.inject.Inject

class ChatRepository @Inject constructor(
    private val api: DeepseekApi
) {
    private val apiKey = "sk-948ddd9855784e9faa2803215189edd0"

    suspend fun sendMessage(messages: List<Message>): ChatResponse {
        val request = ChatRequest(messages = messages)
        return api.chat("Bearer $apiKey", request)
    }
} 