package ru.zagrr.testapp.ui.main

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.zagrr.testapp.repository.Repository
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

    val users = repository
        .getUsers()
        .asLiveData(viewModelScope.coroutineContext) //viewModel scope for auto cancellation when viewModel is destroyed

    fun refreshUsers() {

        viewModelScope.launch {
            repository.refreshUsers()
        }
    }

    fun deleteAllUsers() {
        viewModelScope.launch {
            repository.deleteAllUsers()
        }
    }
}