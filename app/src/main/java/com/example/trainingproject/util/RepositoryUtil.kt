package com.example.trainingproject.util

inline fun <T> safeCall(action: () -> Resource<T>): Resource<T> {
    return try {
        action()
    } catch (e: Exception) {
        Resource.Error(message = e.localizedMessage ?: "An unknown error occurred")
    }
}