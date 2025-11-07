package com.example.pokemon.home.presentation.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

class HomeViewModel() : ViewModel() {

    var state by mutableStateOf(HomeState())
        private set

    private val eventHome = Channel<HomeEvent>()
    val events = eventHome.receiveAsFlow()

    fun onAction(action: HomeAction) {
        when(action) {
            is HomeAction.OnItemClick -> Unit
        }
    }
}