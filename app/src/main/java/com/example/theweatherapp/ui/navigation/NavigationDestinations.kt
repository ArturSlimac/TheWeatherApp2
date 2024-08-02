package com.example.theweatherapp.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.automirrored.outlined.List
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.ui.graphics.vector.ImageVector

sealed class NavigationDestination(
    val route: String,
    val outlinedIcon: ImageVector,
    val filledIcon: ImageVector,
    val label: String,
) {
    object LocalWeather : NavigationDestination("local_weather", Icons.Outlined.LocationOn, Icons.Filled.LocationOn, "Local")

    object SavedCities : NavigationDestination("saved_cities", Icons.AutoMirrored.Outlined.List, Icons.AutoMirrored.Filled.List, "Saved")

    object UserSettings : NavigationDestination("user_settings", Icons.Outlined.Settings, Icons.Filled.Settings, "Settings")

    companion object {
        val values = listOf(LocalWeather, SavedCities, UserSettings)
    }
}
