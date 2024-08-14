package com.example.theweatherapp.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.theweatherapp.R
import com.example.theweatherapp.domain.errors.ErrorCode
import com.example.theweatherapp.ui.theme.TheWeatherAppTheme

/**
 * A composable function that displays an error message and an icon in the center of the screen.
 * The error message is shown as a Snackbar at the bottom of the screen.
 *
 * @param modifier A [Modifier] to be applied to the [Box] container. Default is [Modifier] which allows for custom styling and layout adjustments.
 * @param snackbarHostState An instance of [SnackbarHostState] used to control the display and dismissal of the Snackbar.
 * @param errorMessage A [String] that contains the message to be shown in the Snackbar.
 *
 * The function displays an icon indicating an error or dissatisfaction and uses a [SnackbarHostState]
 * to show the error message in a Snackbar. The icon is centered in the [Box] and the Snackbar message
 * is shown using [LaunchedEffect] when the composable is first composed.
 */
@Composable
fun AbsolutErrorBox(
    modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState,
    errorMessage: String,
) {
    Box(modifier = modifier.fillMaxSize().padding(16.dp), contentAlignment = Alignment.Center) {
        Icon(
            painterResource(R.drawable.sentiment_very_dissatisfied_24dp_e8eaed_fill0_wght400_grad0_opsz24),
            contentDescription = null,
            modifier = Modifier.size(100.dp),
        )

        LaunchedEffect(Unit) {
            snackbarHostState
                .showSnackbar(
                    errorMessage,
                )
        }
    }
}

@Composable
@Preview(showBackground = true)
fun AbsolutErrorBoxpreview() {
    TheWeatherAppTheme {
        AbsolutErrorBox(
            snackbarHostState = remember { SnackbarHostState() },
            errorMessage = ErrorCode.CITY_ERROR.message,
        )
    }
}
