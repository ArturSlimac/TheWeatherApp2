package com.example.theweatherapp.ui.components.weather_cards

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.theweatherapp.R
import com.example.theweatherapp.ui.theme.TheWeatherAppTheme

/**
 * Composable function that displays a weather forecast card, showing the temperature, time, and an icon representing the weather condition.
 *
 * @param temperature A [Pair] representing the temperature, where the first value is the temperature in degrees and the second value is the unit (e.g., "°C" or "°F").
 * @param time A [Pair] representing the time of the forecast, where the first value is the formatted time (e.g., "12:00 PM") and the second value is the time zone or any additional time-related information.
 * @param weatherIcon The resource ID [DrawableRes] of the drawable representing the weather condition (e.g., sunny, cloudy, rainy).
 *
 * This composable displays a card with three main pieces of information: the forecasted time, the weather icon, and the temperature.
 *
 */
@Composable
fun WeatherForecastCard(
    temperature: Pair<Int, String>,
    time: Pair<String, String>,
    @DrawableRes weatherIcon: Int,
) {
    Card(
        modifier = Modifier.padding(end = 8.dp),
        colors =
            CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainerHighest,
            ),
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = time.first,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.tertiary,
                modifier = Modifier.weight(2f),
            )
            Icon(
                painterResource(weatherIcon),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.weight(5f),
            )
            Text(
                text = "${temperature.first}°",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.weight(2f),
            )
        }
    }
}

@Composable
@Preview
fun WeatherForecastCardPreview() {
    TheWeatherAppTheme {
        WeatherForecastCard(Pair(16, "C"), Pair("12:30", ""), R.drawable.ic_rainy)
    }
}
