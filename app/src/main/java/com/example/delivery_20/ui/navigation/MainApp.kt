package com.example.delivery_20.ui.navigation

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.delivery_20.screen.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.CoroutineScope

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainApp() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()

    // ✅ ACTUALIZADO: Drawer items con todas las rutas
    val drawerItems = listOf(
        DrawerItem("Inicio", Icons.Default.Home, Screen.Home.route),
        DrawerItem("Buscar", Icons.Default.Search, Screen.Search.route),
        DrawerItem("Historial", Icons.Default.History, Screen.History.route),
        DrawerItem("Ayuda", Icons.Default.Help, Screen.Help.route),
        DrawerItem("Configuración", Icons.Default.Settings, Screen.Settings.route)
    )

    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = true,
        drawerContent = {
            ModalDrawerSheet(
                modifier = Modifier.width(280.dp)
            ) {
                DrawerHeader()

                drawerItems.forEach { item ->
                    NavigationDrawerItem(
                        icon = { Icon(item.icon, contentDescription = item.title) },
                        label = { Text(item.title) },
                        selected = currentDestination?.route == item.route,
                        onClick = {
                            coroutineScope.launch { drawerState.close() }
                            if (item.route == Screen.Home.route) {
                                navController.navigate(Screen.Home.route) {
                                    popUpTo(Screen.Home.route) { inclusive = true }
                                }
                            } else {
                                navController.navigate(item.route)
                            }
                        },
                        modifier = Modifier.padding(horizontal = 12.dp)
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                DrawerFooter(navController, drawerState, coroutineScope)
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBarWithMenu(
                    title = getScreenTitle(currentDestination),
                    navController = navController,
                    onMenuClick = { coroutineScope.launch { drawerState.open() } },
                    showBackButton = currentDestination?.route != Screen.Home.route,
                    onBackClick = { navController.popBackStack() }
                )
            },
            bottomBar = {
                if (shouldShowBottomBar(currentDestination)) {
                    BottomNavigationBar(navController, currentDestination)
                }
            }
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                AppNavigation(
                    navController = navController,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}

@Composable
fun DrawerHeader() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 24.dp, horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Surface(
            modifier = Modifier.size(80.dp),
            shape = MaterialTheme.shapes.extraLarge,
            color = MaterialTheme.colorScheme.primary
        ) {
            Box(
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.RestaurantMenu,
                    contentDescription = "Food Delivery",
                    modifier = Modifier.size(40.dp),
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Food Delivery",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        Text(
            text = "Pedidos rápidos y deliciosos",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun DrawerFooter(
    navController: NavHostController,
    drawerState: DrawerState,
    coroutineScope: CoroutineScope
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Divider(modifier = Modifier.padding(bottom = 16.dp))

        NavigationDrawerItem(
            icon = { Icon(Icons.Default.Person, contentDescription = "Perfil") },
            label = { Text("Mi Perfil") },
            selected = false,
            onClick = {
                coroutineScope.launch {
                    drawerState.close()
                }
                navController.navigate(Screen.Profile.route)
            },
            modifier = Modifier.padding(horizontal = 4.dp)
        )

        NavigationDrawerItem(
            icon = { Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = "Cerrar sesión") },
            label = { Text("Cerrar sesión") },
            selected = false,
            onClick = {
                coroutineScope.launch {
                    drawerState.close()
                }
                navController.navigate(Screen.Home.route) {
                    popUpTo(Screen.Home.route) { inclusive = true }
                }
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBarWithMenu(
    title: String,
    navController: NavHostController,
    onMenuClick: () -> Unit,
    showBackButton: Boolean = false,
    onBackClick: () -> Unit = {}
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = title,
                fontWeight = FontWeight.Medium
            )
        },
        navigationIcon = {
            if (showBackButton) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Volver"
                    )
                }
            } else {
                IconButton(onClick = onMenuClick) {
                    Icon(Icons.Default.Menu, contentDescription = "Menú")
                }
            }
        },
        actions = {
            IconButton(onClick = { navController.navigate(Screen.Cart.route) }) {
                Icon(Icons.Default.ShoppingCart, contentDescription = "Carrito")
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    )
}

@Composable
fun BottomNavigationBar(
    navController: NavHostController,
    currentDestination: NavDestination?
) {
    NavigationBar {
        val bottomNavItems = listOf(
            BottomNavItem("Inicio", Icons.Default.Home, Screen.Home.route),
            BottomNavItem("Carrito", Icons.Default.ShoppingCart, Screen.Cart.route),
            BottomNavItem("Perfil", Icons.Default.Person, Screen.Profile.route)
        )

        bottomNavItems.forEach { item ->
            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.title
                    )
                },
                label = { Text(item.title) },
                selected = currentDestination?.hierarchy?.any { it.route == item.route } == true,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}

fun shouldShowBottomBar(currentDestination: NavDestination?): Boolean {
    val noBottomBarRoutes = listOf(
        Screen.Login.route,
        Screen.Register.route,
        Screen.EditProfile.route,
        Screen.OrderConfirmation.route,
        Screen.Camera.route,
        Screen.Location.route,
        Screen.Search.route,
        Screen.History.route,
        Screen.Settings.route,
        Screen.Help.route
    )

    return currentDestination?.route !in noBottomBarRoutes
}

fun getScreenTitle(currentDestination: NavDestination?): String {
    return when (currentDestination?.route) {
        Screen.Home.route -> "Food Delivery"
        Screen.RestaurantDetail.route -> "Restaurante"
        Screen.Cart.route -> "Carrito"
        Screen.Profile.route -> "Mi Perfil"
        Screen.Login.route -> "Iniciar Sesión"
        Screen.Register.route -> "Crear Cuenta"
        Screen.Search.route -> "Buscar"
        Screen.History.route -> "Historial"
        Screen.Settings.route -> "Configuración"
        Screen.Help.route -> "Ayuda"
        Screen.OrderConfirmation.route -> "Confirmación"
        Screen.Camera.route -> "Cámara"
        Screen.Location.route -> "Ubicación"
        Screen.EditProfile.route -> "Editar Perfil"
        else -> "Food Delivery"
    }
}

data class DrawerItem(
    val title: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
    val route: String
)

data class BottomNavItem(
    val title: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
    val route: String
)