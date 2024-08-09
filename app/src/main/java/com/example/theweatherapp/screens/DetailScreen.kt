package com.example.theweatherapp.screens

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import com.example.theweatherapp.ui.components.CurrentTemperatureCard
import com.example.theweatherapp.ui.components.CurrentWeatherDetailsCard
import com.example.theweatherapp.ui.components.WeatherForecastCarousel
import com.example.theweatherapp.utils.Response
import com.example.theweatherapp.viewmodel.SavedCitiesViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.DetailScreen(
    navController: NavController,
    savedCitiesViewModel: SavedCitiesViewModel = hiltViewModel(),
) {
    val selectedCity by savedCitiesViewModel.selectedCity.collectAsState()
    val weatherState by savedCitiesViewModel.weatherState

    LaunchedEffect(selectedCity) {
        if (savedCitiesViewModel.shouldFetchWeather) {
            savedCitiesViewModel.fetchWeather(selectedCity!!)
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
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
                        text = selectedCity!!.name!!,
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                        textAlign = TextAlign.Center,
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
                            savedCitiesViewModel.onDeleteCity()
                            navController.popBackStack()
                        }) {
                            Icon(Icons.Outlined.Delete, contentDescription = "Deletes current city from the list")
                        }
                    } else {
                        IconButton(onClick = {
                            savedCitiesViewModel.onSaveCity()
                            savedCitiesViewModel.onToggleSearch()
                            navController.popBackStack()
                        }) {
                            Icon(Icons.Outlined.Add, contentDescription = "Saves current city in the application")
                        }
                    }
                },
            )
        },
    ) { innerPadding ->
        when (weatherState) {
            is Response.Success -> {
                val weatherModel = (weatherState as Response.Success<WeatherModel>).data
                Box(modifier = Modifier.padding(innerPadding)) {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        item {
                            CurrentTemperatureCard(weatherModel!!.toCurrentTemperatureItem())
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

            else -> {}
        }
    }
}
