package com.example.weatherfirebaseapp.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherfirebaseapp.data.local.CacheManager
import com.example.weatherfirebaseapp.data.repository.WeatherRepository
import com.example.weatherfirebaseapp.domain.model.DailyForecast
import com.example.weatherfirebaseapp.domain.model.Weather
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class WeatherViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = WeatherRepository()
    private val cache = CacheManager(application)
    private val _uiState = MutableStateFlow<WeatherUiState>(WeatherUiState.Loading)
    private val _isCelsius = MutableStateFlow(true)
    val isCelsius: StateFlow<Boolean> = _isCelsius
    val uiState: StateFlow<WeatherUiState> = _uiState

    fun toggleUnits() {
        _isCelsius.value = !_isCelsius.value
    }

    fun loadWeather(latitude: Double, longitude: Double) {
        viewModelScope.launch {
            _uiState.value = WeatherUiState.Loading

            try {
                val dto = repository.getWeather(latitude, longitude)

                val forecastList = dto.daily?.maxTemps
                    ?.zip(dto.daily.minTemps)
                    ?.take(3)
                    ?.map { (max, min) ->
                        DailyForecast(
                            maxTemp = max,
                            minTemp = min
                        )
                    } ?: emptyList()

                val currentTime = SimpleDateFormat("HH:mm, dd MMM", Locale.getDefault()).format(Date())

                val weather = Weather(
                    temperature = dto.currentWeather?.temperature ?: 0.0,
                    windSpeed = dto.currentWeather?.windspeed ?: 0.0,
                    forecast = forecastList,
                    lastUpdate = currentTime
                )

                cache.saveWeather(weather)
                _uiState.value = WeatherUiState.Success(weather)

            } catch (e: Exception) {
                val cached = cache.loadWeather()
                if (cached != null) {
                    _uiState.value = WeatherUiState.Success(
                        weather = cached,
                        isOffline = true
                    )
                } else {
                    _uiState.value = WeatherUiState.Error("Failed to load weather: ${e.message}")
                }
            }
        }
    }
}

sealed class WeatherUiState {
    object Loading : WeatherUiState()

    data class Success(
        val weather: Weather,
        val isOffline: Boolean = false
    ) : WeatherUiState()

    data class Error(val message: String) : WeatherUiState()
}