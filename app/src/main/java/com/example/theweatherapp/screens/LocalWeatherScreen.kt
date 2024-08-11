package com.example.theweatherapp.screens

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.theweatherapp.domain.model.weather.WeatherModel
import com.example.theweatherapp.ui.components.BottomNavigationBar
import com.example.theweatherapp.ui.navigation.NavigationDestination
import com.example.theweatherapp.utils.Response
import com.example.theweatherapp.viewmodel.LocalWeatherViewModel

@Composable
fun LocalWeatherScreen(
    navController: NavController,
    localWeatherViewModel: LocalWeatherViewModel = hiltViewModel(),
) {
    val weatherState by localWeatherViewModel.weatherState
    val context = LocalContext.current

    val locationPermissionLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission(),
            onResult = { isGranted: Boolean ->
                if (isGranted) {
                    localWeatherViewModel.fetchWeather()
                } else {
                    localWeatherViewModel.onPermissionDenied()
                }
            },
        )

    LaunchedEffect(Unit) {
        when (
            PackageManager.PERMISSION_GRANTED ==
                ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)
        ) {
            true -> localWeatherViewModel.fetchWeather()
            false -> locationPermissionLauncher.launch(Manifest.permission.ACCESS_COARSE_LOCATION)
        }
    }

    Scaffold(bottomBar = {
        BottomNavigationBar(currentDestination = NavigationDestination.LocalWeather) {
            when (it) {
                is NavigationDestination.LocalWeather -> {}
                is NavigationDestination.SavedCities -> navController.navigate(NavigationDestination.SavedCities.route)
                is NavigationDestination.UserSettings -> navController.navigate(NavigationDestination.UserSettings.route)
                is NavigationDestination.WeatherDetails -> {}
            }
        }
    }) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            when (weatherState) {
                is Response.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                is Response.Success -> {
                    val weather = (weatherState as Response.Success<WeatherModel>).data
                    weather?.let {
                        Text(text = "City: ${it.city?.name}°", modifier = Modifier.align(Alignment.Center))
                        Text(text = "Temp: ${it.current?.temperature_2m}°", modifier = Modifier.align(Alignment.Center))
                    }
                }
                is Response.Failure -> {
                    val errorMessage = (weatherState as Response.Failure).e?.message ?: "Permission Denied"
                    Text(text = errorMessage, color = MaterialTheme.colorScheme.onError, modifier = Modifier.align(Alignment.Center))
                }
            }
        }
    }
}
