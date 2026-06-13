package com.example.fitbody.model

data class Message(
    val id: Int = 0,
    val sender_id: Int,
    val receiver_id: Int,
    val content: String,
    val timestamp: String
)
