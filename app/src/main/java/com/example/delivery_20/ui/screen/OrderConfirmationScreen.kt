package com.example.delivery_20.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.delivery_20.model.CartItem
import com.example.delivery_20.viewmodel.CartViewModel
import kotlin.random.Random
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderConfirmationScreen(
    navController: NavHostController,
    cartViewModel: CartViewModel
) {
    // Obtener datos del carrito
    val cartItems by cartViewModel.cartItems.collectAsState()
    val cartTotal by cartViewModel.cartTotal.collectAsState()

    // Datos aleatorios para el pedido
    val orderId = remember { generateOrderId() }
    val deliveryPerson = remember { getRandomDeliveryPerson() }
    val estimatedTime = remember { getRandomDeliveryTime() }
    val orderDate = remember { getCurrentDate() }

    // Costos
    val shippingCost = 2000
    val finalTotal = cartTotal + shippingCost

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Confirmación de Pedido") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            // Encabezado de confirmación
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "Confirmado",
                        modifier = Modifier.size(60.dp),
                        tint = Color.Green
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "¡Pedido Confirmado!",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Text(
                        text = "ID: #$orderId",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Información del repartidor
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Tu repartidor",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.SemiBold
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Surface(
                            modifier = Modifier.size(50.dp),
                            shape = MaterialTheme.shapes.extraLarge,
                            color = MaterialTheme.colorScheme.secondary
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = deliveryPerson.first().toString(),
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSecondary
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        Column {
                            Text(
                                text = deliveryPerson,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Medium
                            )
                            Text(
                                text = "En camino a tu ubicación",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column {
                            Text(
                                "Tiempo estimado",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                estimatedTime,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Surface(
                            color = MaterialTheme.colorScheme.tertiaryContainer,
                            shape = MaterialTheme.shapes.small
                        ) {
                            Text(
                                "🛵 En camino",
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                style = MaterialTheme.typography.labelMedium
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Detalles del pedido
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Detalles del pedido",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    // Productos
                    cartItems.forEachIndexed { index, item ->
                        OrderItemRow(item = item, isLast = index == cartItems.size - 1)
                    }

                    Divider(modifier = Modifier.padding(vertical = 12.dp))

                    // Resumen de costos
                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        CostRow(label = "Subtotal", value = formatPrice(cartTotal))
                        CostRow(label = "Costo de envío", value = formatPrice(shippingCost))
                        CostRow(
                            label = "Total",
                            value = formatPrice(finalTotal),
                            isTotal = true
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Información adicional
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    // ✅ CAMBIADO: InfoRow -> OrderInfoRow
                    OrderInfoRow(
                        icon = Icons.Default.CalendarToday,
                        text = "Fecha: $orderDate"
                    )
                    OrderInfoRow(
                        icon = Icons.Default.Payment,
                        text = "Método: Tarjeta de crédito"
                    )
                    OrderInfoRow(
                        icon = Icons.Default.LocationOn,
                        text = "Tipo: Entrega a domicilio"
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Botones
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = {
                        // Limpiar carrito y volver al inicio
                        cartViewModel.clearCart()
                        navController.navigate("home") {
                            popUpTo("home") { inclusive = true }
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Icon(Icons.Default.Home, contentDescription = null, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Volver al inicio")
                }

                OutlinedButton(
                    onClick = { navController.navigate("history") },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.History, contentDescription = null, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Ver historial de pedidos")
                }
            }
        }
    }
}

@Composable
fun OrderItemRow(item: CartItem, isLast: Boolean = false) {
    Column {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column {
                Text(
                    text = item.foodItem.name,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = "${item.quantity} x ${formatPrice(item.foodItem.price)}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Text(
                text = formatPrice(item.foodItem.price * item.quantity),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium
            )
        }

        if (!isLast) {
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun CostRow(label: String, value: String, isTotal: Boolean = false) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = label,
            style = if (isTotal) MaterialTheme.typography.titleMedium else MaterialTheme.typography.bodyMedium,
            fontWeight = if (isTotal) FontWeight.Bold else FontWeight.Normal
        )
        Text(
            text = value,
            style = if (isTotal) MaterialTheme.typography.titleMedium else MaterialTheme.typography.bodyMedium,
            fontWeight = if (isTotal) FontWeight.Bold else FontWeight.Normal,
            color = if (isTotal) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
        )
    }
}

// ✅ CAMBIADO: InfoRow -> OrderInfoRow (para evitar conflictos)
@Composable
fun OrderInfoRow(icon: ImageVector, text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 4.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(20.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

// Función para formatear precios
private fun formatPrice(price: Int): String {
    return if (price >= 1000) {
        "$${String.format("%,d", price)}"
    } else {
        "$$price"
    }
}

// Función para generar ID de pedido
private fun generateOrderId(): String {
    val letters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
    val numbers = "0123456789"

    val random = Random(System.currentTimeMillis())
    val letterPart = (1..3).map { letters[random.nextInt(letters.length)] }.joinToString("")
    val numberPart = (1..6).map { numbers[random.nextInt(numbers.length)] }.joinToString("")

    return "$letterPart$numberPart"
}

// Lista de repartidores aleatorios
private val deliveryPeople = listOf(
    "Carlos González",
    "Ana Martínez",
    "Luis Rodríguez",
    "María López",
    "Pedro Sánchez",
    "Laura García",
    "Javier Fernández",
    "Sofía Pérez",
    "Miguel Gómez",
    "Elena Díaz"
)

// Función para obtener repartidor aleatorio
private fun getRandomDeliveryPerson(): String {
    val random = Random(System.currentTimeMillis())
    return deliveryPeople[random.nextInt(deliveryPeople.size)]
}

// Función para obtener tiempo de entrega aleatorio
private fun getRandomDeliveryTime(): String {
    val times = listOf(
        "25-35 min",
        "20-30 min",
        "30-40 min",
        "15-25 min",
        "35-45 min",
        "40-50 min"
    )
    val random = Random(System.currentTimeMillis())
    return times[random.nextInt(times.size)]
}

// Función para obtener fecha actual
private fun getCurrentDate(): String {
    val formatter = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
    return formatter.format(Date())
}