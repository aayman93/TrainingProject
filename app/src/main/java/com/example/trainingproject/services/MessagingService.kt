package com.example.trainingproject.services

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MessagingService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("FCM", "Refreshed token: $token")
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        Log.d("FCM", "Notification title: ${message.notification?.title}")
        Log.d("FCM", "Notification body: ${message.notification?.body}")
        Log.d("FCM", "Message title: ${message.data["title"]}")
        Log.d("FCM", "Message content: ${message.data["message"]}")
    }
}