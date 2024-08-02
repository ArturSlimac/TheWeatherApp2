package com.example.theweatherapp.ui.components

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.example.theweatherapp.ui.navigation.NavigationDestination

@Composable
fun BottomNavigationBar(
    currentDestination: NavigationDestination,
    onNavigate: (NavigationDestination) -> Unit,
) {
    NavigationBar {
        NavigationDestination.values.forEach { destination ->
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
                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                        style = MaterialTheme.typography.bodyMedium,
                    )
                },
                selected = currentDestination == destination,
                onClick = { onNavigate(destination) },
            )
        }
    }
}
