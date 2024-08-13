package com.example.theweatherapp.screens

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.theweatherapp.domain.mappers.toCurrentTemperatureItem
import com.example.theweatherapp.domain.mappers.toCurrentWeatherItem
import com.example.theweatherapp.domain.mappers.toWeatherForecastItems
import com.example.theweatherapp.domain.model.weather.WeatherModel
import com.example.theweatherapp.ui.components.AbsolutErrorBox
import com.example.theweatherapp.ui.components.CircularIndicator
import com.example.theweatherapp.ui.components.weather_cards.CurrentTemperatureCard
import com.example.theweatherapp.ui.components.weather_cards.CurrentWeatherDetailsCard
import com.example.theweatherapp.ui.components.weather_cards.WeatherForecastCarousel
import com.example.theweatherapp.utils.Response
import com.example.theweatherapp.viewmodel.SavedCitiesViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.DetailScreen(
    animatedVisibilityScope: AnimatedVisibilityScope,
    navController: NavController,
    savedCitiesViewModel: SavedCitiesViewModel = hiltViewModel(),
) {
    val selectedCity by savedCitiesViewModel.selectedCity.collectAsState()
    val weatherState by savedCitiesViewModel.weatherState
    val scope = rememberCoroutineScope()
    val isScrolledToEnd = remember { mutableStateOf(false) }
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
        topBar = {
            CenterAlignedTopAppBar(
                modifier = Modifier.fillMaxWidth(),
                scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(),
                colors =
                    TopAppBarColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer,
                        scrolledContainerColor = Color.Transparent,
                        navigationIconContentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                        titleContentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                        actionIconContentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                    ),
                title = {
                    Text(
                        text = selectedCity!!.name,
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                        textAlign = TextAlign.Center,
                        modifier =
                            Modifier.sharedElement(
                                state = rememberSharedContentState(key = "city/${selectedCity!!.name}/$key"),
                                animatedVisibilityScope = animatedVisibilityScope,
                                boundsTransform = { _, _ -> tween(durationMillis = 1000) },
                            ),
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Outlined.ArrowBack, contentDescription = "Returns to the searching")
                    }
                },
                actions = {
                    if (selectedCity!!.isSaved) {
                        IconButton(onClick = {
                            scope.launch {
                                navController.popBackStack()
                                delay(1100)

                                savedCitiesViewModel.onDeleteCity()
                            }
                        }) {
                            Icon(Icons.Outlined.Delete, contentDescription = "Deletes current city from the list")
                        }
                    } else {
                        IconButton(onClick = {
                            scope.launch {
                                isScrolledToEnd.value = true
                                savedCitiesViewModel.onToggleSearch()
                                savedCitiesViewModel.onSaveCity()
                                navController.popBackStack()
                            }
                        }) {
                            Icon(Icons.Outlined.Add, contentDescription = "Saves current city in the application")
                        }
                    }
                },
            )
        },
    ) { innerPadding ->
        when (weatherState) {
            is Response.Loading ->
                CircularIndicator()
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
                AbsolutErrorBox(snackbarHostState, errorMessage)
            }
        }
    }
}
