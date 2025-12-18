package com.example.delivery_20.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import com.example.delivery_20.model.FoodItem
import com.example.delivery_20.model.CartItem  // ← NUEVO IMPORT

class CartViewModel : ViewModel() {
    // Estado del carrito
    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
    val cartItems: StateFlow<List<CartItem>> = _cartItems.asStateFlow()

    // Total del carrito
    private val _cartTotal = MutableStateFlow(0)
    val cartTotal: StateFlow<Int> = _cartTotal.asStateFlow()

    // Contador de items (para el badge)
    private val _itemCount = MutableStateFlow(0)
    val itemCount: StateFlow<Int> = _itemCount.asStateFlow()

    init {
        println("🛒 CartViewModel inicializado")
        updateTotals()
    }

    // ✅ FUNCIÓN CRÍTICA: Agregar producto al carrito
    fun addToCart(foodItem: FoodItem) {
        viewModelScope.launch {
            println("🛒 === ADD TO CART ===")
            println("🛒 Producto: ${foodItem.name} (ID: ${foodItem.id})")
            println("🛒 Precio: ${foodItem.price}")

            _cartItems.update { currentItems ->
                val existingIndex = currentItems.indexOfFirst { it.foodItem.id == foodItem.id }

                if (existingIndex != -1) {
                    // Item ya existe, incrementar cantidad
                    println("🛒 Item existente encontrado en índice $existingIndex")
                    currentItems.toMutableList().apply {
                        val existingItem = this[existingIndex]
                        this[existingIndex] = existingItem.increment()
                        println("🛒 Nueva cantidad: ${this[existingIndex].quantity}")
                    }
                } else {
                    // Nuevo item
                    println("🛒 Nuevo item agregado")
                    currentItems + CartItem(foodItem, 1)
                }
            }

            updateTotals()
            debugCart()
        }
    }

    // Incrementar cantidad de un item
    fun incrementQuantity(itemId: String) {
        viewModelScope.launch {
            _cartItems.update { currentItems ->
                val itemIndex = currentItems.indexOfFirst { it.foodItem.id == itemId }
                if (itemIndex != -1) {
                    currentItems.toMutableList().apply {
                        val item = this[itemIndex]
                        this[itemIndex] = item.increment()
                    }
                } else {
                    currentItems
                }
            }
            updateTotals()
        }
    }

    // Decrementar cantidad de un item
    fun decrementQuantity(itemId: String) {
        viewModelScope.launch {
            _cartItems.update { currentItems ->
                val itemIndex = currentItems.indexOfFirst { it.foodItem.id == itemId }
                if (itemIndex != -1) {
                    val item = currentItems[itemIndex]
                    if (item.quantity > 1) {
                        currentItems.toMutableList().apply {
                            this[itemIndex] = item.decrement()
                        }
                    } else {
                        // Eliminar si cantidad llega a 0
                        currentItems.filterNot { it.foodItem.id == itemId }
                    }
                } else {
                    currentItems
                }
            }
            updateTotals()
        }
    }

    // Eliminar item del carrito
    fun removeItem(itemId: String) {
        viewModelScope.launch {
            _cartItems.update { currentItems ->
                currentItems.filterNot { it.foodItem.id == itemId }
            }
            updateTotals()
        }
    }

    // Limpiar todo el carrito
    fun clearCart() {
        viewModelScope.launch {
            _cartItems.value = emptyList()
            updateTotals()
            println("🛒 Carrito limpiado completamente")
        }
    }

    // ✅ ACTUALIZAR: Calcular totales
    private fun updateTotals() {
        viewModelScope.launch {
            val items = _cartItems.value
            val total = items.sumOf { it.foodItem.price * it.quantity }
            val count = items.sumOf { it.quantity }

            _cartTotal.value = total
            _itemCount.value = count

            println("💰 Total actualizado: $total")
            println("📦 Items totales: $count")
        }
    }

    // ✅ NUEVO: Verificar si un item está en el carrito
    fun isItemInCart(itemId: String): Boolean {
        return _cartItems.value.any { it.foodItem.id == itemId }
    }

    // ✅ NUEVO: Obtener cantidad de un item específico
    fun getItemQuantity(itemId: String): Int {
        return _cartItems.value.find { it.foodItem.id == itemId }?.quantity ?: 0
    }

    // ✅ NUEVO: Función de debugging
    fun debugCart() {
        println("🛒 === DEBUG CART ===")
        println("🛒 Items en carrito: ${_cartItems.value.size}")
        println("🛒 Total items: ${_itemCount.value}")
        println("🛒 Total precio: ${_cartTotal.value}")

        if (_cartItems.value.isEmpty()) {
            println("🛒 Carrito vacío")
        } else {
            _cartItems.value.forEachIndexed { index, item ->
                println("🛒 [$index] ${item.foodItem.name}: ${item.quantity} x ${item.foodItem.price}")
            }
        }
        println("🛒 === FIN DEBUG ===")
    }

}