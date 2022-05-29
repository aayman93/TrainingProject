package com.example.trainingproject.ui.register

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.trainingproject.R
import com.example.trainingproject.repositories.AuthRepository
import com.example.trainingproject.util.Constants.MAX_USERNAME_LENGTH
import com.example.trainingproject.util.Constants.MIN_PASSWORD_LENGTH
import com.example.trainingproject.util.Constants.MIN_USERNAME_LENGTH
import com.example.trainingproject.util.Event
import com.example.trainingproject.util.Resource
import com.google.firebase.auth.AuthResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {

    private val _registerStatus = MutableLiveData<Event<Resource<AuthResult>>>()
    val registerStatus: LiveData<Event<Resource<AuthResult>>> = _registerStatus

    fun register(username: String, email: String, password: String, confirmedPassword: String) {
        _registerStatus.postValue(Event(Resource.Loading()))
        if (!validateUserInput(username, email, password, confirmedPassword)) return

        viewModelScope.launch {
            val result = repository.register(username, email, password)
            _registerStatus.postValue(Event(result))
        }
    }

    private fun validateUserInput(
        username: String,
        email: String,
        password: String,
        confirmedPassword: String
    ): Boolean {
        val error = when {
            username.isBlank() || email.isBlank() ||
                    password.isBlank() || confirmedPassword.isBlank() -> R.string.error_empty_fields
            username.length < MIN_USERNAME_LENGTH -> R.string.error_username_too_short
            username.length > MAX_USERNAME_LENGTH -> R.string.error_username_too_long
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> R.string.error_invalid_email
            password.length < MIN_PASSWORD_LENGTH -> R.string.error_password_too_short
            password != confirmedPassword -> R.string.error_password_mismatch
            else -> null
        }
        error?.let { _registerStatus.postValue(Event(Resource.Error(it))) }
        return error == null
    }
}