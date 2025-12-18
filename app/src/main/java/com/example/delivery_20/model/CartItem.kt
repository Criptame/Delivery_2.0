package com.example.delivery_20.model

data class CartItem(
    val foodItem: FoodItem,
    val quantity: Int = 1
) {
    fun increment(): CartItem = copy(quantity = quantity + 1)
    fun decrement(): CartItem = copy(quantity = quantity - 1)
}