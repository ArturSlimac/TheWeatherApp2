package com.example.theweatherapp.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.theweatherapp.domain.model.city.CityModel
import com.example.theweatherapp.domain.repository.CityRepository
import com.example.theweatherapp.utils.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SavedCitiesViewModel
    @Inject
    constructor(
        private val cityRepository: CityRepository,
    ) : ViewModel() {
        private val _isSearching = mutableStateOf(false)
        val isSearching: State<Boolean> = _isSearching

        private val _searchText = mutableStateOf("")
        val searchText: State<String> = _searchText

        private val _foundCitiesState = mutableStateOf<Response<CityModel>>(Response.Loading)
        val foundCitiesState: State<Response<CityModel>> = _foundCitiesState

        fun onSearchTextChange(text: String) {
            _searchText.value = text
            if (_isSearching.value) {
                searchCitiesByName(_searchText.value)
            }
        }

        fun onToggleSearch() {
            _isSearching.value = !_isSearching.value
            if (!_isSearching.value) {
                onSearchTextChange("")
                _foundCitiesState.value = Response.Loading
            }
        }

        private fun searchCitiesByName(name: String) {
            viewModelScope.launch {
                _foundCitiesState.value = Response.Loading
                if (name.isNotEmpty()) {
                    cityRepository.getCitiesByName(name).collect { response ->
                        _foundCitiesState.value = response
                    }
                }
            }
        }
    }
