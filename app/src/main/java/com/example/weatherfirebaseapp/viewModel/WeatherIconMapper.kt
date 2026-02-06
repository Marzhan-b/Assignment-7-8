package com.example.weatherfirebaseapp.viewModel

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*

fun getWeatherIcon(condition: String) = when (condition.lowercase()) {
    "sunny", "clear" -> Icons.Filled.WbSunny
    "cloudy", "overcast" -> Icons.Filled.Cloud
    "rain", "rainy" -> Icons.Filled.Grain
    "snow" -> Icons.Filled.AcUnit
    "storm", "thunder" -> Icons.Filled.Thunderstorm
    else -> Icons.Filled.Cloud
}
