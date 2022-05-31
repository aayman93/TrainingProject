package com.example.trainingproject.repositories

import com.example.trainingproject.data.models.User
import com.example.trainingproject.util.Constants.KEY_COLLECTION_USERS
import com.example.trainingproject.util.Resource
import com.example.trainingproject.util.safeCall
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

    override suspend fun getUsers(): Resource<List<User>> = withContext(Dispatchers.IO) {
        safeCall {
            val uid = auth.uid!!
            val users = firestore.collection(KEY_COLLECTION_USERS)
                .get()
                .await().toObjects(User::class.java)
                .filter { user -> user.uid != uid }
                .sortedBy { it.username }
            Resource.Success(users)
        }
    }
}