package com.example.weatherfirebaseapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.weatherfirebaseapp.ui.viewmodel.FavoritesViewModel

@Composable
fun SearchScreen(viewModel: FavoritesViewModel) {
    var cityName by remember { mutableStateOf("") }
    var note by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = "Add New City",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        OutlinedTextField(
            value = cityName,
            onValueChange = { cityName = it },
            label = { Text("City Name") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = note,
            onValueChange = { note = it },
            label = { Text("Note (Optional)") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Button(
            onClick = {
                if (cityName.isNotBlank()) {
                    viewModel.addFavorite(cityName, note)
                    cityName = ""
                    note = ""
                }
            },
            modifier = Modifier
                .padding(top = 16.dp)
                .fillMaxWidth(),
            enabled = cityName.isNotBlank()
        ) {
            Text("Add to Favorites")
        }
    }
}