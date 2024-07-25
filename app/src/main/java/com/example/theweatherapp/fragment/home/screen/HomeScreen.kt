package com.example.theweatherapp.fragment.home.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.theweatherapp.domain.model.weather.WeatherModel

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    weatherModel: WeatherModel,
) {
    Column(modifier = modifier) {
        Text(text = "City: ${weatherModel.city}")
        Text(text = "Temperature: ${weatherModel.current?.temperature_2m}")
        // Display other weather details
    }
}
