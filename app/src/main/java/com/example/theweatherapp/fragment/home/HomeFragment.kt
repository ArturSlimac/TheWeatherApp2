package com.example.theweatherapp.fragment.home

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
    val showSearchBar by homeViewModel.showSearchBar

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
        if (showSearchBar) {
            SearchBar(modifier = Modifier.padding(horizontal = 16.dp))
        } else {
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
                is Response.Failure -> Text(text = "ERROR: ${response.e}")
            }
        }
    }
}
