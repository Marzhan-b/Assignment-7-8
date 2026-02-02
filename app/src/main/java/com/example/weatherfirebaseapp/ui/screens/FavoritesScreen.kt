package com.example.weatherfirebaseapp.ui.screens

import androidx.compose.foundation.clickable
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.*



@Composable
fun FavoritesScreen(
    viewModel: FavoritesViewModel,
    onCityClick: (String) -> Unit
) {
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
                        onDelete = { viewModel.deleteFavorite(city.id ?: "") },
                        onCityClick = onCityClick,
                        viewModel = viewModel
                    )
                }
            }
        }
    }
}

@Composable
fun FavoriteCityItem(
    city: FavoriteDto,
    onDelete: () -> Unit,
    onCityClick: (String) -> Unit,
    viewModel: FavoritesViewModel
) {
    var showEditDialog by remember { mutableStateOf(false) }
    var editedNote by remember { mutableStateOf(city.note ?: "") }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            Text(
                text = city.cityName ?: "Unknown",
                style = MaterialTheme.typography.titleLarge
            )

            if (!city.note.isNullOrBlank()) {
                Text(
                    text = city.note,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Row {
                    Button(
                        onClick = {
                            onCityClick(city.cityName ?: "")
                        }
                    ) {
                        Text("Weather")
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    OutlinedButton(
                        onClick = {
                            editedNote = city.note ?: ""
                            showEditDialog = true
                        }
                    ) {
                        Text("Edit note")
                    }
                }

                IconButton(onClick = onDelete) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete City"
                    )
                }
            }
        }
    }
    if (showEditDialog) {
        AlertDialog(
            onDismissRequest = { showEditDialog = false },
            title = { Text("Edit note") },
            text = {
                OutlinedTextField(
                    value = editedNote,
                    onValueChange = { editedNote = it },
                    label = { Text("Note") },
                    singleLine = true
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        city.id?.let {
                            viewModel.updateFavoriteNote(it, editedNote)
                        }
                        showEditDialog = false
                    }
                ) {
                    Text("Save")
                }
            },
            dismissButton = {
                TextButton(onClick = { showEditDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}
