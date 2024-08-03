package com.example.theweatherapp.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.theweatherapp.domain.model.helpers.TemperatureDescription
import com.example.theweatherapp.ui.theme.TheWeatherAppTheme

@Composable
fun CurrentTemperatureCard(
    temperature: Int,
    tempUnit: String,
) {
    val temperatureDescription = TemperatureDescription.tempDescription(temperature)
    val gradientOffset = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        while (true) {
            gradientOffset.animateTo(
                targetValue = 1f,
                animationSpec =
                    infiniteRepeatable(
                        animation = tween(durationMillis = 3000, easing = LinearEasing),
                        repeatMode = RepeatMode.Reverse,
                    ),
            )
        }
    }
    val colors = temperatureDescription.colors

    val animatedGradient =
        Brush.verticalGradient(
            colors = colors,
            startY = gradientOffset.value * 1000f,
            endY = gradientOffset.value * 1000f + 1000f,
        )

    Card(
        modifier =
            Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .padding(8.dp)
                .background(brush = animatedGradient, shape = RoundedCornerShape(15.dp)),
        colors =
            CardDefaults.cardColors(
                containerColor = Color.Transparent,
            ),
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = stringResource(id = temperatureDescription.tempDescription),
                style = MaterialTheme.typography.displaySmall,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                textAlign = TextAlign.End,
                modifier = Modifier.padding(16.dp),
            )
            Text(
                text = "$temperature°$tempUnit",
                style = MaterialTheme.typography.displayLarge,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                textAlign = TextAlign.Start,
                modifier = Modifier.padding(32.dp),
            )
        }
    }
}

@Composable
@Preview
fun CurrentTemperatureCardPreview() {
    TheWeatherAppTheme {
        CurrentTemperatureCard(32, "C")
    }
}
