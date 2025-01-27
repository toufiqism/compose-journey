package com.sol.deepseekcompanion.data.api

import com.sol.deepseekcompanion.data.model.ChatRequest
import com.sol.deepseekcompanion.data.model.ChatResponse
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface DeepseekApi {
    @POST("chat/completions")
    suspend fun chat(
        @Header("Authorization") apiKey: String,
        @Header("Content-Type") contentType: String = "application/json",
        @Body request: ChatRequest
    ): ChatResponse
} 