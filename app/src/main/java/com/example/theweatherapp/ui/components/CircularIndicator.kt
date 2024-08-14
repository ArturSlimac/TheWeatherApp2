package com.example.theweatherapp.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.theweatherapp.ui.theme.TheWeatherAppTheme

/**
 * A composable function that displays a circular progress indicator centered within its container.
 * This indicator is used to show that some operation is in progress.
 *
 * @param modifier An optional [Modifier] to be applied to the progress indicator's container.
 * This allows customization of the indicator's size, alignment, and other properties.
 *
 */
@Composable
fun CircularIndicator(modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxWidth().padding(16.dp), contentAlignment = Alignment.TopCenter) {
        CircularProgressIndicator(
            color = MaterialTheme.colorScheme.tertiary,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CircularIndicatorPreview() {
    TheWeatherAppTheme {
        CircularIndicator()
    }
}
