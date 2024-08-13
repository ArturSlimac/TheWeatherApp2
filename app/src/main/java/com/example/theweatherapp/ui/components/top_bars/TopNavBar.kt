package com.example.theweatherapp.ui.components.top_bars

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import com.example.theweatherapp.domain.model.city.CityItemModel

/**
 * A composable function that displays a top navigation bar with shared transition animations.
 *
 * This top navigation bar shows the selected city's name, allows navigation back, and provides
 * options to save or delete the city. The title is animated using shared element transitions.
 *
 * @param selectedCity The city item model representing the selected city.
 * @param animatedVisibilityScope The scope that handles the visibility of animated components.
 * @param animationKey A unique key used to identify and animate the shared element.
 * @param onBackClick A callback function triggered when the back button (navigationIcon) is clicked.
 * @param onSaveClick A callback function triggered when the save button is clicked.
 * @param onDeleteClick A callback function triggered when the delete button is clicked.
 */
@OptIn(ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.TopNavBar(
    selectedCity: CityItemModel,
    animatedVisibilityScope: AnimatedVisibilityScope,
    animationKey: String,
    onBackClick: () -> Unit,
    onSaveClick: () -> Unit,
    onDeleteClick: () -> Unit,
) {
    CenterAlignedTopAppBar(
        modifier = Modifier.fillMaxWidth(),
        scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(),
        colors =
            TopAppBarDefaults.centerAlignedTopAppBarColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                scrolledContainerColor = Color.Transparent,
            ),
        title = {
            Text(
                text = selectedCity.name,
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                textAlign = TextAlign.Center,
                modifier =
                    Modifier.sharedElement(
                        state = rememberSharedContentState(key = animationKey),
                        animatedVisibilityScope = animatedVisibilityScope,
                        boundsTransform = { _, _ -> tween(durationMillis = 1000) },
                    ),
            )
        },
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(Icons.AutoMirrored.Outlined.ArrowBack, contentDescription = "Returns to the searching")
            }
        },
        actions = {
            if (selectedCity.isSaved) {
                IconButton(onClick = onDeleteClick) {
                    Icon(Icons.Outlined.Delete, contentDescription = "Deletes current city from the list")
                }
            } else {
                IconButton(onClick = onSaveClick) {
                    Icon(Icons.Outlined.Add, contentDescription = "Saves current city in the application")
                }
            }
        },
    )
}
