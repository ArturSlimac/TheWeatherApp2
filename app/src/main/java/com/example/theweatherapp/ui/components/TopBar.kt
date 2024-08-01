package com.example.theweatherapp.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
fun TopBar(
    searchText: String,
    onSearchTextChange: (String) -> Unit,
    isSearching: Boolean,
    onToggleSearch: () -> Unit,
    foundCitiesState: Response<CityModel>,
    onCitySelected: (CityItemModel) -> Unit,
) {
    androidx.compose.material3.SearchBar(
        query = searchText,
        onQueryChange = onSearchTextChange,
        onSearch = onSearchTextChange,
        active = isSearching,
        onActiveChange = { onToggleSearch() },
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
                    )
                } else {
                    Icon(
                        imageVector = Icons.Filled.Search,
                        contentDescription = "Opens the search bar",
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
                    )
                }
            }
        },
        placeholder = {
            Text(
                text = "Search",
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                style = MaterialTheme.typography.bodyLarge,
            )
        },
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(16.dp),
    ) {
        if (searchText.isEmpty()) {
            Box(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                Text(
                    text = "Start typing to find cities",
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                    style = MaterialTheme.typography.bodyMedium,
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
                    LazyColumn(modifier = Modifier.fillMaxSize().padding(top = 16.dp)) {
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
                            color = MaterialTheme.colorScheme.onSecondaryContainer,
                            style = MaterialTheme.typography.bodyMedium,
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
