package com.example.trainingproject.ui.login

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.trainingproject.R
import com.example.trainingproject.repositories.MainRepository
import com.example.trainingproject.util.Event
import com.example.trainingproject.util.Resource
import com.google.firebase.auth.AuthResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: MainRepository
) : ViewModel() {

    private val _loginStatus = MutableLiveData<Event<Resource<AuthResult>>>()
    val loginStatus: LiveData<Event<Resource<AuthResult>>> = _loginStatus

    fun login(email: String, password: String) {
        _loginStatus.postValue(Event(Resource.Loading()))
        if (!validateUserInput(email, password)) return

        viewModelScope.launch {
            val result = repository.login(email, password)
            _loginStatus.postValue(Event(result))
        }
    }

    private fun validateUserInput(email: String, password: String): Boolean {
        return if (email.isBlank() || password.isBlank()) {
            val error = R.string.error_empty_fields
            _loginStatus.postValue(Event(Resource.Error(error)))
            false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            val error = R.string.error_invalid_email
            _loginStatus.postValue(Event(Resource.Error(error)))
            false
        } else {
            true
        }
    }
}