package com.example.theweatherapp.ui.components

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.theweatherapp.ui.navigation.NavigationDestination
import com.example.theweatherapp.ui.navigation.NavigationDestination.LocalWeather
import com.example.theweatherapp.ui.navigation.NavigationDestination.SavedCities
import com.example.theweatherapp.ui.navigation.NavigationDestination.UserSettings

@Composable
fun BottomNavigationBar(
    currentDestination: NavigationDestination,
    onNavigate: (NavigationDestination) -> Unit,
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
                onClick = { onNavigate(destination) },
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
