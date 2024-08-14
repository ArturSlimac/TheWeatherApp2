package com.example.theweatherapp.ui.components

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.theweatherapp.ui.navigation.NavigationDestination
import com.example.theweatherapp.ui.navigation.NavigationDestination.LocalWeather
import com.example.theweatherapp.ui.navigation.NavigationDestination.SavedCities
import com.example.theweatherapp.ui.navigation.NavigationDestination.UserSettings
import com.example.theweatherapp.ui.theme.TheWeatherAppTheme

/**
 * A composable function that displays a bottom navigation bar with icons and labels for different navigation destinations.
 * The navigation bar allows users to switch between primary screens of the app.
 *
 * @param currentDestination The current [NavigationDestination] representing the currently active screen.
 * This is used to highlight the selected item in the navigation bar.
 * @param navController The [NavController] used to navigate between different destinations.
 * It facilitates the navigation actions when a user selects an item in the navigation bar.
 *
 * This function creates a bottom navigation bar with a list of predefined destinations. It updates the icon and label
 * of each item based on whether it is currently selected or not. The bar uses a transparent background and custom colors
 * for selected and unselected states.
 */
@Composable
fun BottomNavigationBar(
    currentDestination: NavigationDestination,
    navController: NavController,
) {
    NavigationBar(
        containerColor = Color.Transparent,
    ) {
        val destinations = listOf(LocalWeather, SavedCities, UserSettings)

        destinations.forEach { destination ->
            NavigationBarItem(
                icon = {
                    val icon =
                        if (currentDestination == destination) {
                            destination.filledIcon
                        } else {
                            destination.outlinedIcon
                        }
                    Icon(icon, contentDescription = destination.label)
                },
                label = {
                    Text(
                        text = destination.label,
                        style = MaterialTheme.typography.labelMedium,
                    )
                },
                selected = currentDestination == destination,
                onClick = { destination.navigate(navController) },
                colors =
                    NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.onSecondaryContainer,
                        selectedTextColor = MaterialTheme.colorScheme.onSurface,
                        unselectedIconColor = MaterialTheme.colorScheme.onSurface,
                        unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        indicatorColor = MaterialTheme.colorScheme.secondaryContainer,
                    ),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BottomNavigationBarPreview() {
    val navController = rememberNavController()
    val currentDestination = LocalWeather
    TheWeatherAppTheme {
        BottomNavigationBar(
            currentDestination = currentDestination,
            navController = navController,
        )
    }
}
