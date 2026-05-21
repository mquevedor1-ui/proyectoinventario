package com.example.inventario.ui.config.notifications

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.NotificationsOff
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.inventario.data.Entrada
import com.example.inventario.data.Salida
import com.example.inventario.data.producto
import com.example.inventario.viewModel.EntradaViewModel
import com.example.inventario.viewModel.ProductoViewModel
import com.example.inventario.viewModel.SalidaViewModel
import androidx.compose.material.icons.automirrored.filled.Input
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.ui.platform.LocalConfiguration
import android.text.format.DateUtils
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificacionesScreen(
    navController: NavController
) {
    val productoViewModel: ProductoViewModel = viewModel()
    val entradaViewModel: EntradaViewModel = viewModel()
    val salidaViewModel: SalidaViewModel = viewModel()
    
    val productosBajoStock by productoViewModel.obtenerProductosBajoStock().collectAsState(initial = emptyList())
    val entradasRecientes by entradaViewModel.allEntradas.collectAsState(initial = emptyList())
    val salidasRecientes by salidaViewModel.allSalidas.collectAsState(initial = emptyList())
    
    // Estado para habilitar/deshabilitar notificaciones (Simulado)
    var notificacionesHabilitadas by remember { mutableStateOf(true) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Centro de Notificaciones", fontWeight = FontWeight.Bold) },
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
        ) {
            // Switch Maestro
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = if (notificacionesHabilitadas) 
                        MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f) 
                        else MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            if (notificacionesHabilitadas) Icons.Default.Notifications else Icons.Default.NotificationsOff,
                            contentDescription = null,
                            tint = if (notificacionesHabilitadas) MaterialTheme.colorScheme.primary else Color.Gray
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = if (notificacionesHabilitadas) "Notificaciones Activas" else "Notificaciones Desactivadas",
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Switch(
                        checked = notificacionesHabilitadas,
                        onCheckedChange = { notificacionesHabilitadas = it }
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            if (!notificacionesHabilitadas) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Las notificaciones están desactivadas", color = Color.Gray)
                }
            } else {
                Text(
                    text = "Alertas Recientes",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                if (productosBajoStock.isEmpty() && entradasRecientes.isEmpty() && salidasRecientes.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("No hay alertas ni movimientos recientes", color = Color.Gray)
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        if (productosBajoStock.isNotEmpty()) {
                            item {
                                StockBajoNotificationItem(productosBajoStock)
                            }
                        }
                        
                        items(entradasRecientes.take(5)) { entrada ->
                            MovimientoNotificationItem(
                                titulo = "Ingreso de ${entrada.descripcion} x${entrada.cantidad}",
                                descripcion = "Se registró un ingreso de ${entrada.cantidad} ${entrada.unidad}",
                                fecha = entrada.fecha,
                                icono = Icons.AutoMirrored.Filled.Input,
                                color = Color(0xFF4CAF50)
                            )
                        }

                        items(salidasRecientes.take(5)) { salida ->
                            MovimientoNotificationItem(
                                titulo = "Salida de ${salida.descripcion} x${salida.cantidad}",
                                descripcion = "Llevado por ${salida.responsable}",
                                fecha = salida.fecha,
                                icono = Icons.AutoMirrored.Filled.Logout,
                                color = Color(0xFFF44336)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MovimientoNotificationItem(titulo: String, descripcion: String, fecha: String, icono: androidx.compose.ui.graphics.vector.ImageVector, color: Color) {
    val configuration = LocalConfiguration.current
    val locale = remember(configuration) { configuration.locales[0] }
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = androidx.compose.foundation.shape.CircleShape,
                color = color.copy(alpha = 0.1f),
                modifier = Modifier.size(40.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(icono, contentDescription = null, tint = color, modifier = Modifier.size(20.dp))
                }
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(titulo, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Text(descripcion, fontSize = 13.sp)
                Text(formatFecha(fecha, locale), fontSize = 11.sp, color = Color.Gray)
            }
        }
    }
}

fun formatFecha(fechaStr: String, locale: java.util.Locale): String {
    return try {
        val sdf = SimpleDateFormat("d/M/yyyy", locale)
        val date = sdf.parse(fechaStr) ?: return fechaStr
        val now = System.currentTimeMillis()
        
        val relativeTime = DateUtils.getRelativeTimeSpanString(
            date.time,
            now,
            DateUtils.MINUTE_IN_MILLIS,
            DateUtils.FORMAT_ABBREV_RELATIVE
        ).toString()
        
        "$fechaStr ($relativeTime)"
    } catch (e: Exception) {
        fechaStr
    }
}

@Composable
fun StockBajoNotificationItem(productos: List<producto>) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.2f)),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Warning, contentDescription = null, tint = MaterialTheme.colorScheme.error)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Stock Bajo", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.error)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text("Hay ${productos.size} productos por debajo del stock mínimo.")
            
            Spacer(modifier = Modifier.height(8.dp))
            
            productos.take(3).forEach { producto ->
                Text("• ${producto.descripcion} (${producto.cantidad} unidades)", fontSize = 12.sp)
            }
            if (productos.size > 3) {
                Text("... y ${productos.size - 3} más", fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun NotificationCategoryItem(titulo: String, mensaje: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(titulo, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(4.dp))
            Text(mensaje, fontSize = 13.sp, color = Color.Gray)
        }
    }
}
