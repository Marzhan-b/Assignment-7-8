package com.example.weatherfirebaseapp.data.local

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.example.weatherfirebaseapp.domain.model.DailyForecast
import com.example.weatherfirebaseapp.domain.model.Weather
import kotlinx.coroutines.flow.first

private val Context.dataStore by preferencesDataStore(name = "weather_cache")
class CacheManager(private val context: Context) {

    companion object {
        private val TEMP = doublePreferencesKey("temp")
        private val WIND = doublePreferencesKey("wind")
        private val FORECAST = stringPreferencesKey("forecast")
        private val LAST_UPDATED=stringPreferencesKey("last_updated")
    }

    suspend fun saveWeather(weather: Weather) {
        val forecastString = weather.forecast.joinToString(";") {
            "${it.maxTemp},${it.minTemp}"
        }

        context.dataStore.edit { prefs ->
            prefs[TEMP] = weather.temperature
            prefs[WIND] = weather.windSpeed
            prefs[FORECAST] = forecastString
            prefs[LAST_UPDATED]=weather.lastUpdate
        }
    }

    suspend fun loadWeather(): Weather? {
        val prefs = context.dataStore.data.first()

        val temp = prefs[TEMP] ?: return null
        val wind = prefs[WIND] ?: return null
        val forecastString = prefs[FORECAST] ?: return null
        val timeStr = prefs[LAST_UPDATED] ?: "Unknown"

        val forecast = forecastString.split(";").map {
            val (max, min) = it.split(",")
            DailyForecast(max.toDouble(), min.toDouble())
        }

        return Weather(
            temperature = temp,
            windSpeed = wind,
            forecast = forecast,
            lastUpdate = timeStr
        )
    }
}
