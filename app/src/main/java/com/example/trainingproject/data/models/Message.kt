package com.example.trainingproject.data.models

import com.google.firebase.firestore.Exclude
import java.util.*

data class Message(
    val body: String? = null,
    val sender: String? = null,
    val type: MessageType? = null,
    val date: Date? = null,
    @get:Exclude val messageId: String? = null,
)

enum class MessageType {
    TEXT,
    IMAGE
}
