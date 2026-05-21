package com.example.inventario.ui.config

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.inventario.viewModel.SessionManager
import com.example.inventario.viewModel.UsuarioViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfigScreen(
    navController: NavController,
    usuarioViewModel: UsuarioViewModel
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Configuración", fontWeight = FontWeight.Bold) },
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
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            
            if (SessionManager.esAdmin()) {
                Text(
                    text = "Administración",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )

                ConfigItem(
                    title = "Gestión de Usuarios",
                    subtitle = "Listar, eliminar y ver roles de usuarios",
                    icon = Icons.Default.People,
                    onClick = { navController.navigate("usuarios") }
                )

                ConfigItem(
                    title = "Crear Nuevo Usuario",
                    subtitle = "Registrar personal con rol admin o usuario",
                    icon = Icons.Default.PersonAdd,
                    onClick = { navController.navigate("crearUsuario") }
                )

                ConfigItem(
                    title = "Papelera de Reciclaje",
                    subtitle = "Restaurar productos, entradas, salidas o facturas",
                    icon = Icons.Default.Delete,
                    onClick = { navController.navigate("papelera") }
                )
            }

            Text(
                text = "Personalización y App",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(top = 8.dp)
            )

            ConfigItem(
                title = "Temas Visuales",
                subtitle = "Cambiar colores de la interfaz",
                icon = Icons.Default.Palette,
                onClick = { navController.navigate("temas") }
            )

            ConfigItem(
                title = "Notificaciones",
                subtitle = "Configurar alertas de stock bajo",
                icon = Icons.Default.Notifications,
                onClick = { navController.navigate("notificaciones") }
            )

            if (SessionManager.esAdmin()) {
                ConfigItem(
                    title = "Avisos de Movimiento",
                    subtitle = "Activar/Desactivar avisos de entradas y salidas",
                    icon = Icons.Default.NotificationAdd,
                    onClick = { /* Implementación pendiente */ }
                )
            }
            
            Spacer(modifier = Modifier.height(20.dp))
            
            // Botón de Cerrar Sesión (Opcional pero recomendado aquí)
            OutlinedButton(
                onClick = { 
                    SessionManager.logout()
                    navController.navigate("login") {
                        popUpTo(0)
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error)
            ) {
                Icon(Icons.Default.ExitToApp, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Cerrar Sesión")
            }
        }
    }
}

@Composable
fun ConfigItem(
    title: String,
    subtitle: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                color = MaterialTheme.colorScheme.secondaryContainer,
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.size(48.dp)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.padding(12.dp),
                    tint = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(text = title, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text(text = subtitle, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            
            Icon(Icons.Default.ChevronRight, contentDescription = null, tint = MaterialTheme.colorScheme.outline)
        }
    }
}
