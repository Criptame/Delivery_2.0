package com.example.delivery_20.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.delivery_20.model.Restaurant
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RestaurantViewModel : ViewModel() {
    private val _restaurants = MutableStateFlow<List<Restaurant>>(emptyList())
    val restaurants: StateFlow<List<Restaurant>> = _restaurants

    private val _selectedRestaurant = MutableStateFlow<Restaurant?>(null)
    val selectedRestaurant: StateFlow<Restaurant?> = _selectedRestaurant

    init {
        loadRestaurants()
    }

    private fun loadRestaurants() {
        viewModelScope.launch {
            // Reemplaza Restaurant.getSampleRestaurants() con datos directos
            _restaurants.value = getSampleRestaurants()
        }
    }

    fun selectRestaurant(restaurant: Restaurant) {
        _selectedRestaurant.value = restaurant
    }

    fun filterByCategory(category: String) {
        viewModelScope.launch {
            val allRestaurants = getSampleRestaurants()  // Cambiado aquí también
            _restaurants.value = if (category == "Todos") {
                allRestaurants
            } else {
                allRestaurants.filter { it.category == category }
            }
        }
    }

    // Función privada para obtener datos de ejemplo
    private fun getSampleRestaurants(): List<Restaurant> {
        return listOf(
            Restaurant(
                id = "1",
                name = "Pizzería Italiana",
                description = "Las mejores pizzas artesanales de la ciudad",
                rating = 4.5f,
                deliveryTime = "20-30 min",
                category = "Italiana"
            ),
            Restaurant(
                id = "2",
                name = "Burger House",
                description = "Hamburguesas gourmet con ingredientes premium",
                rating = 4.3f,
                deliveryTime = "15-25 min",
                category = "Americana"
            ),
            Restaurant(
                id = "3",
                name = "Sushi Zen",
                description = "Sushi fresco preparado por chefs japoneses",
                rating = 4.7f,
                deliveryTime = "30-40 min",
                category = "Japonesa"
            ),
            Restaurant(
                id = "4",
                name = "Tacos México",
                description = "Auténtica comida mexicana con sabores tradicionales",
                rating = 4.4f,
                deliveryTime = "10-20 min",
                category = "Mexicana"
            ),
            Restaurant(
                id = "5",
                name = "Dulce Tentación",
                description = "Postres y pasteles artesanales hechos diariamente",
                rating = 4.6f,
                deliveryTime = "25-35 min",
                category = "Postres"
            )
        )
    }
}