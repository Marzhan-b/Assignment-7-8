package com.example.weatherfirebaseapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.weatherfirebaseapp.viewModel.WeatherUiState
import com.example.weatherfirebaseapp.viewModel.WeatherViewModel

@Composable
fun WeatherScreen(
    viewModel: WeatherViewModel = viewModel()
) {
    var city by remember { mutableStateOf("") }
    val state by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        OutlinedTextField(
            value = city,
            onValueChange = { city = it },
            label = { Text("City") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = {
                val coords = getCoordinatesForCity(city)
                if (coords != null) {
                    viewModel.loadWeather(coords.first, coords.second)
                }
            },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("Search")
        }

        Divider()

        when (state) {
            is WeatherUiState.Loading -> {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }

            is WeatherUiState.Error -> {
                Text(
                    text = (state as WeatherUiState.Error).message,
                    color = MaterialTheme.colorScheme.error
                )
            }

            is WeatherUiState.Success -> {
                val weather = (state as WeatherUiState.Success).weather

                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text("Temperature: ${weather.temperature}°C")
                    Text("Wind: ${weather.windSpeed} km/h")
                    Text("Max: ${weather.maxTemp}°C")
                    Text("Min: ${weather.minTemp}°C")
                }
            }
        }
    }
}

private fun getCoordinatesForCity(city: String): Pair<Double, Double>? {
    return when (city.lowercase()) {
        "astana" -> 51.1694 to 71.4491
        "almaty" -> 43.2220 to 76.8512
        "london" -> 51.5074 to -0.1278
        "new york" -> 40.7128 to -74.0060
        else -> null
    }
}
