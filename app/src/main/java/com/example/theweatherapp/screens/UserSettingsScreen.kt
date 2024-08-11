package com.example.theweatherapp.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.theweatherapp.ui.components.BottomNavigationBar
import com.example.theweatherapp.ui.components.top_bars.TopBar
import com.example.theweatherapp.ui.navigation.NavigationDestination
import com.example.theweatherapp.viewmodel.SettingsViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserSettingsScreen(
    navController: NavController,
    settingsViewModel: SettingsViewModel = hiltViewModel(),
) {
    val windSpeedUnit by settingsViewModel.currentWindSpeedUnit.collectAsState()
    val temperatureUnit by settingsViewModel.currentTemperatureUnit.collectAsState()

    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            TopBar("Settings")
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
                Text("Wind Speed Unit", style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.primary)
                Column {
                    settingsViewModel.windSpeedUnits.forEach { unit ->
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            RadioButton(
                                selected = (unit == windSpeedUnit),
                                onClick = {
                                    settingsViewModel.changeWindSpeedUnit(unit)
                                    scope.launch {
                                        snackbarHostState
                                            .showSnackbar("All changes are saved")
                                    }
                                },
                                colors =
                                    RadioButtonDefaults.colors(
                                        selectedColor = MaterialTheme.colorScheme.primary,
                                        unselectedColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                    ),
                            )
                            Text(
                                unit.unit,
                                modifier = Modifier.padding(start = 8.dp),
                                style = MaterialTheme.typography.labelLarge,
                                color = MaterialTheme.colorScheme.onSurface,
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Temperature Unit Setting
                Text("Temperature Unit", style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.primary)
                Column {
                    settingsViewModel.temperatureUnits.forEach { unit ->
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            RadioButton(
                                selected = (unit == temperatureUnit),
                                onClick = {
                                    settingsViewModel.changeTemperatureUnit(unit)
                                    scope.launch {
                                        snackbarHostState
                                            .showSnackbar("All changes are saved")
                                    }
                                },
                                colors =
                                    RadioButtonDefaults.colors(
                                        selectedColor = MaterialTheme.colorScheme.primary,
                                        unselectedColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                    ),
                            )
                            Text(
                                unit.unit,
                                modifier = Modifier.padding(start = 8.dp),
                                style = MaterialTheme.typography.labelLarge,
                                color = MaterialTheme.colorScheme.onSurface,
                            )
                        }
                    }
                }
            }
        }
    }
}
