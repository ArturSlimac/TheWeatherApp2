package com.example.theweatherapp.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.theweatherapp.ui.components.BottomNavigationBar
import com.example.theweatherapp.ui.navigation.NavigationDestination

@Composable
fun LocalWeatherScreen(navController: NavController) {
    Scaffold(bottomBar = {
        BottomNavigationBar(currentDestination = NavigationDestination.LocalWeather) {
            when (it) {
                is NavigationDestination.LocalWeather -> {}
                is NavigationDestination.SavedCities -> navController.navigate(NavigationDestination.SavedCities.route)
                is NavigationDestination.UserSettings -> navController.navigate(NavigationDestination.UserSettings.route)
                is NavigationDestination.WeatherDetails -> {}
            }
        }
    }) { innerPadding ->
        Text(modifier = Modifier.padding(innerPadding), text = "This is the local current weather")
    }
}
