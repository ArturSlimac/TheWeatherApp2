package com.example.theweatherapp.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.theweatherapp.ui.components.BottomNavigationBar
import com.example.theweatherapp.ui.navigation.NavigationDestination
import com.example.theweatherapp.viewmodel.SettingsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserSettingsScreen(
    navController: NavController,
    settingsViewModel: SettingsViewModel = hiltViewModel(),
) {
    val windSpeedUnit by settingsViewModel.currentWindSpeedUnit.collectAsState()
    val temperatureUnit by settingsViewModel.currentTemperatureUnit.collectAsState()
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                modifier = Modifier.fillMaxWidth(),
                scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(),
                colors =
                    TopAppBarDefaults.centerAlignedTopAppBarColors(
                        titleContentColor = MaterialTheme.colorScheme.onSurface,
                        containerColor = MaterialTheme.colorScheme.surface,
                        scrolledContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                        navigationIconContentColor = MaterialTheme.colorScheme.onSurface,
                        actionIconContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    ),
                title = {
                    Text(
                        text = "Settings",
                        style = MaterialTheme.typography.titleLarge,
                    )
                },
            )
        },
        bottomBar = {
            BottomNavigationBar(currentDestination = NavigationDestination.UserSettings) {
                when (it) {
                    is NavigationDestination.LocalWeather -> navController.navigate(NavigationDestination.LocalWeather.route)
                    is NavigationDestination.SavedCities -> navController.navigate(NavigationDestination.SavedCities.route)
                    is NavigationDestination.UserSettings -> {}
                    is NavigationDestination.WeatherDetails -> {}
                }
            }
        },
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            Column(modifier = Modifier.padding(16.dp)) {
                // Wind Speed Unit Setting
                Text("Wind Speed Unit")
                Row {
                    settingsViewModel.windSpeedUnits.forEach { unit ->
                        RadioButton(
                            selected = (unit == windSpeedUnit),
                            onClick = { settingsViewModel.changeWindSpeedUnit(unit) },
                        )
                        Text(unit.unit, modifier = Modifier.padding(start = 8.dp))
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Temperature Unit Setting
                Text("Temperature Unit")
                Row {
                    settingsViewModel.temperatureUnits.forEach { unit ->
                        RadioButton(
                            selected = (unit == temperatureUnit),
                            onClick = { settingsViewModel.changeTemperatureUnit(unit) },
                        )
                        Text(unit.unit, modifier = Modifier.padding(start = 8.dp))
                    }
                }
            }
        }
    }
}
