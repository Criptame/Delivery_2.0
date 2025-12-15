package com.example.delivery_20.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import com.example.delivery_20.model.FoodItem

// Modelo para items del carrito
data class CartItem(
    val foodItem: FoodItem,
    var quantity: Int = 1
)

class CartViewModel : ViewModel() {
    // Estado del carrito
    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
    val cartItems: StateFlow<List<CartItem>> = _cartItems.asStateFlow()

    // Total del carrito
    private val _cartTotal = MutableStateFlow(0)
    val cartTotal: StateFlow<Int> = _cartTotal.asStateFlow()

    init {
        updateTotal()
    }

    // Agregar producto al carrito
    fun addToCart(foodItem: FoodItem) {
        println("🛒 DEBUG: addToCart llamado - Producto: ${foodItem.name}")
        viewModelScope.launch {
            val currentItems = _cartItems.value.toMutableList()
            val existingItem = currentItems.find { it.foodItem.id == foodItem.id }

            println("🛒 DEBUG: Productos antes: ${currentItems.size}")

            if (existingItem != null) {
                // Si ya existe, incrementar cantidad
                existingItem.quantity++
                println("🛒 DEBUG: Incrementado cantidad. Nueva cantidad: ${existingItem.quantity}")
            } else {
                // Si no existe, agregar nuevo
                currentItems.add(CartItem(foodItem, 1))
                println("🛒 DEBUG: Nuevo producto agregado")
            }

            _cartItems.value = currentItems
            updateTotal()

            println("🛒 DEBUG: Productos después: ${_cartItems.value.size}")
            println("🛒 DEBUG: Total items: ${_cartItems.value.sumOf { it.quantity }}")
        }
    }

    // Incrementar cantidad de un item
    fun incrementQuantity(itemId: String) {
        viewModelScope.launch {
            val currentItems = _cartItems.value.toMutableList()
            val item = currentItems.find { it.foodItem.id == itemId }

            item?.let {
                it.quantity++
                _cartItems.value = currentItems
                updateTotal()
            }
        }
    }

    // Decrementar cantidad de un item
    fun decrementQuantity(itemId: String) {
        viewModelScope.launch {
            val currentItems = _cartItems.value.toMutableList()
            val itemIndex = currentItems.indexOfFirst { it.foodItem.id == itemId }

            if (itemIndex != -1) {
                val item = currentItems[itemIndex]
                if (item.quantity > 1) {
                    // Si tiene más de 1, decrementar
                    item.quantity--
                } else {
                    // Si tiene 1, eliminar del carrito
                    currentItems.removeAt(itemIndex)
                }
                _cartItems.value = currentItems
                updateTotal()
            }
        }
    }

    // Eliminar item completamente del carrito
    fun removeItem(itemId: String) {
        viewModelScope.launch {
            val currentItems = _cartItems.value.toMutableList()
            currentItems.removeAll { it.foodItem.id == itemId }
            _cartItems.value = currentItems
            updateTotal()
        }
    }

    // Limpiar todo el carrito
    fun clearCart() {
        viewModelScope.launch {
            _cartItems.value = emptyList()
            _cartTotal.value = 0
        }
    }

    // Calcular el total
    private fun updateTotal() {
        val total = _cartItems.value.sumOf { it.foodItem.price * it.quantity }
        _cartTotal.value = total
    }

    // Obtener cantidad de items (para el badge)
    fun getItemCount(): Int {
        return _cartItems.value.sumOf { it.quantity }
    }
}