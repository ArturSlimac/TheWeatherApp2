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

/**
 * Sealed class representing different navigation destinations within the application.
 *
 * Each object in this sealed class defines a specific screen or destination in the app's navigation
 * graph, including its route, icons, label, and navigation action.
 *
 * @property route The unique string route for the destination used for navigation.
 * @property outlinedIcon The [ImageVector] representing the outlined icon for this destination.
 * @property filledIcon The [ImageVector] representing the filled icon for this destination.
 * @property label The label displayed for the destination in the bottom navigation bar.
 * @property navigate A lambda function that performs the navigation action to this destination using
 * a [NavController].
 */
sealed class NavigationDestination(
    val route: String,
    val outlinedIcon: ImageVector,
    val filledIcon: ImageVector,
    val label: String,
    val navigate: (NavController) -> Unit,
) {
    /**
     * Navigation destination for the local weather screen.
     *
     * Displays current weather information based on the user's location.
     */
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

    /**
     * Navigation destination for the saved cities screen.
     *
     * Allows users to view and manage a list of saved cities.
     */
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

    /**
     * Navigation destination for the user settings screen.
     *
     * Provides options for users to customize their application settings.
     */
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

    /**
     * Navigation destination for the weather details screen.
     *
     * Shows detailed weather information for a selected city.
     */
    data object WeatherDetails : NavigationDestination(
        "weather_detail",
        Icons.Outlined.Done,
        Icons.Filled.Done,
        "",
        {},
    )
}
