package com.example.theweatherapp.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.theweatherapp.domain.model.helpers.WeatherType
import com.example.theweatherapp.ui.theme.TheWeatherAppTheme

@Composable
fun CurrentWeatherDetailsCard(
    pressure: Pair<Double, String>,
    humidity: Pair<Double, String>,
    windSpeed: Pair<Double, String>,
    weatherType: WeatherType,
) {
    Card(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(16.dp),
        colors =
            CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
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
                Icon(painterResource(weatherType.dayWeatherIcon),
                    contentDescription = null)
                Text(
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,

                    text = stringResource(weatherType.weatherTitle,

                        ),
                )
            }

            Column(
                modifier = Modifier.weight(2f).padding(8.dp).fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceBetween,
            ) {
                Row (verticalAlignment = Alignment.Bottom){
                    Text(
                        text = "humidity: ",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.tertiary,
                    )
                    Text(
                        text = "${humidity.first} ${humidity.second}",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                    )
                }
                Row (verticalAlignment = Alignment.Bottom){
                    Text(
                        text = "pressure: ",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.tertiary,
                    )
                    Text(
                        text = "${pressure.first} ${pressure.second}",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                    )
                }
                Row (verticalAlignment = Alignment.Bottom){
                    Text(
                        text = "wind speed: ",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.tertiary,
                    )
                    Text(
                        text = "${windSpeed.first} ${windSpeed.second}",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
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
            Pair(555.0, "ddd"),
            Pair(44.0, "%"),
            Pair(5.0, "m/s"),
            WeatherType.fromWmoStandard(4),
        )
    }
}
