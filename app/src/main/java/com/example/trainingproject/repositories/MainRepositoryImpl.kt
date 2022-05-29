package com.example.trainingproject.repositories

import com.example.trainingproject.util.Resource
import com.example.trainingproject.util.safeCall
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MainRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val storage: FirebaseStorage
) : MainRepository {

    override suspend fun login(
        email: String,
        password: String
    ): Resource<AuthResult> = withContext(Dispatchers.IO) {
        safeCall {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            Resource.Success(result)
        }
    }
}