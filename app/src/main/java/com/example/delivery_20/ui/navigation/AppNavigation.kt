package com.example.delivery_20.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.delivery_20.screen.*
import androidx.compose.material3.Text

// Definimos las rutas de la app
sealed class Screen(val route: String) {
    object Home : Screen("home")
    object RestaurantDetail : Screen("restaurant_detail/{restaurantId}") {
        fun createRoute(restaurantId: String) = "restaurant_detail/$restaurantId"
    }
    object Cart : Screen("cart")
    object Profile : Screen("profile")
    object Login : Screen("login")
    object Register : Screen("register")
    object EditProfile : Screen("edit_profile")
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
        composable(Screen.Home.route) {
            HomeScreen(navController = navController)
        }
        composable(Screen.RestaurantDetail.route) { backStackEntry ->
            val restaurantId = backStackEntry.arguments?.getString("restaurantId") ?: ""
            RestaurantDetailScreen(
                navController = navController,
                restaurantId = restaurantId
            )
        }
        composable(Screen.Cart.route) {
            CartScreen(navController = navController)
        }
        composable(Screen.Profile.route) {
            ProfileScreen(navController = navController)
        }
        composable(Screen.Login.route) {
            LoginScreen(
                navController = navController,
                onLoginSuccess = {
                    navController.popBackStack()
                    navController.navigate(Screen.Profile.route)
                }
            )
        }
        composable(Screen.Register.route) {
            RegisterScreen(
                navController = navController,
                onRegisterSuccess = {
                    navController.popBackStack()
                    navController.navigate(Screen.Profile.route)
                }
            )
        }
        composable(Screen.EditProfile.route) {
            // TODO: Crear pantalla de edición de perfil
            Text("Editar perfil (en desarrollo)")
        }
    }
}