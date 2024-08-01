package com.example.theweatherapp.fragment.all_cities

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.theweatherapp.fragment.all_cities.screen.SavedCitiesScreen
import com.example.theweatherapp.viewmodel.SavedCitiesViewModel

@Composable
fun SavedCitiesFragment(
    modifier: Modifier = Modifier,
    savedCitiesViewModel: SavedCitiesViewModel = hiltViewModel(),
) {
    val foundCitiesState by savedCitiesViewModel.foundCitiesState
    val isSearchingState by savedCitiesViewModel.isSearching
    val searchText by savedCitiesViewModel.searchText

    SavedCitiesScreen(
        modifier,
        searchText = searchText,
        onSearchTextChange = savedCitiesViewModel::onSearchTextChange,
        isSearching = isSearchingState,
        onToggleSearch = savedCitiesViewModel::onToggleSearch,
        foundCitiesState = foundCitiesState,
    )
}
