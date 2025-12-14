@file:OptIn(ExperimentalMaterial3Api::class)
package com.example.delivery_20.screen


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.delivery_20.state.SessionManager
import com.example.delivery_20.ui.components.buttons.PrimaryButton
import com.example.delivery_20.ui.components.buttons.SecondaryButton

@Composable
fun ProfileScreen(
    navController: NavHostController
) {
    val currentUser = SessionManager.currentUser

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        if (SessionManager.isLoggedIn && currentUser != null) {
            // Usuario logueado - mostrar perfil
            LoggedInProfile(navController, currentUser)
        } else {
            // Usuario no logueado - mostrar opciones de autenticación
            GuestProfile(navController)
        }
    }
}

@Composable
fun LoggedInProfile(
    navController: NavHostController,
    user: com.example.delivery_20.model.User
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        // Avatar
        Surface(
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape),
            color = MaterialTheme.colorScheme.primary,
            shape = CircleShape
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Perfil",
                    modifier = Modifier.size(60.dp),
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = user.name,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = user.email,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(8.dp))

        Surface(
            color = MaterialTheme.colorScheme.secondaryContainer,
            shape = MaterialTheme.shapes.small
        ) {
            Text(
                text = "⭐ Cliente frecuente",
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                style = MaterialTheme.typography.labelMedium
            )
        }
    }

    Spacer(modifier = Modifier.height(32.dp))

    // Información del usuario
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        InfoRow(icon = Icons.Default.Phone, text = user.phone)
        InfoRow(icon = Icons.Default.LocationOn, text = user.address)
    }

    Spacer(modifier = Modifier.height(32.dp))

    // Opciones
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        ProfileOption(
            icon = Icons.Default.History,
            title = "Historial de pedidos",
            onClick = { /* TODO */ }
        )

        ProfileOption(
            icon = Icons.Default.Edit,
            title = "Editar perfil",
            onClick = { navController.navigate("edit_profile") }
        )

        ProfileOption(
            icon = Icons.Default.Settings,
            title = "Configuración",
            onClick = { /* TODO */ }
        )
    }

    Spacer(modifier = Modifier.height(32.dp))

    // Botón de cerrar sesión
    SecondaryButton(
        text = "Cerrar sesión",
        onClick = {
            SessionManager.logout()
            navController.navigate("home") {
                popUpTo("home") { inclusive = true }
            }
        },
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun GuestProfile(navController: NavHostController) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Surface(
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape),
            color = MaterialTheme.colorScheme.surfaceVariant,
            shape = CircleShape
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.PersonOutline,
                    contentDescription = "Invitado",
                    modifier = Modifier.size(70.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "No has iniciado sesión",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Medium
        )

        Text(
            text = "Inicia sesión para guardar tus datos y ver tu historial",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(horizontal = 32.dp, vertical = 16.dp),
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )

        Spacer(modifier = Modifier.height(24.dp))

        PrimaryButton(
            text = "Iniciar sesión",
            onClick = { navController.navigate("login") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        SecondaryButton(
            text = "Crear cuenta",
            onClick = { navController.navigate("register") },
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun InfoRow(icon: ImageVector, text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(20.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.width(12.dp))

        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
fun ProfileOption(
    icon: ImageVector,
    title: String,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                modifier = Modifier.size(24.dp),
                tint = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.width(16.dp))

            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.weight(1f)
            )

            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = "Ir",
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}