package com.example.theweatherapp.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.theweatherapp.domain.model.city.CityItemModel
import com.example.theweatherapp.domain.model.city.CityModel
import com.example.theweatherapp.ui.theme.TheWeatherAppTheme
import com.example.theweatherapp.utils.Response

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopSearchBar(
    searchText: String,
    onSearchTextChange: (String) -> Unit,
    isSearching: Boolean,
    onToggleSearch: () -> Unit,
    foundCitiesState: Response<CityModel>,
    onCitySelected: (CityItemModel) -> Unit,
) {
    SearchBar(
        query = searchText,
        onQueryChange = onSearchTextChange,
        onSearch = onSearchTextChange,
        active = isSearching,
        onActiveChange = { onToggleSearch() },
        colors =
            SearchBarDefaults.colors(
                containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                dividerColor = MaterialTheme.colorScheme.outline,
            ),
        leadingIcon = {
            IconButton(
                onClick = {
                    onToggleSearch()
                },
            ) {
                if (isSearching) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Closes the search bar",
                        tint = MaterialTheme.colorScheme.onSurface,
                    )
                } else {
                    Icon(
                        imageVector = Icons.Filled.Search,
                        contentDescription = "Opens the search bar",
                        tint = MaterialTheme.colorScheme.onSurface,
                    )
                }
            }
        },
        trailingIcon = {
            if (isSearching) {
                IconButton(
                    onClick = {
                        onSearchTextChange("")
                    },
                ) {
                    Icon(
                        imageVector = Icons.Filled.Clear,
                        contentDescription = "Clears the search query",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
        },
        placeholder = {
            Text(
                text = "Search",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodyLarge,
            )
        },
        modifier =
            if (isSearching) {
                Modifier
                    .fillMaxWidth()
            } else {
                Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = 16.dp,
                    )
            },
    ) {
        if (searchText.isEmpty()) {
            Box(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                Text(
                    text = "Start typing to find cities",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodyLarge,
                )
            }
        }

        when (foundCitiesState) {
            is Response.Loading -> {
                if (searchText.isNotEmpty()) {
                    Box(modifier = Modifier.fillMaxWidth().padding(16.dp), contentAlignment = Alignment.TopCenter) {
                        CircularProgressIndicator(
                            color = MaterialTheme.colorScheme.tertiary,
                        )
                    }
                }
            }

            is Response.Success -> {
                val cities = foundCitiesState.data
                if (!cities.isNullOrEmpty()) {
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items(cities) { city ->
                            FoundListItem(
                                city,
                            ) {
                                onCitySelected(city)
                            }
                        }
                    }
                } else {
                    Box(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                        Text(
                            text = "No cities found",
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            style = MaterialTheme.typography.bodyLarge,
                        )
                    }
                }
            }

            is Response.Failure -> {
                Box(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = "Error loading cities",
                        color = MaterialTheme.colorScheme.onErrorContainer,
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun TopBarPreview() {
    TheWeatherAppTheme {
        // TopBar()
    }
}
