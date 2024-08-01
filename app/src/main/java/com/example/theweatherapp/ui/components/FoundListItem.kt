package com.example.theweatherapp.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.fontResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.theweatherapp.domain.model.city.CityItemModel

@Composable
fun FoundListItem(
    item: CityItemModel,
    onItemSelected: () -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier =
            Modifier.fillMaxWidth().clickable {
                onItemSelected()
            },
    ) {
        Icon(imageVector = Icons.Filled.Search, contentDescription = item.name, modifier = Modifier.padding(16.dp, 8.dp, 16.dp, 8.dp))
        Text(
            text = "${item.name}, ${item.country}",
style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSecondaryContainer
        )
    }
}

@Preview
@Composable
fun FoundListItemPreview() {
    FoundListItem(
        CityItemModel(
            "BE",
            "Sint-Niklaas",
            longitude = 12.2,
            latitude = 12.2,
            state = null,
        ),
        {},
    )
}
