package com.example.theweatherapp.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
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

@Composable
fun WeatherForecastCard(
    temperature: Pair<Int, String>,
    time: Pair<String, String>,
    @DrawableRes weatherIcon: Int,
) {
    Card(modifier = Modifier.padding(end = 8.dp),
        colors =
            CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainerHighest,
            ),

    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(text = time.first,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.tertiary,)
            Icon(painterResource(weatherIcon), contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.height(124.dp))
            Text(text = "${temperature.first}°",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onSurface,

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
