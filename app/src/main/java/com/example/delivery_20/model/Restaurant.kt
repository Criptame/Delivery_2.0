package com.example.delivery_20.model

data class Restaurant(
    val id: String,
    val name: String,
    val description: String,
    val rating: Float,
    val deliveryTime: String,
    val category: String
)