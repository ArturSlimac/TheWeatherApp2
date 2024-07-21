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
import com.example.theweatherapp.utils.Const
import com.example.theweatherapp.utils.Response
import com.example.theweatherapp.viewmodel.HomeViewModel

@Composable
fun HomeFragment(
    modifier: Modifier = Modifier,
    homeViewModel: HomeViewModel = hiltViewModel(),
) {
    fun launch() {
        homeViewModel.getWeather(
            latitude = 14.25,
            longitude = 12.12,
            windSpeedUnit = Const.WindSpeedUnit.MS,
            timezone = "Europe/London",
            temperatureUnit = Const.TemperatureUnit.C,
        )
    }

    launch()

    Surface(modifier = modifier.fillMaxSize()) {
        when (val weatherResponse = homeViewModel.weatherState.value) {
            is Response.Loading -> CircularProgressIndicator()
            is Response.Success ->
                HomeScreen(modifier = Modifier.padding(horizontal = 16.dp))
            is Response.Failure -> Text(text = "ERROR")
        }
    }
}
