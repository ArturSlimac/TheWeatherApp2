package com.example.theweatherapp.ui.components.weather_cards

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.theweatherapp.domain.model.helpers.CurrentWeatherItem
import com.example.theweatherapp.domain.model.helpers.WeatherType
import com.example.theweatherapp.ui.theme.TheWeatherAppTheme

/**
 * A composable function that displays detailed current weather information in a card format.
 * The card includes weather icon, humidity, pressure, and wind speed, arranged in a structured layout.
 *
 * @param currentWeatherItem The data model [CurrentWeatherItem] containing the current weather details, such as humidity, pressure, wind speed, and weather type.
 */
@Composable
fun CurrentWeatherDetailsCard(currentWeatherItem: CurrentWeatherItem) {
    Card(
        modifier =
            Modifier
                .fillMaxWidth()
                .aspectRatio(2f)
                .padding(horizontal = 16.dp)
                .padding(bottom = 16.dp),
        colors =
            CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainerHighest,
            ),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Column(
                modifier = Modifier.weight(1f).padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Icon(
                    painter =
                        if (currentWeatherItem.isDay) {
                            painterResource(currentWeatherItem.weatherType.dayWeatherIcon)
                        } else {
                            painterResource(currentWeatherItem.weatherType.nightWeatherIcon)
                        },
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    // modifier = Modifier.width(100.dp)
                )
                Text(
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center,
                    text =
                        stringResource(
                            currentWeatherItem.weatherType.weatherTitle,
                        ),
                )
            }

            Column(
                modifier = Modifier.weight(2f).fillMaxHeight().padding(8.dp),
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.End,
            ) {
                Row(verticalAlignment = Alignment.Bottom) {
                    Text(
                        text = "humidity: ",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.tertiary,
                    )
                    Text(
                        text = "${currentWeatherItem.humidity.first} ${currentWeatherItem.humidity.second}",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                }
                Row(verticalAlignment = Alignment.Bottom) {
                    Text(
                        text = "pressure: ",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.tertiary,
                    )
                    Text(
                        text = "${currentWeatherItem.pressure.first} ${currentWeatherItem.pressure.second}",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                }
                Row(verticalAlignment = Alignment.Bottom) {
                    Text(
                        text = "wind speed: ",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.tertiary,
                    )
                    Text(
                        text = "${currentWeatherItem.windSpeed.first} ${currentWeatherItem.windSpeed.second}",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                }
            }
        }
    }
}

@Composable
@Preview
fun CurrentWeatherDetailsCardPreview() {
    TheWeatherAppTheme {
        CurrentWeatherDetailsCard(
            CurrentWeatherItem(
                pressure = Pair(555.0, "ddd"),
                humidity = Pair(44.0, "%"),
                windSpeed = Pair(5.0, "m/s"),
                weatherType = WeatherType.fromWmoStandard(3),
                isDay = false,
            ),
        )
    }
}
