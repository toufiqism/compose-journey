package com.sol.deepseekcompanion.data.model

data class ChatRequest(
    val model: String ="deepseek-chat",// "deepseek-reasoner",
    val messages: List<Message>,
    val stream: Boolean = false
)

data class Message(
    val role: String,
    val content: String
) 