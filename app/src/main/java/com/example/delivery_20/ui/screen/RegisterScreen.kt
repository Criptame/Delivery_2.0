package com.example.delivery_20.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.delivery_20.model.User
import com.example.delivery_20.state.SessionManager
import com.example.delivery_20.ui.components.buttons.PrimaryButton
import com.example.delivery_20.ui.components.buttons.SecondaryButton
import java.util.*

@Composable
fun RegisterScreen(
    navController: NavHostController,
    onRegisterSuccess: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        // Título
        Text(
            text = "Crear cuenta",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Campos de formulario
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it; errorMessage = "" },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Nombre completo") },
                leadingIcon = {
                    Icon(Icons.Default.Person, contentDescription = "Nombre")
                },
                singleLine = true
            )

            OutlinedTextField(
                value = email,
                onValueChange = { email = it; errorMessage = "" },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Correo electrónico") },
                leadingIcon = {
                    Icon(Icons.Default.Email, contentDescription = "Email")
                },
                singleLine = true
            )

            OutlinedTextField(
                value = phone,
                onValueChange = { phone = it; errorMessage = "" },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Teléfono") },
                leadingIcon = {
                    Icon(Icons.Default.Phone, contentDescription = "Teléfono")
                },
                singleLine = true
            )

            OutlinedTextField(
                value = address,
                onValueChange = { address = it; errorMessage = "" },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Dirección de entrega") },
                leadingIcon = {
                    Icon(Icons.Default.LocationOn, contentDescription = "Dirección")
                },
                singleLine = true
            )

            OutlinedTextField(
                value = password,
                onValueChange = { password = it; errorMessage = "" },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Contraseña") },
                leadingIcon = {
                    Icon(Icons.Default.Lock, contentDescription = "Contraseña")
                },
                visualTransformation = PasswordVisualTransformation(),
                singleLine = true
            )

            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it; errorMessage = "" },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Confirmar contraseña") },
                leadingIcon = {
                    Icon(Icons.Default.Lock, contentDescription = "Confirmar contraseña")
                },
                visualTransformation = PasswordVisualTransformation(),
                singleLine = true
            )

            if (errorMessage.isNotEmpty()) {
                Text(
                    text = errorMessage,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Botón de registro
        PrimaryButton(
            text = if (isLoading) "Creando cuenta..." else "Crear cuenta",
            onClick = {
                // Validaciones
                if (name.isEmpty() || email.isEmpty() || phone.isEmpty() ||
                    address.isEmpty() || password.isEmpty()) {
                    errorMessage = "Por favor completa todos los campos"
                    return@PrimaryButton
                }

                if (password != confirmPassword) {
                    errorMessage = "Las contraseñas no coinciden"
                    return@PrimaryButton
                }

                if (password.length < 6) {
                    errorMessage = "La contraseña debe tener al menos 6 caracteres"
                    return@PrimaryButton
                }

                if (SessionManager.isEmailRegistered(email)) {
                    errorMessage = "Este correo ya está registrado"
                    return@PrimaryButton
                }

                isLoading = true

                // Crear nuevo usuario
                val newUser = User(
                    id = UUID.randomUUID().toString(),
                    name = name,
                    email = email,
                    phone = phone,
                    address = address,
                    password = password
                )

                val success = SessionManager.registerUser(newUser)

                if (success) {
                    onRegisterSuccess()
                } else {
                    errorMessage = "Error al crear la cuenta"
                    isLoading = false
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Enlace a login
        Text(
            text = "¿Ya tienes una cuenta?",
            style = MaterialTheme.typography.bodyMedium
        )

        SecondaryButton(
            text = "Iniciar sesión",
            onClick = { navController.popBackStack() },
            modifier = Modifier.fillMaxWidth()
        )
    }
}