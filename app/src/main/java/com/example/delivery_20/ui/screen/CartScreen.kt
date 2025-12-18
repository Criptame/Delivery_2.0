package com.example.delivery_20.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.delivery_20.model.CartItem
import com.example.delivery_20.viewmodel.CartViewModel

@Composable
fun CartScreen(
    navController: NavHostController,
    cartViewModel: CartViewModel // ← Recibir como parámetro (NO viewModel() aquí)
) {
    // Observar estado del carrito
    val cartItems by cartViewModel.cartItems.collectAsState()
    val cartTotal by cartViewModel.cartTotal.collectAsState()

    // Debug para verificar
    println("🛒 CartScreen - Items en carrito: ${cartItems.size}")
    cartItems.forEachIndexed { index, item ->
        println("🛒 CartScreen - [$index] ${item.foodItem.name}: ${item.quantity}")
    }

    // Costo de envío fijo
    val shippingCost = 2000 // $2.000 CLP
    val finalTotal = cartTotal + shippingCost

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        if (cartItems.isNotEmpty()) {
            // Título
            Text(
                text = "Tu Carrito (${cartItems.size} ${if (cartItems.size == 1) "producto" else "productos"})",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Lista de items
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(cartItems) { cartItem ->
                    CartItemCard(
                        cartItem = cartItem,
                        onIncrement = { cartViewModel.incrementQuantity(cartItem.foodItem.id) },
                        onDecrement = { cartViewModel.decrementQuantity(cartItem.foodItem.id) },
                        onRemove = { cartViewModel.removeItem(cartItem.foodItem.id) }
                    )
                }
            }

            // Resumen y botones
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Resumen
                Surface(
                    shape = MaterialTheme.shapes.medium,
                    tonalElevation = 2.dp,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Subtotal", style = MaterialTheme.typography.bodyMedium)
                            Text(formatPrice(cartTotal), style = MaterialTheme.typography.bodyMedium)
                        }

                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Costo de envío", style = MaterialTheme.typography.bodyMedium)
                            Text(formatPrice(shippingCost), style = MaterialTheme.typography.bodyMedium)
                        }

                        Divider(modifier = Modifier.padding(vertical = 8.dp))

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
                                text = formatPrice(finalTotal),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }

                // Botones
                Button(
                    onClick = {
                        // Navegar a la pantalla de confirmación
                        navController.navigate("order_confirmation")
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Text("Continuar con el pago")
                }


                OutlinedButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Seguir comprando")
                }

                TextButton(
                    onClick = { cartViewModel.clearCart() },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Vaciar carrito", color = MaterialTheme.colorScheme.error)
                }
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
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = { navController.navigate("home") },
                    modifier = Modifier.width(200.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Text("Explorar restaurantes")
                }
            }
        }
    }
}

@Composable
fun CartItemCard(
    cartItem: CartItem,
    onIncrement: () -> Unit,
    onDecrement: () -> Unit,
    onRemove: () -> Unit
) {
    val itemTotal = cartItem.foodItem.price * cartItem.quantity

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
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
                    text = cartItem.foodItem.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "${formatPrice(cartItem.foodItem.price)} c/u",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Contador de cantidad
            Surface(
                shape = MaterialTheme.shapes.small,
                tonalElevation = 1.dp
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    IconButton(
                        onClick = onDecrement,
                        modifier = Modifier.size(28.dp)
                    ) {
                        Icon(
                            Icons.Default.Remove,
                            contentDescription = "Disminuir",
                            modifier = Modifier.size(16.dp)
                        )
                    }

                    Text(
                        text = cartItem.quantity.toString(),
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.width(24.dp),
                        textAlign = TextAlign.Center
                    )

                    IconButton(
                        onClick = onIncrement,
                        modifier = Modifier.size(28.dp)
                    ) {
                        Icon(
                            Icons.Default.Add,
                            contentDescription = "Aumentar",
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Total del item y botón eliminar
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = formatPrice(itemTotal),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(4.dp))

                IconButton(
                    onClick = onRemove,
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "Eliminar",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}

// Función para formatear precios en formato chileno
private fun formatPrice(price: Int): String {
    return if (price >= 1000) {
        "$${String.format("%,d", price)}"
    } else {
        "$$price"
    }
}