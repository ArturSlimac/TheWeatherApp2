package com.example.theweatherapp.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.theweatherapp.ui.components.BottomNavigationBar
import com.example.theweatherapp.ui.navigation.NavigationDestination

@SuppressLint("UnrememberedMutableState")
@Composable
fun UserSettingsScreen(navController: NavController) {
    var searchText by mutableStateOf("")
    Scaffold(bottomBar = {
        BottomNavigationBar(currentDestination = NavigationDestination.UserSettings) {
            when (it) {
                is NavigationDestination.LocalWeather -> navController.navigate(NavigationDestination.LocalWeather.route)
                is NavigationDestination.SavedCities -> navController.navigate(NavigationDestination.SavedCities.route)
                is NavigationDestination.UserSettings -> {}
                is NavigationDestination.WeatherDetails -> {}
            }
        }
    }) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            Text(text = "This is the settings")
            TextField(
                value = searchText,
                onValueChange = { searchText = it },
            )
        }
    }
}
