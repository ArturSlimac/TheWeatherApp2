package com.example.theweatherapp.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.theweatherapp.R

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
