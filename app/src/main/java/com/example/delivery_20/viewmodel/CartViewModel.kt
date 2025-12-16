package com.example.delivery_20.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import com.example.delivery_20.model.FoodItem

// ✅ CORRECTO: Usa val y hazlo INMUTABLE
data class CartItem(
    val foodItem: FoodItem,
    val quantity: Int = 1  // ← CAMBIÉ a val
) {
    // Métodos para "modificar" de forma inmutable
    fun increment(): CartItem = copy(quantity = quantity + 1)
    fun decrement(): CartItem = copy(quantity = quantity - 1)
}

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

    // ✅ CORREGIDO: Agregar producto al carrito
    fun addToCart(foodItem: FoodItem) {
        viewModelScope.launch {
            println("🛒 DEBUG: addToCart llamado - Producto: ${foodItem.name}")

            val currentItems = _cartItems.value
            val existingIndex = currentItems.indexOfFirst { it.foodItem.id == foodItem.id }

            println("🛒 DEBUG: Productos antes: ${currentItems.size}")

            val newItems = if (existingIndex != -1) {
                // ✅ CORRECTO: Crear NUEVO objeto con copy()
                val existingItem = currentItems[existingIndex]
                currentItems.toMutableList().apply {
                    this[existingIndex] = existingItem.increment()
                }.also {
                    println("🛒 DEBUG: Incrementado cantidad. Nueva cantidad: ${existingItem.quantity + 1}")
                }
            } else {
                // ✅ CORRECTO: Agregar nuevo item
                (currentItems + CartItem(foodItem, 1)).also {
                    println("🛒 DEBUG: Nuevo producto agregado")
                }
            }

            _cartItems.value = newItems
            updateTotal()

            println("🛒 DEBUG: Productos después: ${_cartItems.value.size}")
            println("🛒 DEBUG: Total items: ${_cartItems.value.sumOf { it.quantity }}")
        }
    }

    // ✅ CORREGIDO: Incrementar cantidad de un item
    fun incrementQuantity(itemId: String) {
        viewModelScope.launch {
            val currentItems = _cartItems.value
            val itemIndex = currentItems.indexOfFirst { it.foodItem.id == itemId }

            if (itemIndex != -1) {
                val item = currentItems[itemIndex]
                val newItems = currentItems.toMutableList().apply {
                    this[itemIndex] = item.increment()
                }
                _cartItems.value = newItems
                updateTotal()
            }
        }
    }

    // ✅ CORREGIDO: Decrementar cantidad de un item
    fun decrementQuantity(itemId: String) {
        viewModelScope.launch {
            val currentItems = _cartItems.value
            val itemIndex = currentItems.indexOfFirst { it.foodItem.id == itemId }

            if (itemIndex != -1) {
                val item = currentItems[itemIndex]
                val newItems = if (item.quantity > 1) {
                    currentItems.toMutableList().apply {
                        this[itemIndex] = item.decrement()
                    }
                } else {
                    // Eliminar si cantidad llega a 0
                    currentItems.filterNot { it.foodItem.id == itemId }
                }
                _cartItems.value = newItems
                updateTotal()
            }
        }
    }

    // ✅ CORREGIDO: Eliminar item completamente del carrito
    fun removeItem(itemId: String) {
        viewModelScope.launch {
            val newItems = _cartItems.value.filterNot { it.foodItem.id == itemId }
            _cartItems.value = newItems
            updateTotal()
        }
    }

    // ✅ CORRECTO: Limpiar todo el carrito
    fun clearCart() {
        viewModelScope.launch {
            _cartItems.value = emptyList()
            _cartTotal.value = 0
        }
    }

    // ✅ CORRECTO: Calcular el total
    private fun updateTotal() {
        val total = _cartItems.value.sumOf { it.foodItem.price * it.quantity }
        _cartTotal.value = total
        println("💰 Total actualizado: $total")
    }

    // ✅ CORRECTO: Obtener cantidad de items (para el badge)
    fun getItemCount(): Int {
        return _cartItems.value.sumOf { it.quantity }
    }
}