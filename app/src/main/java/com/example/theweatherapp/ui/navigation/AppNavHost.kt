package com.example.theweatherapp.ui.navigation

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
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

/**
 * Sets up the navigation host for the app using a [NavHostController] and handles navigation transitions.
 *
 * This composable defines the navigation graph for the application, including the routes and their
 * corresponding screens. It utilizes [SharedTransitionLayout] to enable shared element transitions
 * between composable destinations.
 *
 * @param navController The [NavHostController] used to manage the navigation stack and handle
 * navigation events within the app.
 *
 * This function sets up the following destinations:
 *
 * - **LocalWeatherScreen**: Displayed when navigating to the route defined by
 * [NavigationDestination.LocalWeather.route].
 * - **UserSettingsScreen**: Displayed when navigating to the route defined by
 * [NavigationDestination.UserSettings.route].
 * - **SavedCitiesScreen**: Displayed when navigating to the route defined by
 * [NavigationDestination.SavedCities.route]. This screen receives the current [AnimatedVisibilityScope]
 * for handling shared element transitions.
 * - **DetailScreen**: Displayed when navigating to the route defined by
 * [NavigationDestination.WeatherDetails.route]. This screen is provided with a [SavedCitiesViewModel]
 * obtained from the navigation back stack entry for the `SavedCities` destination, and it also receives
 * the current [AnimatedVisibilityScope] for shared element transitions.
 *
 * **SavedCitiesScreen** and **DetailScreen** share one view model [SavedCitiesViewModel]
 *
 * The [SharedTransitionLayout] composable is used to manage the shared element transitions between
 * these destinations, enhancing the visual experience when navigating through the app.
 */
@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun AppNavHost(navController: NavHostController) {
    SharedTransitionLayout {
        NavHost(navController = navController, startDestination = NavigationDestination.LocalWeather.route) {
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
                route = NavigationDestination.WeatherDetails.route,
            ) { backStackEntry ->

                val parentEntry =
                    remember(backStackEntry) {
                        navController.getBackStackEntry(NavigationDestination.SavedCities.route)
                    }
                val parentViewModel = hiltViewModel<SavedCitiesViewModel>(parentEntry)
                DetailScreen(
                    navController = navController,
                    savedCitiesViewModel = parentViewModel,
                    animatedVisibilityScope = this,
                )
            }
        }
    }
}
