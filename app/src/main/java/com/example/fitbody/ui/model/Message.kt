package com.example.fitbody.model

data class Message(
    val id: Int,
    val sender_id: Int,
    val receiver_id: Int,
    val message: String,
    val created_at: String
)