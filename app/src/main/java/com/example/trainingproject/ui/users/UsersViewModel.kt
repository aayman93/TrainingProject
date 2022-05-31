package com.example.trainingproject.ui.users

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.trainingproject.data.models.User
import com.example.trainingproject.repositories.MainRepository
import com.example.trainingproject.util.Event
import com.example.trainingproject.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UsersViewModel @Inject constructor(
    private val repository: MainRepository
) : ViewModel() {

    private val _users = MutableLiveData<Event<Resource<List<User>>>>()
    val users: LiveData<Event<Resource<List<User>>>> = _users

    init {
        getUsers()
    }

    private fun getUsers() {
        _users.postValue(Event(Resource.Loading()))
        viewModelScope.launch {
            val result = repository.getUsers()
            _users.postValue(Event(result))
        }
    }
}