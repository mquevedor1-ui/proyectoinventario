package com.example.inventario.ui.config

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.inventario.viewModel.AppThemeState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TemasScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Temas Visuales", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Regresar")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Selecciona un color para la aplicación",
                style = MaterialTheme.typography.bodyLarge
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Button(
                    onClick = { AppThemeState.cambiarTema("verde") },
                    modifier = Modifier.weight(1f)
                ) { Text("Verde") }
                
                Button(
                    onClick = { AppThemeState.cambiarTema("azul") },
                    modifier = Modifier.weight(1f)
                ) { Text("Azul") }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Button(
                    onClick = { AppThemeState.cambiarTema("oscuro") },
                    modifier = Modifier.weight(1f)
                ) { Text("Oscuro") }
                
                Button(
                    onClick = { AppThemeState.cambiarTema("naranja") },
                    modifier = Modifier.weight(1f)
                ) { Text("Naranja") }
            }

            Button(
                onClick = { AppThemeState.cambiarTema("morado") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Morado")
            }
            
            Spacer(modifier = Modifier.height(20.dp))
            
            Text(
                text = "Nota: El cambio se aplica globalmente a toda la aplicación.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
