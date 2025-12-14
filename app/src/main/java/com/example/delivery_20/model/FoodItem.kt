package com.example.delivery_20.model

data class FoodItem(
    val id: String,
    val name: String,
    val description: String,
    val price: Double,
    val restaurantId: String,
    val category: String = "Comida"
) {
    companion object {
        fun getSampleFoodItems(): List<FoodItem> {
            return listOf(
                FoodItem(
                    id = "101",
                    name = "Pizza Margarita",
                    description = "Queso mozzarella y tomate",
                    price = 11.999,
                    restaurantId = "1"
                ),
                FoodItem(
                    id = "102",
                    name = "Hamburguesa Clásica",
                    description = "Carne, queso y vegetales",
                    price = 9.999,
                    restaurantId = "2"
                ),
                FoodItem(
                    id = "103",
                    name = "Sushi Roll Mixto",
                    description = "12 piezas variadas",
                    price = 15.999,
                    restaurantId = "3"
                )
            )
        }
    }
}