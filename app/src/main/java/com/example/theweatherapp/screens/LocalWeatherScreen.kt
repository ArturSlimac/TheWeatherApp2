package com.example.theweatherapp.screens

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.navigation.NavController
import com.example.theweatherapp.domain.mappers.toCurrentTemperatureItem
import com.example.theweatherapp.domain.mappers.toCurrentWeatherItem
import com.example.theweatherapp.domain.mappers.toWeatherForecastItems
import com.example.theweatherapp.domain.model.weather.WeatherModel
import com.example.theweatherapp.ui.components.AbsolutErrorBox
import com.example.theweatherapp.ui.components.BottomNavigationBar
import com.example.theweatherapp.ui.components.CircularIndicator
import com.example.theweatherapp.ui.components.top_bars.TopBar
import com.example.theweatherapp.ui.components.weather_cards.CurrentTemperatureCard
import com.example.theweatherapp.ui.components.weather_cards.CurrentWeatherDetailsCard
import com.example.theweatherapp.ui.components.weather_cards.WeatherForecastCarousel
import com.example.theweatherapp.ui.navigation.NavigationDestination
import com.example.theweatherapp.utils.Response
import com.example.theweatherapp.viewmodel.LocalWeatherViewModel

@OptIn(ExperimentalSharedTransitionApi::class, ExperimentalMaterial3Api::class)
@Composable
fun SharedTransitionScope.LocalWeatherScreen(
    navController: NavController,
    localWeatherViewModel: LocalWeatherViewModel = hiltViewModel(),
) {
    val weatherState by localWeatherViewModel.weatherState
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }
    val cityName = remember { mutableStateOf("") }

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

    LifecycleResumeEffect(Unit) {
        when (
            PackageManager.PERMISSION_GRANTED ==
                ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)
        ) {
            true -> localWeatherViewModel.fetchWeather()
            false -> locationPermissionLauncher.launch(Manifest.permission.ACCESS_COARSE_LOCATION)
        }

        onPauseOrDispose {
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        bottomBar = {
            BottomNavigationBar(currentDestination = NavigationDestination.LocalWeather, navController = navController)
        },
        topBar = {
            if (weatherState is Response.Success) {
                TopBar(cityName.value)
            }
        },
    ) { innerPadding ->
        when (weatherState) {
            is Response.Loading -> {
                CircularIndicator(modifier = Modifier.padding(innerPadding))
            }
            is Response.Success -> {
                Box(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
                    val weather = (weatherState as Response.Success<WeatherModel>).data
                    weather?.let {
                        cityName.value = weather.city?.name ?: ""

                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            item {
                                CurrentTemperatureCard(
                                    currentTemperatureItem = weather.toCurrentTemperatureItem(),
                                )
                            }
                            item {
                                CurrentWeatherDetailsCard(weather.toCurrentWeatherItem())
                            }
                            item {
                                WeatherForecastCarousel(weather.toWeatherForecastItems())
                            }
                        }
                    }
                }
            }
            is Response.Failure -> {
                val errorMessage = (weatherState as Response.Failure).e?.message ?: "Something went wrong. Please try again later"
                AbsolutErrorBox(modifier = Modifier.padding(innerPadding), snackbarHostState, errorMessage)
            }
        }
    }
}
