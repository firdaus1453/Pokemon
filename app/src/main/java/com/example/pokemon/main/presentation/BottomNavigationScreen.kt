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
import androidx.compose.ui.tooling.preview.Preview
import com.example.pokemon.core.presentation.designsystem.PersonIcon
import com.example.pokemon.core.presentation.designsystem.PokemonTheme

@Composable
fun BottomNavigationScreen() {
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
                AppDestinations.HOME -> Greeting("Home", padding)
                AppDestinations.PROFILE -> Greeting("Profile", padding)
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

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!", modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    PokemonTheme {
        Greeting("Android")
    }
}