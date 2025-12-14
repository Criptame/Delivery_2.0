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
            // Por ahora usamos datos de muestra
            _restaurants.value = Restaurant.getSampleRestaurants()
        }
    }

    fun selectRestaurant(restaurant: Restaurant) {
        _selectedRestaurant.value = restaurant
    }

    fun filterByCategory(category: String) {
        viewModelScope.launch {
            val allRestaurants = Restaurant.getSampleRestaurants()
            _restaurants.value = if (category == "Todos") {
                allRestaurants
            } else {
                allRestaurants.filter { it.category == category }
            }
        }
    }
}