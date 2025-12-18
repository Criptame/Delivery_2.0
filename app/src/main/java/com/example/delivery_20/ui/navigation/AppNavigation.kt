package com.example.delivery_20.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.delivery_20.screen.*
import com.example.delivery_20.viewmodel.CartViewModel

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object RestaurantDetail : Screen("restaurant_detail/{restaurantId}")
    object Cart : Screen("cart")
    object Profile : Screen("profile")
    object Camera : Screen("camera")
    object Location : Screen("location")
    object Login : Screen("login")
    object Register : Screen("register")
}

@Composable
fun AppNavigation(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = Screen.Home.route
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        // 1. Home
        composable(Screen.Home.route) {
            HomeScreen(navController = navController)
        }

        // 2. Detalle Restaurante (CON cartViewModel)
        composable(Screen.RestaurantDetail.route) { backStackEntry ->
            val restaurantId = backStackEntry.arguments?.getString("restaurantId") ?: ""
            RestaurantDetailScreen(
                navController = navController,
                restaurantId = restaurantId,
                cartViewModel = viewModel()  // ← ¡AGREGA ESTO!
            )
        }

        // 3. Carrito (CON cartViewModel)
        composable(Screen.Cart.route) {
            CartScreen(
                navController = navController,
                cartViewModel = viewModel()  // ← ¡AGREGA ESTO!
            )
        }

        // 4. Perfil
        composable(Screen.Profile.route) {
            ProfileScreen(navController = navController)
        }

        // 5. Cámara (SIN cartViewModel)
        composable(Screen.Camera.route) {
            CameraScreen(
                navController = navController,
                onPhotoTaken = { uri ->
                    println("📸 Foto: $uri")
                }
            )
        }

        // 6. GPS
        composable(Screen.Location.route) {
            LocationScreen(navController = navController)
        }

        // 7. Login
        composable(Screen.Login.route) {
            LoginScreen(
                navController = navController,
                onLoginSuccess = {
                    navController.popBackStack()
                    navController.navigate(Screen.Profile.route)
                }
            )
        }

        // 8. Registro
        composable(Screen.Register.route) {
            RegisterScreen(
                navController = navController,
                onRegisterSuccess = {
                    navController.popBackStack()
                    navController.navigate(Screen.Profile.route)
                }
            )
        }
    }
}