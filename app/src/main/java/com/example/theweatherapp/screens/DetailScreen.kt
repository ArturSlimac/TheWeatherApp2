package com.example.theweatherapp.screens

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.theweatherapp.domain.mappers.toCurrentTemperatureItem
import com.example.theweatherapp.domain.mappers.toCurrentWeatherItem
import com.example.theweatherapp.domain.mappers.toWeatherForecastItems
import com.example.theweatherapp.domain.model.weather.WeatherModel
import com.example.theweatherapp.ui.components.AbsolutErrorBox
import com.example.theweatherapp.ui.components.CircularIndicator
import com.example.theweatherapp.ui.components.top_bars.TopNavBar
import com.example.theweatherapp.ui.components.weather_cards.CurrentTemperatureCard
import com.example.theweatherapp.ui.components.weather_cards.CurrentWeatherDetailsCard
import com.example.theweatherapp.ui.components.weather_cards.WeatherForecastCarousel
import com.example.theweatherapp.utils.Response
import com.example.theweatherapp.viewmodel.SavedCitiesViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.DetailScreen(
    animatedVisibilityScope: AnimatedVisibilityScope,
    navController: NavController,
    savedCitiesViewModel: SavedCitiesViewModel = hiltViewModel(),
) {
    val selectedCity by savedCitiesViewModel.selectedCity.collectAsState()
    val weatherState by savedCitiesViewModel.weatherState
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(selectedCity) {
        if (savedCitiesViewModel.shouldFetchWeather) {
            savedCitiesViewModel.fetchWeather(selectedCity!!)
        }
    }

    val key = selectedCity!!.name

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar =
            {
                TopNavBar(
                    selectedCity = selectedCity!!,
                    animatedVisibilityScope = animatedVisibilityScope,
                    animationKey = "city/${selectedCity!!.name}/$key",
                    onBackClick = { navController.popBackStack() },
                    onSaveClick = {
                        scope.launch {
                            savedCitiesViewModel.onToggleSearch()
                            savedCitiesViewModel.onSaveCity()
                            navController.popBackStack()
                        }
                    },
                    onDeleteClick = {
                        scope.launch {
                            navController.popBackStack()
                            delay(1100)
                            savedCitiesViewModel.onDeleteCity()
                        }
                    },
                )
            },
    ) { innerPadding ->
        when (weatherState) {
            is Response.Loading ->
                CircularIndicator(modifier = Modifier.padding(innerPadding))
            is Response.Success -> {
                val weatherModel = (weatherState as Response.Success<WeatherModel>).data
                Box(modifier = Modifier.padding(innerPadding)) {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        item {
                            CurrentTemperatureCard(
                                currentTemperatureItem = weatherModel!!.toCurrentTemperatureItem(),
                                animatedVisibilityScope = animatedVisibilityScope,
                                key = key,
                            )
                        }
                        item {
                            CurrentWeatherDetailsCard(weatherModel!!.toCurrentWeatherItem())
                        }
                        item {
                            WeatherForecastCarousel(weatherModel!!.toWeatherForecastItems())
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
