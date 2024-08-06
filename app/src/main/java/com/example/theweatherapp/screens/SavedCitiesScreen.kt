package com.example.theweatherapp.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.theweatherapp.ui.components.BottomNavigationBar
import com.example.theweatherapp.ui.components.TopSearchBar
import com.example.theweatherapp.ui.navigation.NavigationDestination
import com.example.theweatherapp.viewmodel.SavedCitiesViewModel

@Composable
fun SavedCitiesScreen(
    savedCitiesViewModel: SavedCitiesViewModel = hiltViewModel(),
    navController: NavHostController,
) {
    val foundCitiesState by savedCitiesViewModel.foundCitiesState
    val isSearchingState by savedCitiesViewModel.isSearching
    val searchText by savedCitiesViewModel.searchText

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopSearchBar(
                searchText = searchText,
                onSearchTextChange = savedCitiesViewModel::onSearchTextChange,
                isSearching = isSearchingState,
                onToggleSearch = savedCitiesViewModel::onToggleSearch,
                foundCitiesState = foundCitiesState,
                onCitySelected = {
                    savedCitiesViewModel.onCitySelected(it)
                    navController.navigate(NavigationDestination.WeatherDetails.route)
                },
            )
        },
        bottomBar = {
            BottomNavigationBar(currentDestination = NavigationDestination.SavedCities) {
                when (it) {
                    is NavigationDestination.LocalWeather -> navController.navigate(NavigationDestination.LocalWeather.route)
                    is NavigationDestination.SavedCities -> {}
                    is NavigationDestination.UserSettings -> navController.navigate(NavigationDestination.UserSettings.route)
                    is NavigationDestination.WeatherDetails -> {}
                }
            }
        },
    ) { innerPadding ->
        Box(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
        ) {
            Text("This is the future list of saved cities")
        }
    }
}
