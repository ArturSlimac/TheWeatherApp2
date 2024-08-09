package com.example.theweatherapp.ui.navigation

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.theweatherapp.screens.DetailScreen
import com.example.theweatherapp.screens.LocalWeatherScreen
import com.example.theweatherapp.screens.SavedCitiesScreen
import com.example.theweatherapp.screens.UserSettingsScreen
import com.example.theweatherapp.viewmodel.SavedCitiesViewModel

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun AppNavHost(navController: NavHostController) {
    SharedTransitionLayout {
        NavHost(navController = navController, startDestination = NavigationDestination.SavedCities.route) {
            composable(NavigationDestination.LocalWeather.route) {
                LocalWeatherScreen(navController = navController)
            }
            composable(NavigationDestination.UserSettings.route) {
                UserSettingsScreen(navController = navController)
            }

            composable(NavigationDestination.SavedCities.route) {
                SavedCitiesScreen(
                    navController = navController,
                    animatedVisibilityScope = this,
                )
            }

            composable(
                route = "${NavigationDestination.WeatherDetails.route}/{key}",
                arguments =
                    listOf(
                        navArgument("key") {
                            type = NavType.IntType
                        },
                    ),
            ) { backStackEntry ->

                val parentEntry =
                    remember(backStackEntry) {
                        navController.getBackStackEntry(NavigationDestination.SavedCities.route)
                    }
                val parentViewModel = hiltViewModel<SavedCitiesViewModel>(parentEntry)
                val key = backStackEntry.arguments?.getInt("key") ?: 0
                DetailScreen(
                    navController = navController,
                    savedCitiesViewModel = parentViewModel,
                    animatedVisibilityScope = this,
                    key = key,
                )
            }
        }
    }
}
