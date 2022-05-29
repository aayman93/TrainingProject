package com.example.trainingproject.repositories

import com.example.trainingproject.data.models.User
import com.example.trainingproject.util.Constants.KEY_COLLECTION_USERS
import com.example.trainingproject.util.Resource
import com.example.trainingproject.util.safeCall
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : AuthRepository {

    override suspend fun login(
        email: String,
        password: String
    ): Resource<AuthResult> = withContext(Dispatchers.IO) {
        safeCall {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            Resource.Success(result)
        }
    }

    override suspend fun register(
        username: String,
        email: String,
        password: String
    ): Resource<AuthResult> = withContext(Dispatchers.IO) {
        safeCall {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            val uid = result.user?.uid!!
            addUserToDatabase(uid, username, email)
            Resource.Success(result)
        }
    }

    private suspend fun addUserToDatabase(userId: String, username: String, email: String) {
        val user = User(userId, username, email)
        firestore.collection(KEY_COLLECTION_USERS).document(userId).set(user).await()
    }
}