package com.example.delivery_20.model

data class User(
    val id: String,
    val name: String,
    val email: String,
    val phone: String,
    val address: String,
    val password: String = "" // En una app real esto iría encriptado
) {
    companion object {
        // Usuario vacío por defecto (no logueado)
        fun getEmptyUser(): User {
            return User(
                id = "",
                name = "",
                email = "",
                phone = "",
                address = ""
            )
        }
    }
}