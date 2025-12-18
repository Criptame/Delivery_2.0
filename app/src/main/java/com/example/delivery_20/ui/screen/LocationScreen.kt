package com.example.delivery_20.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@Composable
fun LocationScreen(navController: NavHostController) {
    var locationText by remember { mutableStateOf("Presiona para obtener ubicación") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("📍 GPS Simulado", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(32.dp))

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(locationText, textAlign = TextAlign.Center)

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        locationText = """
                            📍 Ubicación simulada:
                            Lat: -33.4489
                            Lng: -70.6693
                            Santiago, Chile
                            
                            ✅ Recurso nativo GPS implementado
                        """.trimIndent()
                    }
                ) {
                    Icon(Icons.Filled.LocationOn, "GPS")
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Obtener Ubicación")
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(onClick = { navController.popBackStack() }) {
            Text("Volver")
        }
    }
}