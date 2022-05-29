package com.example.trainingproject.util

import androidx.annotation.StringRes

sealed class Resource<T>(
    val data: T? = null,
    val message: String? = null,
    @StringRes val errorRes: Int? = null
) {
    class Success<T>(data: T) : Resource<T>(data)
    class Error<T>(errorRes: Int? = null, message: String? = null, data: T? = null) :
        Resource<T>(data, message, errorRes)

    class Loading<T>(data: T? = null) : Resource<T>(data)
}
