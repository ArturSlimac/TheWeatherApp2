package com.example.theweatherapp.fragment.all_cities.screen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.theweatherapp.domain.model.city.CityModel
import com.example.theweatherapp.ui.components.TopBar
import com.example.theweatherapp.utils.Response

@Composable
fun SavedCitiesScreen(
    modifier: Modifier,
    searchText: String,
    onSearchTextChange: (String) -> Unit,
    isSearching: Boolean,
    onToggleSearch: () -> Unit,
    foundCitiesState: Response<CityModel>,
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopBar(
                searchText = searchText,
                onSearchTextChange = onSearchTextChange,
                isSearching = isSearching,
                onToggleSearch = onToggleSearch,
                foundCitiesState = foundCitiesState,
                onCitySelected = {},
            )
        },
    ) {
    }
}
