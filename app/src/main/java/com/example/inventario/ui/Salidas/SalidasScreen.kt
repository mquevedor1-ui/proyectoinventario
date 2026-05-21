package com.example.inventario.ui.Salidas

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.Calendar
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.inventario.data.Salida
import com.example.inventario.ui.AppTopBar
import com.example.inventario.viewModel.SalidaViewModel
import com.example.inventario.viewModel.SessionManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SalidasScreen(
    navController: NavController,
    bodegaId: String
) {
    val context = LocalContext.current
    val salidaViewModel: SalidaViewModel = viewModel()

    val searchQuery by salidaViewModel.searchQuery.collectAsState()
    val filtroPeriodo by salidaViewModel.filtroPeriodo.collectAsState()
    val periodoTexto by salidaViewModel.periodoTexto.collectAsState()
    val fechaReferencia by salidaViewModel.fechaReferencia.collectAsState()
    val salidas by salidaViewModel.obtenerSalidasFiltradas(bodegaId).collectAsState(initial = emptyList())
    val totalCosto by salidaViewModel.obtenerTotalCostoFiltrado(bodegaId).collectAsState(initial = 0.0)

    var showDatePicker by remember { mutableStateOf(false) }

    if (showDatePicker) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = fechaReferencia.timeInMillis
        )
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        val cal = Calendar.getInstance().apply { timeInMillis = millis + (1000 * 60 * 60 * 24) }
                        salidaViewModel.setFechaReferencia(cal)
                    }
                    showDatePicker = false
                }) { Text("Aceptar") }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) { Text("Cancelar") }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    LaunchedEffect(Unit) {
        salidaViewModel.sincronizarDesdeFirebase()
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            AppTopBar(
                titulo = "Historial de Salidas",
                subtitulo = "Bodega: $bodegaId",
                navController = navController
            )
        },
        floatingActionButton = {
            if (SessionManager.esAdmin() || SessionManager.rolUsuario() == "encargado") {
                FloatingActionButton(
                    onClick = { navController.navigate("crearSalida/$bodegaId") },
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Registrar")
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            // Buscador
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { salidaViewModel.setSearchQuery(it) },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Buscar por código, descripción o vehículo...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                shape = RoundedCornerShape(12.dp),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Resumen de Costos
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.MonetizationOn, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text("Costo Total en Periodo", style = MaterialTheme.typography.labelMedium)
                        Text(
                            "$${String.format("%.2f", totalCosto)}",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Selector de Periodo
            PeriodoTabs(filtroPeriodo) { salidaViewModel.setFiltroPeriodo(it) }

            Text(
                text = periodoTexto,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .clickable { if (filtroPeriodo != "Todo") showDatePicker = true },
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(modifier = Modifier.fillMaxWidth()) {
                Button(
                    onClick = { exportarSalidasPDF(context, salidas, periodoTexto) },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Default.PictureAsPdf, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("PDF")
                }

                Spacer(modifier = Modifier.width(8.dp))

                Button(
                    onClick = { exportarSalidasExcel(context, salidas, periodoTexto) },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1D6F42))
                ) {
                    Icon(Icons.Default.TableChart, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Excel")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (salidas.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No se encontraron salidas", color = Color.Gray)
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(salidas) { salida ->
                        SalidaCardItem(salida, salidaViewModel, navController)
                    }
                }
            }
        }
    }
}

@Composable
fun PeriodoTabs(
    periodoSeleccionado: String,
    onPeriodoSelected: (String) -> Unit
) {
    val opciones = listOf("Dia", "Semana", "Mes", "Año", "Todo")
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(12.dp))
            .padding(4.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        opciones.forEach { opcion ->
            val seleccionado = periodoSeleccionado == opcion
            Box(
                modifier = Modifier
                    .weight(1f)
                    .background(
                        if (seleccionado) MaterialTheme.colorScheme.primary else Color.Transparent,
                        RoundedCornerShape(8.dp)
                    )
                    .clickable { onPeriodoSelected(opcion) }
                    .padding(vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = opcion,
                    color = if (seleccionado) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = if (seleccionado) FontWeight.Bold else FontWeight.Normal,
                    fontSize = 13.sp
                )
            }
        }
    }
}

@Composable
fun SalidaCardItem(
    salida: Salida,
    viewModel: SalidaViewModel,
    navController: NavController
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    val desc = if (salida.descripcion.isNotEmpty()) salida.descripcion else "Sin descripción"
                    Text(
                        text = desc,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        maxLines = 1
                    )
                    Text(
                        text = "Código: ${salida.codigo}",
                        fontSize = 13.sp,
                        color = Color.Gray
                    )
                }
                Text(
                    text = "-${salida.cantidad}",
                    color = Color(0xFFC62828), // Rojo oscuro
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 18.sp
                )
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), thickness = 0.5.dp)

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.CalendarToday, contentDescription = null, modifier = Modifier.size(14.dp), tint = Color.Gray)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(salida.fecha, fontSize = 12.sp, color = Color.Gray)
                    }
                    if (salida.destino.isNotEmpty()) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Place, contentDescription = null, modifier = Modifier.size(14.dp), tint = Color.Gray)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(salida.destino, fontSize = 12.sp, color = Color.Gray)
                        }
                    }
                    if (salida.vehiculo.isNotEmpty()) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.DirectionsCar, contentDescription = null, modifier = Modifier.size(14.dp), tint = Color.Gray)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(salida.vehiculo, fontSize = 12.sp, color = Color.Gray)
                        }
                    }
                }

                if (SessionManager.esAdmin()) {
                    Row {
                        IconButton(onClick = { 
                            if (SessionManager.esAdmin()) {
                                navController.navigate("editarSalida/${salida.id}") 
                            }
                        }) {
                            Icon(Icons.Default.Edit, contentDescription = "Editar", tint = MaterialTheme.colorScheme.primary)
                        }
                        IconButton(onClick = { 
                            if (SessionManager.esAdmin()) {
                                viewModel.eliminarSalida(salida) 
                            }
                        }) {
                            Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = MaterialTheme.colorScheme.error)
                        }
                    }
                }
            }
        }
    }
}
