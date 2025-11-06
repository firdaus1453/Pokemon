package com.example.pokemon.profile.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokemon.profile.domain.ProfileRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val profileRepository: ProfileRepository
) : ViewModel() {

    var state by mutableStateOf(ProfileState())
        private set

    private val eventChannel = Channel<ProfileEvent>()
    val events = eventChannel.receiveAsFlow()

    init {
        loadUserProfile()
    }

    fun onAction(action: ProfileAction) {
        when (action) {
            ProfileAction.OnToggleEmailVisibilityClick -> {
                state = state.copy(
                    isEmailVisible = !state.isEmailVisible
                )
            }
            ProfileAction.OnLogoutClick -> logout()
        }
    }

    private fun loadUserProfile() {
        viewModelScope.launch {
            state = state.copy(isLoading = true)
            val user = profileRepository.getCurrentUser()
            state = state.copy(
                name = user?.name ?: "",
                email = user?.email ?: "",
                isLoading = false
            )
        }
    }

    private fun logout() {
        viewModelScope.launch {
            state = state.copy(isLoggingOut = true)
            profileRepository.logout()
            eventChannel.send(ProfileEvent.LogoutSuccess)
        }
    }
}
