package com.example.weatherfirebaseapp.data.remote

import com.squareup.moshi.Json

data class WeatherDto(
    @Json(name = "current_weather")
    val currentWeather: CurrentWeatherDto?,

    val daily: DailyWeatherDto?
)

data class CurrentWeatherDto(
    val temperature: Double,
    val windspeed: Double,
    val weathercode: Int
)

data class DailyWeatherDto(
    @Json(name = "temperature_2m_max")
    val maxTemps: List<Double>,

    @Json(name = "temperature_2m_min")
    val minTemps: List<Double>
)