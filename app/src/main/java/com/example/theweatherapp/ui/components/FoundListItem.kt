package com.example.theweatherapp.ui.components

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.theweatherapp.domain.model.city.CityItemModel

@OptIn(ExperimentalFoundationApi::class, ExperimentalSharedTransitionApi::class)
@Composable
fun FoundListItem(
    item: CityItemModel,
    onItemSelected: () -> Unit,
) {
    ListItem(
        modifier =
            Modifier.clickable(
                onClick = {
                    // Click action if required when not in selection mode
                    onItemSelected()
                },
            ),
        colors =
            ListItemDefaults.colors(
                containerColor = MaterialTheme.colorScheme.surface,
                headlineColor = MaterialTheme.colorScheme.onSurface,
                leadingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
            ),
        headlineContent = {
            Text(
                text = "${item.name}, ${item.country}",
                style = MaterialTheme.typography.bodyLarge,

            )
        },
        leadingContent = {
            Icon(
                imageVector = Icons.Filled.Search,
                contentDescription = item.name,
            )
        },
    )
}

@Preview
@Composable
fun FoundListItemPreview() {
 /*   FoundListItem(
        CityItemModel(
            "BE",
            "Sint-Niklaas",
            longitude = 12.2,
            latitude = 12.2,
            state = null,
        ),
        {},
    )*/
}
