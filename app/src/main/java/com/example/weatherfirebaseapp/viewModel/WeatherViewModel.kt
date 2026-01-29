package com.example.weatherfirebaseapp.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherfirebaseapp.data.repository.WeatherRepository
import com.example.weatherfirebaseapp.domain.model.Weather
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class WeatherViewModel : ViewModel() {

    private val repository = WeatherRepository()

    private val _uiState = MutableStateFlow<WeatherUiState>(WeatherUiState.Loading)
    val uiState: StateFlow<WeatherUiState> = _uiState

    fun loadWeather(latitude: Double, longitude: Double) {
        viewModelScope.launch {
            _uiState.value = WeatherUiState.Loading

            try {
                val dto = repository.getWeather(latitude, longitude)

                val weather = Weather(
                    temperature = dto.currentWeather?.temperature ?: 0.0,
                    windSpeed = dto.currentWeather?.windspeed ?: 0.0,
                    maxTemp = dto.daily?.maxTemps?.firstOrNull() ?: 0.0,
                    minTemp = dto.daily?.minTemps?.firstOrNull() ?: 0.0
                )

                _uiState.value = WeatherUiState.Success(weather)
            } catch (e: Exception) {
                _uiState.value = WeatherUiState.Error("Failed to load weather")
            }
        }
    }
}
sealed class WeatherUiState {
    object Loading : WeatherUiState()
    data class Success(val weather: Weather) : WeatherUiState()
    data class Error(val message: String) : WeatherUiState()
}