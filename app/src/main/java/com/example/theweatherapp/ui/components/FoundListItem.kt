package com.example.theweatherapp.ui.components

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

/**
 * A composable function that displays an item in a list with a clickable interaction.
 * This is used to represent a city item in a list format with an icon and text.
 *
 * @param item A [CityItemModel] representing the city to be displayed. It includes the city name
 * and country which are shown in the list item.
 * @param onItemSelected A lambda function to be called when the list item is clicked. This allows
 * for handling the click action, such as navigating to a detail view or performing another action.
 *
 */
@Composable
fun FoundListItem(
    item: CityItemModel,
    onItemSelected: () -> Unit,
) {
    ListItem(
        modifier =
            Modifier.clickable(
                onClick = {
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

@Preview(showBackground = true)
@Composable
fun FoundListItemPreview() {
    FoundListItem(
        CityItemModel(
            "BE",
            "Sint-Niklaas",
            longitude = 12.2,
            latitude = 12.2,
            state = "",
        ),
        {},
    )
}
