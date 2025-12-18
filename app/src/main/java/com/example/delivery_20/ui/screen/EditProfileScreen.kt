package com.example.delivery_20.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.delivery_20.state.SessionManager
import com.example.delivery_20.ui.components.buttons.PrimaryButton
import com.example.delivery_20.model.User

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(navController: NavHostController) {
    val currentUser = SessionManager.currentUser ?: return

    var name by remember { mutableStateOf(currentUser.name) }
    var email by remember { mutableStateOf(currentUser.email) }
    var phone by remember { mutableStateOf(currentUser.phone) }
    var address by remember { mutableStateOf(currentUser.address) }
    var currentPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    var successMessage by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Editar Perfil") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (successMessage.isNotEmpty()) {
                Surface(
                    color = MaterialTheme.colorScheme.primaryContainer,
                    shape = MaterialTheme.shapes.medium,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.CheckCircle,
                            contentDescription = "Éxito",
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            successMessage,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }

            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    "Información personal",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )

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
                    singleLine = true,
                    enabled = false
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

                Divider(modifier = Modifier.padding(vertical = 8.dp))

                Text(
                    "Cambiar contraseña",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )

                OutlinedTextField(
                    value = currentPassword,
                    onValueChange = { currentPassword = it; errorMessage = "" },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Contraseña actual") },
                    leadingIcon = {
                        Icon(Icons.Default.Lock, contentDescription = "Contraseña actual")
                    },
                    singleLine = true
                )

                OutlinedTextField(
                    value = newPassword,
                    onValueChange = { newPassword = it; errorMessage = "" },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Nueva contraseña") },
                    leadingIcon = {
                        Icon(Icons.Default.Lock, contentDescription = "Nueva contraseña")
                    },
                    singleLine = true
                )

                OutlinedTextField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it; errorMessage = "" },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Confirmar nueva contraseña") },
                    leadingIcon = {
                        Icon(Icons.Default.Lock, contentDescription = "Confirmar contraseña")
                    },
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

            PrimaryButton(
                text = "Guardar cambios",
                onClick = {
                    if (name.isEmpty() || phone.isEmpty() || address.isEmpty()) {
                        errorMessage = "Por favor completa todos los campos obligatorios"
                        return@PrimaryButton
                    }

                    if (newPassword.isNotEmpty()) {
                        if (currentPassword.isEmpty()) {
                            errorMessage = "Ingresa tu contraseña actual para cambiarla"
                            return@PrimaryButton
                        }

                        if (newPassword.length < 6) {
                            errorMessage = "La nueva contraseña debe tener al menos 6 caracteres"
                            return@PrimaryButton
                        }

                        if (newPassword != confirmPassword) {
                            errorMessage = "Las nuevas contraseñas no coinciden"
                            return@PrimaryButton
                        }

                        if (currentPassword != currentUser.password) {
                            errorMessage = "La contraseña actual es incorrecta"
                            return@PrimaryButton
                        }
                    }

                    val updatedUser = User(
                        id = currentUser.id,
                        name = name,
                        email = currentUser.email,
                        phone = phone,
                        address = address,
                        password = if (newPassword.isNotEmpty()) newPassword else currentUser.password
                    )

                    SessionManager.updateUserProfile(updatedUser)
                    successMessage = "Perfil actualizado correctamente"
                    errorMessage = ""
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            TextButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Cancelar")
            }
        }
    }
}