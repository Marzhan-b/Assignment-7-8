package com.example.weatherfirebaseapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.weatherfirebaseapp.viewModel.WeatherUiState
import com.example.weatherfirebaseapp.viewModel.WeatherViewModel

@Composable
fun WeatherScreen(
    cityName: String = "",
    viewModel: WeatherViewModel = viewModel()
) {
    var inputError by remember { mutableStateOf<String?>(null) }
    var city by remember { mutableStateOf("") }

    val state by viewModel.uiState.collectAsState()
    val isCelsius by viewModel.isCelsius.collectAsState()
    LaunchedEffect(Unit) {
        viewModel.loadLastCityWeather()
    }

    LaunchedEffect(cityName) {
        if (cityName.isNotBlank()) {
            city = cityName
            val coords = getCoordinatesForCity(cityName)
            if (coords != null) {
                inputError = null
                viewModel.loadWeather(cityName, coords.first, coords.second)
            } else {
                inputError = "City '$cityName' not found in database"
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Weather Settings", style = MaterialTheme.typography.titleMedium)
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("°C")
                Switch(
                    checked = !isCelsius,
                    onCheckedChange = { viewModel.toggleUnits() }
                )
                Text("°F")
            }
        }

        OutlinedTextField(
            value = city,
            onValueChange = {
                city = it
                inputError = null
            },
            label = { Text("Enter City Name") },
            isError = inputError != null,
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = {
                val trimmedCity = city.trim()
                if (trimmedCity.isBlank()) {
                    inputError = "Please enter a city name"
                } else {
                    val coords = getCoordinatesForCity(trimmedCity)
                    if (coords != null) {
                        inputError = null
                        viewModel.loadWeather(trimmedCity, coords.first, coords.second)
                    } else {
                        inputError = "City '$trimmedCity' not found in database"
                    }
                }
            },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("Search")
        }

        inputError?.let {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }

        HorizontalDivider()

        when (state) {
            is WeatherUiState.Loading -> {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }

            is WeatherUiState.Error -> {
                Text(
                    text = (state as WeatherUiState.Error).message,
                    color = MaterialTheme.colorScheme.error,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            is WeatherUiState.Success -> {
                val success = state as WeatherUiState.Success
                val weather = success.weather
                val temp = if (isCelsius) weather.temperature else weather.temperature * 9 / 5 + 32
                val unit = if (isCelsius) "°C" else "°F"

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {

                    if (success.isOffline) {
                        Surface(
                            color = MaterialTheme.colorScheme.errorContainer,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "Viewing cached data (Offline)",
                                modifier = Modifier.padding(8.dp),
                                textAlign = TextAlign.Center
                            )
                        }
                    }

                    Text(
                        "Current Temperature: ${String.format("%.1f", temp)} $unit",
                        style = MaterialTheme.typography.headlineSmall
                    )
                    Text("Wind Speed: ${weather.windSpeed} km/h")

                    Spacer(modifier = Modifier.height(12.dp))
                    Text("3-Day Forecast:", style = MaterialTheme.typography.titleMedium)

                    weather.forecast.forEachIndexed { index, day ->
                        val maxT = if (isCelsius) day.maxTemp else day.maxTemp * 9 / 5 + 32
                        val minT = if (isCelsius) day.minTemp else day.minTemp * 9 / 5 + 32

                        Card(modifier = Modifier.fillMaxWidth()) {
                            Text(
                                text = "Day ${index + 1}: Max ${String.format("%.1f", maxT)}$unit / Min ${String.format("%.1f", minT)}$unit",
                                modifier = Modifier.padding(12.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

private fun getCoordinatesForCity(city: String): Pair<Double, Double>? =
    when (city.trim().lowercase()) {
        "astana" -> 51.1694 to 71.4491
        "almaty" -> 43.2220 to 76.8512
        "shymkent" -> 42.3417 to 69.5901
        "karaganda" -> 49.8019 to 73.1021
        "london" -> 51.5074 to -0.1278
        "new york" -> 40.7128 to -74.0060
        else -> null
    }
