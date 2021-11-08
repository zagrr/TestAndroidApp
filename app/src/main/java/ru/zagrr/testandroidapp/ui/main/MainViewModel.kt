package ru.zagrr.testandroidapp.ui.main

import android.util.Log
import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import ru.zagrr.testandroidapp.model.User
import ru.zagrr.testandroidapp.repository.Repository
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

    private val usersChannel = Channel<UsersEvents>()
    val usersEvent = usersChannel.receiveAsFlow()

    private var userSearchFlowState = MutableStateFlow("")

    private val usersFlow = userSearchFlowState.flatMapLatest {
        repository.getUsers(it)
    }

    val users = usersFlow.asLiveData(viewModelScope.coroutineContext)

    fun getUsers(searchQuery : String) = viewModelScope.launch {
        userSearchFlowState.value = searchQuery
    }

    fun refreshUsers() = viewModelScope.launch {
        repository.refreshUsers()
    }

    fun deleteUsers(users : List<User>) = viewModelScope.launch {

        repository.deleteUsers(users)
        usersChannel.send(UsersEvents.UsersDeleted(users))
    }

    fun saveUsers(users : List<User>) = viewModelScope.launch {
        repository.saveUsers(users)
    }


    sealed class UsersEvents {

        data class UsersDeleted(val users: List<User>) : UsersEvents()
    }
}