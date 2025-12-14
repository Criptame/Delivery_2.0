package com.example.delivery_20.utils

import java.text.NumberFormat
import java.util.Locale

object Utils {
    // Formatear precios
    fun formatPrice(price: Double): String {
        val format = NumberFormat.getCurrencyInstance(Locale("es", "ES"))
        return format.format(price)
    }

    // Calcular total del carrito
    fun calculateTotal(items: Map<String, Int>, prices: Map<String, Double>): Double {
        return items.entries.sumOf { (id, quantity) ->
            (prices[id] ?: 0.0) * quantity
        }
    }

    // Validar email
    fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    // Generar ID único simple
    fun generateId(prefix: String = ""): String {
        return prefix + System.currentTimeMillis().toString()
    }
}