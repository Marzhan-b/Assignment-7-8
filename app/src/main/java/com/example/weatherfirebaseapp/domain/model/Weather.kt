package com.example.weatherfirebaseapp.domain.model

data class Weather(
    val temperature: Double,
    val windSpeed: Double,
    val maxTemp: Double,
    val minTemp: Double
)