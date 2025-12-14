package com.example.delivery_20.state

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.delivery_20.model.Restaurant
import com.example.delivery_20.model.User

object AppState {
    // Usuario actual
    var currentUser by mutableStateOf<User?>(null)
        private set

    // Restaurante seleccionado
    var selectedRestaurant by mutableStateOf<Restaurant?>(null)
        private set

    // Carrito de compras
    var cartItems by mutableStateOf<Map<String, Int>>(emptyMap())
        private set

    // Funciones para modificar el estado
    fun loginUser(user: User) {
        currentUser = user
    }

    fun logoutUser() {
        currentUser = null
        cartItems = emptyMap()
    }

    fun selectRestaurant(restaurant: Restaurant) {
        selectedRestaurant = restaurant
    }

    fun addToCart(itemId: String, quantity: Int = 1) {
        val currentQuantity = cartItems[itemId] ?: 0
        cartItems = cartItems + (itemId to (currentQuantity + quantity))
    }

    fun removeFromCart(itemId: String) {
        cartItems = cartItems - itemId
    }

    fun getCartCount(): Int {
        return cartItems.values.sum()
    }

    fun clearCart() {
        cartItems = emptyMap()
    }
}