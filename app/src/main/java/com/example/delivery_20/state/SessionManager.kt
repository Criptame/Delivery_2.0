package com.example.delivery_20.state

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.delivery_20.model.User

object SessionManager {
    var currentUser by mutableStateOf<User?>(null)
        private set

    var isLoggedIn by mutableStateOf(false)
        private set

    // Datos de usuarios registrados (simulado - en una app real usarías una base de datos)
    private val registeredUsers = mutableListOf<User>()

    init {
        // Agregar un usuario de prueba
        registeredUsers.add(
            User(
                id = "1",
                name = "Usuario Demo",
                email = "demo@example.com",
                phone = "123456789",
                address = "Calle Principal 123",
                password = "123456"
            )
        )
    }

    fun registerUser(user: User): Boolean {
        // Verificar si el email ya está registrado
        if (registeredUsers.any { it.email == user.email }) {
            return false
        }

        registeredUsers.add(user)
        login(user.email, user.password)
        return true
    }

    fun login(email: String, password: String): Boolean {
        val user = registeredUsers.find {
            it.email == email && it.password == password
        }

        user?.let {
            currentUser = it
            isLoggedIn = true
            return true
        }

        return false
    }

    fun logout() {
        currentUser = null
        isLoggedIn = false
    }

    fun updateUserProfile(updatedUser: User) {
        val index = registeredUsers.indexOfFirst { it.id == updatedUser.id }
        if (index != -1) {
            registeredUsers[index] = updatedUser
            currentUser = updatedUser
        }
    }

    fun isEmailRegistered(email: String): Boolean {
        return registeredUsers.any { it.email == email }
    }
}