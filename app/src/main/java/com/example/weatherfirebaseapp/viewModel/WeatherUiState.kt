package com.example.weatherfirebaseapp.viewModel

import com.example.weatherfirebaseapp.domain.model.Weather

sealed class WeatherUiState {

    object Loading : WeatherUiState()

    data class Success(
        val cityName: String,
        val weather: Weather,
        val isOffline: Boolean = false
    ) : WeatherUiState()


    data class Error(
        val message: String
    ) : WeatherUiState()
}
