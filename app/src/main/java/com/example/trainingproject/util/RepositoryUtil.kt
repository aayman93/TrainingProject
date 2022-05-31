package com.example.trainingproject.util

import com.google.firebase.crashlytics.FirebaseCrashlytics


inline fun <T> safeCall(action: () -> Resource<T>): Resource<T> {
    return try {
        action()
    } catch (e: Exception) {
        FirebaseCrashlytics.getInstance().log(e.message ?: "Unknown error")
        Resource.Error(message = e.localizedMessage ?: "An unknown error occurred")
    }
}