package com.example.trainingproject.repositories

import com.example.trainingproject.data.models.User
import com.example.trainingproject.util.Resource

interface MainRepository {

    suspend fun getUsers(): Resource<List<User>>
}