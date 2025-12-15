package com.example.delivery_20.screen

import androidx.compose.foundation.clickable  // ¡AGREGA ESTE IMPORT!
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(navController: NavHostController) {

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Configuración") }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Lista de opciones de configuración
            Column(modifier = Modifier.fillMaxWidth()) {
                SettingsOption(Icons.Default.Notifications, "Notificaciones") {
                    navController.navigate("notifications_settings")
                }
                SettingsOption(Icons.Default.Payment, "Métodos de Pago") {
                    navController.navigate("payment_methods")
                }
                SettingsOption(Icons.Default.LocationOn, "Direcciones Guardadas") {
                    navController.navigate("saved_addresses")
                }
                SettingsOption(Icons.Default.Security, "Privacidad y Seguridad") {
                    navController.navigate("privacy_settings")
                }
                SettingsOption(Icons.Default.Language, "Idioma") {
                    navController.navigate("language_settings")
                }
                SettingsOption(Icons.Default.Info, "Acerca de") {
                    navController.navigate("about")
                }
            }
        }
    }
}

@Composable
fun SettingsOption(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    onClick: () -> Unit
) {
    ListItem(
        headlineContent = { Text(title) },
        leadingContent = {
            Icon(icon, contentDescription = title)
        },
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)  // ¡CORREGIDO!
    )
    Divider()
}