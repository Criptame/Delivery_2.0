package com.example.delivery_20.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(navController: NavHostController) {
    var searchQuery by remember { mutableStateOf("") }
    var searchType by remember { mutableStateOf("Todos") }

    Column(modifier = Modifier.fillMaxSize()) {
        // Barra de búsqueda
        Surface(tonalElevation = 3.dp) {
            Column(modifier = Modifier.padding(16.dp)) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    modifier = Modifier.fillMaxWidth(),
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Buscar") },
                    placeholder = { Text("Buscar restaurantes o productos...") },
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Filtros
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf("Todos", "Restaurantes", "Productos").forEach { type ->
                        FilterChip(
                            selected = searchType == type,
                            onClick = { searchType = type },
                            label = { Text(type) }
                        )
                    }
                }
            }
        }
    }
}