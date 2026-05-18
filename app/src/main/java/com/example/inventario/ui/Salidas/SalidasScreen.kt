package com.example.inventario.ui.Salidas

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.inventario.data.Salida
import com.example.inventario.viewModel.SalidaViewModel
import com.example.inventario.viewModel.ProductoViewModel
import com.example.inventario.viewModel.SessionManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SalidasScreen(navController: NavController) {
    val salidaViewModel: SalidaViewModel = viewModel()
    val productoViewModel: ProductoViewModel = viewModel()
    val salidas by salidaViewModel.allSalidas.collectAsState(initial = emptyList())

    LaunchedEffect(Unit) {
        salidaViewModel.sincronizarDesdeFirebase()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Historial de Salidas") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Regresar")
                    }
                }
            )
        },
        floatingActionButton = {
            if (SessionManager.esAdmin() || SessionManager.rolUsuario() == "encargado") {
                FloatingActionButton(onClick = { navController.navigate("crearSalida") }) {
                    Icon(Icons.Default.Add, contentDescription = "Nueva Salida")
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
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(salidas) { salida ->
                    SalidaCard(salida, salidaViewModel, productoViewModel)
                }
            }
        }
    }
}

@Composable
fun SalidaCard(
    salida: Salida,
    viewModel: SalidaViewModel,
    productoViewModel: ProductoViewModel
) {
    var productoNombre by remember { mutableStateOf("Cargando...") }

    LaunchedEffect(salida.productoId) {
        val p = productoViewModel.obtenerProductoPorId(salida.productoId)
        productoNombre = p?.descripcion ?: "Producto no encontrado"
    }

    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(text = productoNombre, style = MaterialTheme.typography.titleMedium)
                    Text(text = "Cantidad: ${salida.cantidad}", style = MaterialTheme.typography.bodyMedium)
                    Text(text = "Fecha: ${salida.fecha}", style = MaterialTheme.typography.bodySmall)
                    if (salida.notas.isNotEmpty()) {
                        Text(text = "Notas: ${salida.notas}", style = MaterialTheme.typography.bodySmall)
                    }
                }
                if (SessionManager.esAdmin()) {
                    IconButton(onClick = { viewModel.eliminarSalida(salida) }) {
                        Icon(Icons.Default.Delete, contentDescription = "Eliminar")
                    }
                }
            }
        }
    }
}
