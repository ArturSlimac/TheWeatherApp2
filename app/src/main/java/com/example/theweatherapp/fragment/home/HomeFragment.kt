package com.example.theweatherapp.fragment.home

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.theweatherapp.fragment.home.screen.HomeScreen
import com.example.theweatherapp.ui.components.RequestLocationPermission
import com.example.theweatherapp.utils.Response
import com.example.theweatherapp.viewmodel.HomeViewModel

@Composable
fun HomeFragment(
    modifier: Modifier = Modifier,
    homeViewModel: HomeViewModel = hiltViewModel(),
) {
    val onPermissionGranted = { homeViewModel.fetchWeather() }
    val onPermissionDenied = { homeViewModel.onPermissionDenied() }
    val onPermissionsRevoked = { homeViewModel.onPermissionsRevoked() }

    RequestLocationPermission(
        onPermissionGranted = onPermissionGranted,
        onPermissionDenied = onPermissionDenied,
        onPermissionsRevoked = onPermissionsRevoked,
    )

    Surface(modifier = modifier.fillMaxSize()) {
        when (val weatherResponse = homeViewModel.weatherState.value) {
            is Response.Loading -> CircularProgressIndicator()
            is Response.Success ->
                HomeScreen(modifier = Modifier.padding(horizontal = 16.dp))
            is Response.Failure -> Text(text = "ERROR")
        }
    }
}
