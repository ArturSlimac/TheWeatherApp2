package com.example.theweatherapp.screens

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.theweatherapp.domain.formaters.formatDate
import com.example.theweatherapp.domain.mappers.toShortWeatherOverview
import com.example.theweatherapp.domain.model.weather.WeatherModel
import com.example.theweatherapp.ui.components.AbsolutErrorBox
import com.example.theweatherapp.ui.components.BottomNavigationBar
import com.example.theweatherapp.ui.components.CircularIndicator
import com.example.theweatherapp.ui.components.top_bars.TopSearchBar
import com.example.theweatherapp.ui.components.weather_cards.ShortWeatherOverviewCard
import com.example.theweatherapp.ui.navigation.NavigationDestination
import com.example.theweatherapp.utils.Response
import com.example.theweatherapp.viewmodel.SavedCitiesViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.SavedCitiesScreen(
    animatedVisibilityScope: AnimatedVisibilityScope,
    savedCitiesViewModel: SavedCitiesViewModel = hiltViewModel(),
    navController: NavHostController,
) {
    val foundCitiesState by savedCitiesViewModel.foundCitiesState
    val isSearchingState by savedCitiesViewModel.isSearching
    val searchText by savedCitiesViewModel.searchText
    val savedCitiesWeatherState by savedCitiesViewModel.savedCitiesWeatherState
    val isScrolledToEnd by savedCitiesViewModel.isScrolledToEnd.collectAsState()

    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val listState = rememberLazyListState()

    LaunchedEffect(savedCitiesWeatherState) {
        if (isScrolledToEnd) {
            listState.animateScrollToItem((savedCitiesWeatherState as? Response.Success<List<WeatherModel>>)?.data?.size?.minus(1) ?: 0)
            savedCitiesViewModel.setScrolledToEnd(false)
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            TopSearchBar(
                searchText = searchText,
                onSearchTextChange = savedCitiesViewModel::onSearchTextChange,
                isSearching = isSearchingState,
                onToggleSearch = savedCitiesViewModel::onToggleSearch,
                foundCitiesState = foundCitiesState,
                onCitySelected = {
                    scope.launch {
                        savedCitiesViewModel.onSearchCitySelected(it)
                        navController.navigate(NavigationDestination.WeatherDetails.route)
                    }
                },
            )
        },
        bottomBar = {
            BottomNavigationBar(currentDestination = NavigationDestination.SavedCities, navController = navController)
        },
    ) { innerPadding ->
        when (savedCitiesWeatherState) {
            is Response.Loading -> {
                CircularIndicator(modifier = Modifier.padding(innerPadding))
            }
            is Response.Success -> {
                Box(modifier = Modifier.fillMaxSize().padding(innerPadding).padding(top = 16.dp)) {
                    val weatherModels = (savedCitiesWeatherState as Response.Success<List<WeatherModel>>).data

                    weatherModels?.let {
                        if (weatherModels.firstOrNull()?.cashed == true) {
                            scope.launch {
                                snackbarHostState
                                    .showSnackbar(
                                        "There is no internet connection. The weather was last updated at ${
                                            weatherModels.first().formatDate()
                                        }",
                                    )
                            }
                        }

                        LazyColumn(state = listState) {
                            items(weatherModels) { weatherModel ->
                                ShortWeatherOverviewCard(
                                    overview = weatherModel.toShortWeatherOverview(),
                                    key = weatherModel.city!!.name,
                                    animatedVisibilityScope = animatedVisibilityScope,
                                ) {
                                    savedCitiesViewModel.onCitySelected(weatherModel.city, weatherModel)
                                    navController.navigate(NavigationDestination.WeatherDetails.route)
                                }
                            }
                        }
                    }
                }
            }
            is Response.Failure -> {
                val errorMessage =
                    (savedCitiesWeatherState as Response.Failure).e?.message ?: "Something went wrong. Please try again later"
                AbsolutErrorBox(modifier = Modifier.padding(innerPadding), snackbarHostState, errorMessage)
            }
        }
    }
}
