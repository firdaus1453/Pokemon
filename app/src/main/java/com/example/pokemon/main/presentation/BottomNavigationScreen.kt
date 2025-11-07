package com.example.pokemon.main.presentation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.pokemon.core.presentation.designsystem.PersonIcon
import com.example.pokemon.home.domain.Pokemon
import com.example.pokemon.home.presentation.home.RootHomeScreen
import com.example.pokemon.profile.presentation.ProfileScreenRoot

@Composable
fun BottomNavigationScreen(
    onLogout: () -> Unit = {},
    onNavigateToDetail: (Int) -> Unit = {}
) {
    var currentDestination by rememberSaveable { mutableStateOf(AppDestinations.HOME) }

    NavigationSuiteScaffold(
        navigationSuiteItems = {
            AppDestinations.entries.forEach {
                item(
                    icon = {
                        it.icon()
                    },
                    label = { Text(it.label) },
                    selected = it == currentDestination,
                    onClick = { currentDestination = it })
            }
        }) {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            val padding = Modifier.padding(innerPadding)
            when (currentDestination) {
                AppDestinations.HOME -> RootHomeScreen(
                    modifier = padding,
                    onItemClick = onNavigateToDetail
                )
                AppDestinations.PROFILE -> ProfileScreenRoot(
                    modifier = padding,
                    onLogoutSuccess = onLogout
                )
            }
        }
    }
}

enum class AppDestinations(
    val label: String,
    val icon: @Composable () -> Unit,
) {
    HOME(
        "Home", {
            Icon(
                Icons.Default.Home, contentDescription = "Home"
            )
        }),
    PROFILE("Profile", {
        Icon(
            PersonIcon, contentDescription = "Profile"
        )
    }),
}
