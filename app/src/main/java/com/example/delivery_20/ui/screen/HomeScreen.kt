package com.example.delivery_20.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.delivery_20.model.Restaurant
import com.example.delivery_20.ui.components.cards.RestaurantCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavHostController
) {
    // SOLUCIÓN 1: Reemplazar la llamada a función que no existe
    val restaurants = remember {
        listOf(
            Restaurant("1", "Pizzería Italiana", "Auténtica pizza italiana", 4.8f, "25-35 min", "Italiana"),
            Restaurant("2", "Burger House", "Hamburguesas gourmet", 4.6f, "20-30 min", "Americana"),
            Restaurant("3", "Sushi Zen", "Sushi fresco japonés", 4.9f, "30-40 min", "Japonesa"),
            Restaurant("4", "Tacos México", "Comida mexicana auténtica", 4.7f, "15-25 min", "Mexicana"),
            Restaurant("5", "Dulce Tentación", "Postres artesanales", 4.5f, "20-30 min", "Postres")
        )
    }

    var searchText by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(8.dp))

        // Título de bienvenida
        Text(
            text = "¡Buen provecho! 👨‍🍳",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "¿Qué deseas ordenar hoy?",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Barra de búsqueda
        OutlinedTextField(
            value = searchText,
            onValueChange = { searchText = it },
            modifier = Modifier.fillMaxWidth(),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Buscar"
                )
            },
            placeholder = { Text("Buscar restaurantes...") },
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline
            )
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Sección de categorías rápidas
        Text(
            text = "Categorías populares",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Chips de categorías
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(bottom = 16.dp)
        ) {
            listOf("Todos", "Pizza", "Hamburguesas", "Sushi"
                , "Mexicana",
                "Postres").forEach { category ->
                FilterChip(
                    selected = category == "Todos",
                    onClick = { /* Filtrar por categoría */ },
                    label = { Text(category) }
                )
            }
        }

        Text(
            text = "Restaurantes cerca de ti",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(16.dp))

        // SOLUCIÓN 2: Verificar que la lista no esté vacía (sin .isNotEmpty())
        if (restaurants.any()) {  // Cambiado de isNotEmpty() a any()
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(restaurants) { restaurant ->
                    RestaurantCard(
                        restaurant = restaurant,
                        onClick = {
                            navController.navigate(
                                "restaurant_detail/${restaurant.id}"
                            )
                        }
                    )
                }
            }
        } else {
            // Estado vacío
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "No hay restaurantes disponibles",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}