package com.example.delivery_20.ui.navigation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.delivery_20.screen.*


// Actualiza la clase Screen con las nuevas rutas
sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Search : Screen("search") // NUEVO
    object History : Screen("history") // NUEVO
    object RestaurantDetail : Screen("restaurant_detail/{restaurantId}") {
        fun createRoute(restaurantId: String) = "restaurant_detail/$restaurantId"
    }
    object Cart : Screen("cart")
    object Profile : Screen("profile")
    object Settings : Screen("settings") // NUEVO
    object Help : Screen("help") // NUEVO
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
        // Pantallas existentes
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

        // NUEVAS PANTALLAS
        composable(Screen.Search.route) {
            SearchScreen(navController = navController)
        }

        composable(Screen.History.route) {
            HistoryScreen(navController = navController)
        }

        // En la clase Screen:
        object Camera : Screen("camera")

// En el NavHost (dentro del composable):
        composable(Screen.Camera.route) { backStackEntry ->
            CameraScreen(
                navController = navController,
                onPhotoTaken = { uri ->
                    // Guardar URI en ViewModel o estado global
                    // Ejemplo: orderViewModel.setOrderPhoto(uri)
                }
            )
        }

        // Pantallas de autenticación
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