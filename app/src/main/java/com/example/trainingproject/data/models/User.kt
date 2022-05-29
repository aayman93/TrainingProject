package com.example.trainingproject.data.models

import com.example.trainingproject.util.Constants.DEFAULT_PROFILE_PICTURE_URL

data class User(
    val uid: String = "",
    val username: String = "",
    val email: String = "",
    val profilePictureUrl: String = DEFAULT_PROFILE_PICTURE_URL
)