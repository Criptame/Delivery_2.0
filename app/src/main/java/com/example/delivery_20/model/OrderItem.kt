package com.example.delivery_20.model

import java.util.Date

data class OrderItem(
    val foodItem: FoodItem,
    val quantity: Int
)

enum class OrderStatus {
    PREPARING,    // En preparación
    ON_THE_WAY,   // En camino
    DELIVERED,    // Entregado
    CANCELLED     // Cancelado
}

data class Order(
    val id: String,
    val items: List<OrderItem>,
    val total: Int,  // CAMBIA Double por Int
    val status: OrderStatus,
    val date: Date,
    val address: String
) {
    companion object {
        fun calculateTotal(items: List<OrderItem>): Int {  // CAMBIA Double por Int
            return items.sumOf { it.foodItem.price * it.quantity }
        }
    }
}