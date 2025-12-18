package com.example.delivery_20.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.delivery_20.model.Restaurant
import com.example.delivery_20.model.FoodItem
import com.example.delivery_20.viewmodel.CartViewModel
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RestaurantDetailScreen(
    navController: NavHostController,
    restaurantId: String
) {
    // ViewModel del carrito
    val cartViewModel: CartViewModel = viewModel()

    // DEBUG: Observar estado del carrito
    val cartItems by cartViewModel.cartItems.collectAsState()
    val cartSize = cartItems.size
    val totalItems = cartItems.sumOf { it.quantity }

    // Determinar qué restaurante es según el ID
    val (restaurant, foodItems) = remember(restaurantId) {
        when (restaurantId) {
            "1" -> Pair(
                Restaurant(
                    id = "1",
                    name = "Pizzería Italiana",
                    description = "Auténtica pizza italiana hecha en horno de leña",
                    rating = 4.8f,
                    deliveryTime = "25-35 min",
                    category = "Italiana"
                ),
                getItalianRestaurantMenu()
            )
            "2" -> Pair(
                Restaurant(
                    id = "2",
                    name = "Burger House",
                    description = "Hamburguesas gourmet con ingredientes premium",
                    rating = 4.6f,
                    deliveryTime = "20-30 min",
                    category = "Americana"
                ),
                getBurgerRestaurantMenu()
            )
            "3" -> Pair(
                Restaurant(
                    id = "3",
                    name = "Sushi Zen",
                    description = "Sushi fresco preparado por chefs japoneses",
                    rating = 4.9f,
                    deliveryTime = "30-40 min",
                    category = "Japonesa"
                ),
                getSushiRestaurantMenu()
            )
            "4" -> Pair(
                Restaurant(
                    id = "4",
                    name = "Tacos México",
                    description = "Auténtica comida mexicana con sabores tradicionales",
                    rating = 4.7f,
                    deliveryTime = "15-25 min",
                    category = "Mexicana"
                ),
                getMexicanRestaurantMenu()
            )
            "5" -> Pair(
                Restaurant(
                    id = "5",
                    name = "Dulce Tentación",
                    description = "Postres y pasteles artesanales hechos diariamente",
                    rating = 4.5f,
                    deliveryTime = "20-30 min",
                    category = "Postres"
                ),
                getDessertRestaurantMenu()
            )
            else -> Pair(
                Restaurant(
                    id = restaurantId,
                    name = "Restaurante Ejemplo",
                    description = "Comida deliciosa y rápida",
                    rating = 4.0f,
                    deliveryTime = "30-40 min",
                    category = "General"
                ),
                emptyList()
            )
        }
    }
    Button(
        onClick = { navController.navigate("camera") },
        modifier = Modifier.padding(16.dp)
    ) {
        Icon(Icons.Default.Camera, contentDescription = "Tomar foto")
        Spacer(modifier = Modifier.width(8.dp))
        Text("Tomar foto del pedido")
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(restaurant.name)
                        // DEBUG: Mostrar info del carrito
                        Text(
                            "🛒 Carrito: $totalItems items",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Regresar"
                        )
                    }
                },
                actions = {
                    // Badge del carrito
                    BadgedBox(
                        badge = {
                            if (totalItems > 0) {
                                Badge {
                                    Text(totalItems.toString())
                                }
                            }
                        }
                    ) {
                        IconButton(onClick = { navController.navigate("cart") }) {
                            Icon(
                                Icons.Default.ShoppingCart,
                                contentDescription = "Carrito"
                            )
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // DEBUG: Panel informativo (temporal)
            if (totalItems > 0) {
                Surface(
                    color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "✅ Productos en carrito: $totalItems",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                        TextButton(
                            onClick = { navController.navigate("cart") },
                            modifier = Modifier.height(32.dp)
                        ) {
                            Text("Ver carrito")
                        }
                    }
                }
            }

            // Imagen del restaurante
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                color = MaterialTheme.colorScheme.primaryContainer
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.RestaurantMenu,
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
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = "Rating",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = String.format("%.1f", restaurant.rating),
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

                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
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
                text = "Menú (${foodItems.size} productos)",
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
                        FoodItemCard(
                            foodItem = foodItem,
                            cartViewModel = cartViewModel,
                            isInCart = cartItems.any { it.foodItem.id == foodItem.id }
                        )
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

// Funciones para obtener los menús específicos de cada restaurante (mantener igual)
private fun getItalianRestaurantMenu(): List<FoodItem> {
    return listOf(
        FoodItem("i1", "Pizza Margarita", "Queso mozzarella y tomate fresco", 8990, "1"),
        FoodItem("i2", "Pizza Pepperoni", "Pepperoni picante y queso derretido", 10990, "1"),
        FoodItem("i3", "Lasagna Clásica", "Pasta con carne y salsa bechamel", 7990, "1"),
        FoodItem("i4", "Ravioli de Ricotta", "Ravioli relleno de ricota y espinaca", 8490, "1"),
        FoodItem("i5", "Espagueti Carbonara", "Con salsa cremosa y tocino", 7490, "1"),
        FoodItem("i6", "Calzone", "Pizza cerrada rellena de jamón y queso", 9490, "1"),
        FoodItem("i7", "Tiramisú", "Postre italiano con café y cacao", 3990, "1")
    )
}

private fun getBurgerRestaurantMenu(): List<FoodItem> {
    return listOf(
        FoodItem("b1", "Burger Clásica", "Carne 200g, lechuga, tomate, queso", 6990, "2"),
        FoodItem("b2", "Burger BBQ", "Con salsa barbacoa y cebolla crispy", 7990, "2"),
        FoodItem("b3", "Burger Doble Queso", "Doble carne y doble queso cheddar", 8990, "2"),
        FoodItem("b4", "Burger Vegetariana", "Medallón de garbanzos y vegetales", 7490, "2"),
        FoodItem("b5", "Papas Fritas", "Porción grande con salsa a elección", 2990, "2"),
        FoodItem("b6", "Aros de Cebolla", "Rebozados y crujientes", 3490, "2"),
        FoodItem("b7", "Milkshake Chocolate", "Batido cremoso de chocolate", 3990, "2")
    )
}

private fun getSushiRestaurantMenu(): List<FoodItem> {
    return listOf(
        FoodItem("s1", "Roll California", "Cangrejo, palta y pepino", 7990, "3"),
        FoodItem("s2", "Roll Philadelphia", "Salmón, queso crema y palta", 8990, "3"),
        FoodItem("s3", "Roll Tempura", "Camarón tempura y salsa agridulce", 9490, "3"),
        FoodItem("s4", "Sashimi Salmón", "8 piezas de salmón fresco", 11990, "3"),
        FoodItem("s5", "Nigiri Variado", "6 piezas de pescado variado", 10990, "3"),
        FoodItem("s6", "Sushi Mixto", "Combinación de 20 piezas", 15990, "3"),
        FoodItem("s7", "Mochi Helado", "Postre japonés de helado", 2990, "3")
    )
}

private fun getMexicanRestaurantMenu(): List<FoodItem> {
    return listOf(
        FoodItem("m1", "Tacos al Pastor", "3 tacos con carne adobada", 6990, "4"),
        FoodItem("m2", "Burrito Gigante", "Carne, frijoles, arroz y guacamole", 8490, "4"),
        FoodItem("m3", "Quesadillas", "4 quesadillas con queso derretido", 5990, "4"),
        FoodItem("m4", "Nachos Supreme", "Con carne, queso, guacamole", 7990, "4"),
        FoodItem("m5", "Enchiladas Verdes", "3 enchiladas con pollo", 7490, "4"),
        FoodItem("m6", "Chiles Rellenos", "Chiles poblanos rellenos", 8990, "4"),
        FoodItem("m7", "Flan Casero", "Postre tradicional mexicano", 3490, "4")
    )
}

private fun getDessertRestaurantMenu(): List<FoodItem> {
    return listOf(
        FoodItem("d1", "Cheesecake de Frutilla", "Tarta de queso con salsa de frutilla", 4990, "5"),
        FoodItem("d2", "Brownie con Helado", "Brownie caliente con helado de vainilla", 5990, "5"),
        FoodItem("d3", "Tres Leches", "Bizcocho humedecido en tres leches", 4490, "5"),
        FoodItem("d4", "Crepes Nutella", "Crepes rellenos de Nutella y frutas", 5490, "5"),
        FoodItem("d5", "Profiteroles", "Bolitas de hojaldre con crema", 3990, "5"),
        FoodItem("d6", "Macarons (6 und)", "Galletas francesas de almendra", 6990, "5"),
        FoodItem("d7", "Mousse de Chocolate", "Espumoso y delicioso", 3990, "5")
    )
}

@Composable
fun FoodItemCard(
    foodItem: FoodItem,
    cartViewModel: CartViewModel,
    isInCart: Boolean = false
) {
    var isAdded by remember { mutableStateOf(false) }
    var currentIsInCart by remember { mutableStateOf(isInCart) }

    // Actualizar cuando cambie el estado del carrito
    LaunchedEffect(isInCart) {
        currentIsInCart = isInCart
    }

    // LaunchedEffect para resetear el estado "Agregado"
    LaunchedEffect(isAdded) {
        if (isAdded) {
            delay(2000)
            isAdded = false
        }
    }

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
            Column(modifier = Modifier.weight(1f)) {
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
                    text = formatChileanPrice(foodItem.price),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )

                // DEBUG: Mostrar si ya está en el carrito
                if (currentIsInCart) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        "✅ Ya en el carrito",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.Green
                    )
                }
            }

            Button(
                onClick = {
                    // DEBUG: Log antes de agregar
                    println("🛒 DEBUG: Botón presionado - ${foodItem.name}")
                    cartViewModel.addToCart(foodItem)
                    isAdded = true
                    currentIsInCart = true
                },
                modifier = Modifier.padding(start = 16.dp),
                enabled = !currentIsInCart, // Deshabilitar si ya está en carrito
                colors = ButtonDefaults.buttonColors(
                    containerColor = when {
                        currentIsInCart -> MaterialTheme.colorScheme.tertiaryContainer
                        isAdded -> MaterialTheme.colorScheme.tertiary
                        else -> MaterialTheme.colorScheme.primary
                    }
                )
            ) {
                if (currentIsInCart) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "En carrito",
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("En carrito")
                } else if (isAdded) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Agregado",
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Agregado")
                } else {
                    Icon(
                        imageVector = Icons.Default.AddShoppingCart,
                        contentDescription = "Agregar al carrito",
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Agregar")
                }
            }
        }
    }
}

// Función para formatear precios en formato chileno
private fun formatChileanPrice(price: Int): String {
    return if (price >= 1000) {
        "$${String.format("%,d", price)}"
    } else {
        "$$price"
    }
}