package com.sol.deepseekcompanion.data.api

import com.sol.deepseekcompanion.data.model.ChatRequest
import com.sol.deepseekcompanion.data.model.ChatResponse
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface DeepseekApi {
    @POST("v1/chat/completions")
    suspend fun chat(
        @Header("Authorization") apiKey: String,
        @Body request: ChatRequest
    ): ChatResponse
} 