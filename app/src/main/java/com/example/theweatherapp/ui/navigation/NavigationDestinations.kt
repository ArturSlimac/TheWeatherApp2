package com.example.theweatherapp.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.automirrored.outlined.List
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Done
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import com.example.theweatherapp.ui.navigation.NavigationDestination.LocalWeather.route

sealed class NavigationDestination(
    val route: String,
    val outlinedIcon: ImageVector,
    val filledIcon: ImageVector,
    val label: String,
    val navigate: (NavController) -> Unit,
) {
    data object LocalWeather : NavigationDestination(
        route = "local_weather",
        Icons.Outlined.LocationOn,
        Icons.Filled.LocationOn,
        "Local",
        navigate = { navController ->
            navController.navigate("local_weather") {
            }
        },
    )

    data object SavedCities : NavigationDestination(
        "saved_cities",
        Icons.AutoMirrored.Outlined.List,
        Icons.AutoMirrored.Filled.List,
        "Saved",
        navigate = { navController ->
            navController.navigate("saved_cities") {
            }
        },
    )

    data object UserSettings : NavigationDestination(
        "user_settings",
        Icons.Outlined.Settings,
        Icons.Filled.Settings,
        "Settings",
        navigate = { navController ->
            navController.navigate("user_settings") {
            }
        },
    )

    data object WeatherDetails : NavigationDestination(
        "weather_detail",
        Icons.Outlined.Done,
        Icons.Filled.Done,
        "",
        {},
    )
}
