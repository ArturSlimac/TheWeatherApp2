package com.example.theweatherapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.theweatherapp.fragment.saved_cities.SavedCitiesFragment
import com.example.theweatherapp.fragment.settings.SettingsFragment

@Composable
fun AppNavHost(navController: NavHostController) {
    NavHost(navController = navController, startDestination = NavigationDestination.SavedCities.route) {
        composable(NavigationDestination.LocalWeather.route) {
        }
        composable(NavigationDestination.SavedCities.route) {
            SavedCitiesFragment()
        }
        composable(NavigationDestination.UserSettings.route) {
            SettingsFragment()
        }
    }
}
