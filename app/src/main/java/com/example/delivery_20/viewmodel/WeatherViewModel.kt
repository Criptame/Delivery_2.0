// Crea en: app/src/main/java/com/example/delivery_20/viewmodel/WeatherViewModel.kt
package com.example.delivery_20.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.delivery_20.api.WeatherService
import com.example.delivery_20.model.WeatherResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class WeatherViewModel : ViewModel() {
    private val _weather = MutableStateFlow("🌤️ Cargando clima...")
    val weather: StateFlow<String> = _weather

    private val _temperature = MutableStateFlow("--°C")
    val temperature: StateFlow<String> = _temperature

    private val service = Retrofit.Builder()
        .baseUrl("https://api.openweathermap.org/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(WeatherService::class.java)

    init {
        loadWeather()
    }

    fun loadWeather() {
        viewModelScope.launch {
            try {
                val response = service.getWeather()
                _weather.value = "🌤️ ${response.weather.firstOrNull()?.description ?: "Despejado"}"
                _temperature.value = "${response.main.temp.toInt()}°C"

                println("✅ API Clima funcionando: ${response.name} - ${response.main.temp}°C")
            } catch (e: Exception) {
                _weather.value = "⚠️ Error cargando clima"
                _temperature.value = "--°C"
                println("❌ Error API Clima: ${e.message}")
            }
        }
    }
}