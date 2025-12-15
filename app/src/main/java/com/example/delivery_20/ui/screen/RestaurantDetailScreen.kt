package com.example.delivery_20.screen

import androidx.compose.foundation.layout.*
import com.example.delivery_20.model.Restaurant
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RestaurantDetailScreen(
    navController: NavHostController,
    restaurantId: String
) {
    // Datos de ejemplo directamente
    val restaurant = remember {
        Restaurant(
            id = restaurantId,
            name = "Restaurante Ejemplo",
            description = "Comida deliciosa y rápida",
            rating = 4.5f,
            deliveryTime = "30-40 min",
            category = "Italiana"
        )
    }

    val foodItems = remember {
        listOf(
            FoodItem("1", "Pizza Margarita", "Queso y tomate", 12.99, restaurantId),
            FoodItem("2", "Pizza Pepperoni", "Pepperoni y queso", 14.99, restaurantId),
            FoodItem("3", "Lasagna", "Pasta con carne", 11.99, restaurantId)
        )
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("cart") },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(Icons.Default.ShoppingCart, contentDescription = "Carrito")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Imagen del restaurante (placeholder)
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                color = MaterialTheme.colorScheme.primaryContainer
            ) {
                Box(
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Restaurant,
                        contentDescription = "Restaurante",
                        modifier = Modifier.size(80.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }

            // Encabezado del restaurante
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = restaurant.name,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = "Rating",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = restaurant.rating.toString(),
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = restaurant.description,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Surface(
                        color = MaterialTheme.colorScheme.secondaryContainer,
                        shape = MaterialTheme.shapes.small
                    ) {
                        Text(
                            text = "⏱️ ${restaurant.deliveryTime}",
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            style = MaterialTheme.typography.labelMedium
                        )
                    }

                    Surface(
                        color = MaterialTheme.colorScheme.tertiaryContainer,
                        shape = MaterialTheme.shapes.small
                    ) {
                        Text(
                            text = restaurant.category,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            style = MaterialTheme.typography.labelMedium
                        )
                    }
                }
            }

            Divider(modifier = Modifier.padding(horizontal = 16.dp))

            // Menú del restaurante
            Text(
                text = "Menú",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(16.dp)
            )

            if (foodItems.isNotEmpty()) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    items(foodItems) { foodItem ->
                        FoodItemCard(foodItem = foodItem)
                    }
                }
            } else {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text("No hay elementos en el menú")
                }
            }
        }
    }
}

@Composable
fun FoodItemCard(foodItem: FoodItem) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = foodItem.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = foodItem.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "$${String.format("%.2f", foodItem.price)}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Button(
                onClick = { /* TODO: Agregar al carrito */ },
                modifier = Modifier.padding(start = 16.dp)
            ) {
                Text("Agregar")
            }
        }
    }
}