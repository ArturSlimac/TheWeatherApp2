package com.example.theweatherapp.fragment.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.theweatherapp.fragment.home.screen.HomeScreen
import com.example.theweatherapp.ui.components.RequestLocationPermission
import com.example.theweatherapp.ui.components.SearchBar
import com.example.theweatherapp.utils.Response
import com.example.theweatherapp.viewmodel.HomeViewModel

@Composable
fun HomeFragment(
    modifier: Modifier = Modifier,
    homeViewModel: HomeViewModel = hiltViewModel(),
) {
    val weatherState by homeViewModel.weatherState
    val permissionGranted by homeViewModel.permissionGranted
    val searchedCity by homeViewModel.searchText
    val foundCitiesState by homeViewModel.foundCitiesState

    val context = LocalContext.current

    LaunchedEffect(Unit) {
        homeViewModel.checkLocationPermission(context)
        if (permissionGranted) {
            homeViewModel.fetchWeather()
        }
    }
    val onPermissionGranted = { homeViewModel.onPermissionGranted() }
    val onPermissionDenied = { homeViewModel.onPermissionDenied() }
    val onPermissionsRevoked = { homeViewModel.onPermissionsRevoked() }

    RequestLocationPermission(
        onPermissionGranted = onPermissionGranted,
        onPermissionDenied = onPermissionDenied,
        onPermissionsRevoked = onPermissionsRevoked,
    )

    Surface(modifier = modifier.fillMaxSize()) {
        Column {
            SearchBar(
                searchText = searchedCity,
                onSearchTextChange = { homeViewModel.onSearchTextChange(it) },
            )

            when (val citiesResponse = foundCitiesState) {
                is Response.Loading -> CircularProgressIndicator()
                is Response.Success -> {
                    val cities = citiesResponse.data
                    if (!cities.isNullOrEmpty()) {
                        LazyColumn {
                            items(cities) { city ->
                                Text(
                                    text = "${city.name!!}, ${city.country}",
                                    modifier =
                                        Modifier
                                            .fillMaxWidth()
                                            .clickable {
                                                // homeViewModel.searchWeatherByCity(city)
                                            }.padding(8.dp),
                                )
                            }
                        }
                    } else {
                        Text("No cities found")
                    }
                }
                is Response.Failure -> Text(text = "ERROR: ${citiesResponse.e?.message}")
            }

            when (val response = weatherState) {
                is Response.Loading -> CircularProgressIndicator()
                is Response.Success -> {
                    val weatherModel = response.data
                    if (weatherModel != null) {
                        HomeScreen(modifier = Modifier.padding(horizontal = 16.dp), weatherModel)
                    } else {
                        Text("No weather data available")
                    }
                }
                is Response.Failure -> Text(text = "ERROR: ${response.e?.message}")
            }
        }
    }
}
