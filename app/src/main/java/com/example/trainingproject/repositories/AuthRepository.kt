package com.example.trainingproject.repositories

import com.example.trainingproject.util.Resource
import com.google.firebase.auth.AuthResult

interface AuthRepository {

    suspend fun login(email: String, password: String): Resource<AuthResult>

    suspend fun register(username: String, email: String, password: String): Resource<AuthResult>
}