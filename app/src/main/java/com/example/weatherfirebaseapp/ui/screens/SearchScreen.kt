package com.example.weatherfirebaseapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.weatherfirebaseapp.viewModel.WeatherViewModel

@Composable
fun SearchScreen(
    viewModel: WeatherViewModel = viewModel()
) {
    var city by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        OutlinedTextField(
            value = city,
            onValueChange = { city = it },
            label = { Text("City") },
            singleLine = true
        )

        Button(
            onClick = {
                val coords = getCoordinatesForCity(city)
                if (coords != null) {
                    viewModel.loadWeather(coords.first, coords.second)
                }
            }
        ) {
            Text("Search")
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
