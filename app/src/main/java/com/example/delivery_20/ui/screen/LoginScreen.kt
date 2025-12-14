package com.example.delivery_20.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.delivery_20.state.SessionManager
import com.example.delivery_20.ui.components.buttons.PrimaryButton
import com.example.delivery_20.ui.components.buttons.SecondaryButton
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    navController: NavHostController,
    onLoginSuccess: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Logo/Título
        Text(
            text = "🍕 Food Delivery",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Text(
            text = "Inicia sesión para continuar",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        // Campos de formulario
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = email,
                onValueChange = { email = it; errorMessage = "" },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Correo electrónico") },
                leadingIcon = {
                    Icon(Icons.Default.Email, contentDescription = "Email")
                },
                singleLine = true,
                isError = errorMessage.isNotEmpty()
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
                singleLine = true,
                isError = errorMessage.isNotEmpty()
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

        // Botón de inicio de sesión
        PrimaryButton(
            text = if (isLoading) "Iniciando sesión..." else "Iniciar sesión",
            onClick = {
                if (email.isEmpty() || password.isEmpty()) {
                    errorMessage = "Por favor completa todos los campos"
                    return@PrimaryButton
                }

                isLoading = true

                // Usamos coroutineScope.launch para operaciones asíncronas
                coroutineScope.launch {
                    val success = SessionManager.login(email, password)

                    if (success) {
                        onLoginSuccess()
                    } else {
                        errorMessage = "Correo o contraseña incorrectos"
                        isLoading = false
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Enlace a registro
        Text(
            text = "¿No tienes una cuenta?",
            style = MaterialTheme.typography.bodyMedium
        )

        SecondaryButton(
            text = "Crear cuenta nueva",
            onClick = { navController.navigate("register") },
            modifier = Modifier.fillMaxWidth()
        )

        // Opción de invitado
        Spacer(modifier = Modifier.height(24.dp))
        TextButton(onClick = {
            // Continuar como invitado
            onLoginSuccess()
        }) {
            Text("Continuar como invitado")
        }
    }
}