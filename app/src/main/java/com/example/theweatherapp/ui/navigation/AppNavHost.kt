package com.example.theweatherapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.theweatherapp.screens.DetailScreen
import com.example.theweatherapp.screens.LocalWeatherScreen
import com.example.theweatherapp.screens.SavedCitiesScreen
import com.example.theweatherapp.screens.UserSettingsScreen
import com.example.theweatherapp.viewmodel.SavedCitiesViewModel

@Composable
fun AppNavHost(navController: NavHostController) {
    NavHost(navController = navController, startDestination = NavigationDestination.SavedCities.route) {
        composable(NavigationDestination.LocalWeather.route) {
            LocalWeatherScreen(navController = navController)
        }
        composable(NavigationDestination.SavedCities.route) {
            SavedCitiesScreen(navController = navController)
        }
        composable(NavigationDestination.UserSettings.route) {
            UserSettingsScreen(navController = navController)
        }

        composable(NavigationDestination.WeatherDetails.route) { backStackEntry ->
            val parentEntry =
                remember(backStackEntry) {
                    navController.getBackStackEntry(NavigationDestination.SavedCities.route)
                }
            val parentViewModel = hiltViewModel<SavedCitiesViewModel>(parentEntry)
            DetailScreen(navController = navController, savedCitiesViewModel = parentViewModel)
        }
    }
}
