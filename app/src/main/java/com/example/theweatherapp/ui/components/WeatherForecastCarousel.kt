package com.example.theweatherapp.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.theweatherapp.R
import com.example.theweatherapp.domain.model.helpers.WeatherForecastItem
import com.example.theweatherapp.ui.theme.TheWeatherAppTheme

@Composable
fun WeatherForecastCarousel(items: List<WeatherForecastItem>) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
        colors =
            CardDefaults.cardColors(
                containerColor = Color.Transparent,
            ),
    ) {
        LazyRow() {
            items(items) { item ->
                WeatherForecastCard(
                    temperature = item.temperature,
                    time = item.time,
                    weatherIcon = item.weatherIcon,
                )
            }
        }
    }
}

@Composable
@Preview
fun WeatherForecastCarouselPreview() {
    TheWeatherAppTheme {
        WeatherForecastCarousel(
            listOf(
                WeatherForecastItem(
                    temperature = Pair(25, "C"),
                    time = Pair("14:00", "UTC"),
                    weatherIcon = R.drawable.ic_rainy,
                ),
                WeatherForecastItem(
                    temperature = Pair(20, "C"),
                    time = Pair("15:00", "UTC"),
                    weatherIcon = R.drawable.ic_cloudy_day,
                ),
                WeatherForecastItem(
                    temperature = Pair(18, "C"),
                    time = Pair("16:00", "UTC"),
                    weatherIcon = R.drawable.ic_thunder,
                ),
            ),
        )
    }
}
