package com.example.delivery_20.model

data class Restaurant(
    val id: String,
    val name: String,
    val description: String,
    val rating: Float,
    val deliveryTime: String,
    val category: String = "General"
) {
    companion object {
        fun getSampleRestaurants(): List<Restaurant> {
            return listOf(
                Restaurant(
                    id = "1",
                    name = "Pizza Italiana",
                    description = "Pizzas artesanales",
                    rating = 4.5f,
                    deliveryTime = "30-40 min",
                    category = "Italiana"
                ),
                Restaurant(
                    id = "2",
                    name = "Burger House",
                    description = "Hamburguesas gourmet",
                    rating = 4.7f,
                    deliveryTime = "20-30 min",
                    category = "Americana"
                ),
                Restaurant(
                    id = "3",
                    name = "Sushi Express",
                    description = "Sushi fresco",
                    rating = 4.8f,
                    deliveryTime = "40-50 min",
                    category = "Japonesa"
                )
            )
        }
    }
}