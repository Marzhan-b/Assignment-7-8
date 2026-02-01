package com.example.weatherfirebaseapp.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherfirebaseapp.data.local.CacheManager
import com.example.weatherfirebaseapp.data.repository.WeatherRepository
import com.example.weatherfirebaseapp.domain.model.DailyForecast
import com.example.weatherfirebaseapp.domain.model.Weather
import com.example.weatherfirebaseapp.util.CityCoordinates
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class WeatherViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = WeatherRepository()
    private val cache = CacheManager(application)

    private val _uiState = MutableStateFlow<WeatherUiState>(WeatherUiState.Loading)
    val uiState: StateFlow<WeatherUiState> = _uiState

    private val _isCelsius = MutableStateFlow(true)
    val isCelsius: StateFlow<Boolean> = _isCelsius

    fun toggleUnits() {
        _isCelsius.value = !_isCelsius.value
    }

    fun loadLastCityWeather() {
        viewModelScope.launch {
            val city = cache.loadCity() ?: return@launch
            val coords = CityCoordinates.get(city) ?: return@launch
            loadWeather(city, coords.first, coords.second)
        }
    }

    fun loadWeather(cityName: String, lat: Double, lon: Double) {
        viewModelScope.launch {
            _uiState.value = WeatherUiState.Loading
            try {
                val dto = repository.getWeather(lat, lon)

                val forecast = dto.daily?.maxTemps
                    ?.zip(dto.daily.minTemps)
                    ?.take(3)
                    ?.map { (max, min) -> DailyForecast(max, min) }
                    ?: emptyList()

                val time = SimpleDateFormat("HH:mm, dd MMM", Locale.getDefault()).format(Date())

                val weather = Weather(
                    temperature = dto.currentWeather?.temperature ?: 0.0,
                    windSpeed = dto.currentWeather?.windspeed ?: 0.0,
                    forecast = forecast,
                    lastUpdate = time
                )

                cache.saveWeather(weather)
                cache.saveCity(cityName)

                _uiState.value = WeatherUiState.Success(weather)
            } catch (e: Exception) {
                val cached = cache.loadWeather()
                if (cached != null) {
                    _uiState.value = WeatherUiState.Success(cached, true)
                } else {
                    _uiState.value = WeatherUiState.Error("Failed to load weather")
                }
            }
        }
    }
}
