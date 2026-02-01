package com.example.weatherfirebaseapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.weatherfirebaseapp.data.firebase.FavoriteDto
import com.example.weatherfirebaseapp.ui.viewmodel.FavoritesViewModel

@Composable
fun FavoritesScreen(viewModel: FavoritesViewModel) {
    val favorites by viewModel.favorites.collectAsState()

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(
            text = "My Favorite Cities",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        if (favorites.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = "No favorites added yet")
            }
        } else {
            LazyColumn {
                items(favorites) { city ->
                    FavoriteCityItem(
                        city = city,
                        onDelete = { viewModel.deleteFavorite(city.id ?: "") }
                    )
                }
            }
        }
    }
}

@Composable
fun FavoriteCityItem(city: FavoriteDto, onDelete: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = city.cityName ?: "Unknown", style = MaterialTheme.typography.titleLarge)
                if (!city.note.isNullOrBlank()) {
                    Text(text = city.note, style = MaterialTheme.typography.bodyMedium)
                }
            }
            IconButton(onClick = onDelete) {
                Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete City")
            }
        }
    }
}