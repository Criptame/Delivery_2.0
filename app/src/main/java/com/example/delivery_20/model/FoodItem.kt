package com.example.delivery_20.model

data class FoodItem(
    val id: String,
    val name: String,
    val description: String,
    val price: Int,
    val restaurantId: String
)