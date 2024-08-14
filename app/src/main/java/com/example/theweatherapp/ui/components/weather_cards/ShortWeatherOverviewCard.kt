package com.example.theweatherapp.ui.components.weather_cards

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.theweatherapp.domain.model.helpers.ShortWeatherOverview
import com.example.theweatherapp.ui.theme.TheWeatherAppTheme

/**
 * A composable function that displays a short weather overview in a card layout with shared transition animations.
 *
 * @param animatedVisibilityScope The scope [AnimatedVisibilityScope] used for the animated visibility within the shared transition.
 * @param overview The [ShortWeatherOverview] data containing weather information such as city name, temperature, and apparent temperature.
 * @param key A unique key used to identify this card and its shared elements during transitions.
 * @param onClick A lambda function to be executed when the card is clicked.
 *
 * This card uses shared transitions to animate its content when entering or leaving the screen. The card is clickable
 * and triggers the provided [onClick] action when tapped. The card displays the city name, the current temperature,
 * and the "feels like" temperature.
 *
 * @see SharedTransitionScope
 * @see AnimatedVisibilityScope
 */
@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.ShortWeatherOverviewCard(
    animatedVisibilityScope: AnimatedVisibilityScope,
    overview: ShortWeatherOverview,
    key: String,
    onClick: () -> Unit,
) {
    Card(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
                .sharedElement(
                    state = rememberSharedContentState(key = "temp_card/$key"),
                    animatedVisibilityScope = animatedVisibilityScope,
                    boundsTransform = { _, _ -> tween(durationMillis = 1000) },
                ).clickable { onClick() },
        colors =
            CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainerHighest,
            ),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(modifier = Modifier.weight(2f)) {
                Text(
                    text = overview.cityName,
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier =
                        Modifier.sharedElement(
                            state = rememberSharedContentState(key = "city/${overview.cityName}/$key"),
                            animatedVisibilityScope = animatedVisibilityScope,
                            boundsTransform = { _, _ -> tween(durationMillis = 1000) },
                        ),
                )

                Row(verticalAlignment = Alignment.Bottom) {
                    Text(
                        text = "feels like ",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.tertiary,
                        modifier =
                            Modifier.sharedElement(
                                state = rememberSharedContentState(key = "feels_like/$key"),
                                animatedVisibilityScope = animatedVisibilityScope,
                                boundsTransform = { _, _ -> tween(durationMillis = 1000) },
                            ),
                    )
                    Text(
                        text = "${overview.apparentTemperature.first}°",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier =
                            Modifier.sharedElement(
                                state = rememberSharedContentState(key = "feels_like/temp/$key"),
                                animatedVisibilityScope = animatedVisibilityScope,
                                boundsTransform = { _, _ -> tween(durationMillis = 1000) },
                            ),
                    )
                }
            }
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.End,
            ) {
                Text(
                    text = "${overview.temperature2m.first}${overview.temperature2m.second}",
                    style = MaterialTheme.typography.displayMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier =
                        Modifier.sharedElement(
                            state = rememberSharedContentState(key = "temp_2m/${overview.temperature2m.first}/$key"),
                            animatedVisibilityScope = animatedVisibilityScope,
                            boundsTransform = { _, _ -> tween(durationMillis = 1000) },
                        ),
                )
            }
        }
    }
}

@Preview
@Composable
fun CityCardPreview() {
    TheWeatherAppTheme {
        /*CityCard(
            ShortWeatherOverview(
                cityName = "Sint-Niklaas",
                temperature2m = Pair(16, "C"),
                apparentTemperature = Pair(17, "C"),
            ),
        ) {}*/
    }
}
