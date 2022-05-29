package com.example.trainingproject.repositories

import com.example.trainingproject.util.Resource
import com.google.firebase.auth.AuthResult

interface MainRepository {

    suspend fun login(email: String, password: String): Resource<AuthResult>
}