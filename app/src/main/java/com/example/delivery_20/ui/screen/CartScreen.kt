package com.example.delivery_20.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.delivery_20.ui.components.buttons.PrimaryButton
import com.example.delivery_20.ui.components.buttons.SecondaryButton

@Composable
fun CartScreen(
    navController: NavHostController
) {
    // Datos de ejemplo para el carrito
    val cartItems = remember {
        listOf(
            CartItem(
                foodItem = FoodItem(
                    id = "1",
                    name = "Pizza Margarita",
                    description = "Queso y tomate",
                    price = 12.99,
                    restaurantId = "1"
                ),
                quantity = 1
            ),
            CartItem(
                foodItem = FoodItem(
                    id = "2",
                    name = "Hamburguesa Clásica",
                    description = "Carne y queso",
                    price = 9.99,
                    restaurantId = "2"
                ),
                quantity = 2
            )
        )
    }

    val total = remember(cartItems) {
        cartItems.sumOf { it.foodItem.price * it.quantity }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        if (cartItems.isNotEmpty()) {
            // Título
            Text(
                text = "Tu Carrito",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Lista de items
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(cartItems) { cartItem ->
                    CartItemCard(cartItem = cartItem)
                }
            }

            // Resumen y botones
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Resumen
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Subtotal")
                        Text("$${String.format("%.2f", total)}")
                    }

                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Envío")
                        Text("$2.99")
                    }

                    Divider()

                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Total",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "$${String.format("%.2f", total + 2.99)}",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                // Botones
                PrimaryButton(
                    text = "Continuar con el pago",
                    onClick = { /* TODO: Ir a pago */ },
                    modifier = Modifier.fillMaxWidth()
                )

                SecondaryButton(
                    text = "Seguir comprando",
                    onClick = { navController.popBackStack() },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        } else {
            // Carrito vacío
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.ShoppingCart,
                    contentDescription = "Carrito vacío",
                    modifier = Modifier.size(80.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Tu carrito está vacío",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Medium
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Agrega algunos productos deliciosos",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(24.dp))

                PrimaryButton(
                    text = "Explorar restaurantes",
                    onClick = { navController.navigate("home") }
                )
            }
        }
    }
}

// Modelos temporales
data class FoodItem(
    val id: String,
    val name: String,
    val description: String,
    val price: Double,
    val restaurantId: String
)

data class CartItem(
    val foodItem: FoodItem,
    var quantity: Int
)

@Composable
fun CartItemCard(cartItem: CartItem) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = cartItem.foodItem.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "$${String.format("%.2f", cartItem.foodItem.price)} c/u",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Contador de cantidad
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                IconButton(
                    onClick = { /* Decrementar */ },
                    modifier = Modifier.size(32.dp)
                ) {
                    Text("-", style = MaterialTheme.typography.titleMedium)
                }

                Text(
                    text = cartItem.quantity.toString(),
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.width(24.dp),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )

                IconButton(
                    onClick = { /* Incrementar */ },
                    modifier = Modifier.size(32.dp)
                ) {
                    Text("+", style = MaterialTheme.typography.titleMedium)
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Botón eliminar
            IconButton(onClick = { /* Eliminar */ }) {
                Icon(Icons.Default.Delete, contentDescription = "Eliminar")
            }
        }
    }
}