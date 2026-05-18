package com.example.inventario.ui.entradas

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
import com.example.inventario.data.Entrada
import com.example.inventario.viewModel.EntradaViewModel
import com.example.inventario.viewModel.ProductoViewModel
import com.example.inventario.viewModel.SessionManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EntradasScreen(navController: NavController) {
    val entradaViewModel: EntradaViewModel = viewModel()
    val productoViewModel: ProductoViewModel = viewModel()
    val entradas by entradaViewModel.allEntradas.collectAsState(initial = emptyList())

    LaunchedEffect(Unit) {
        entradaViewModel.sincronizarDesdeFirebase()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Historial de Entradas") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Regresar")
                    }
                }
            )
        },
        floatingActionButton = {
            if (SessionManager.esAdmin() || SessionManager.rolUsuario() == "encargado") {
                FloatingActionButton(onClick = { navController.navigate("crearEntrada") }) {
                    Icon(Icons.Default.Add, contentDescription = "Nueva Entrada")
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
                items(entradas) { entrada ->
                    EntradaCard(entrada, entradaViewModel, productoViewModel)
                }
            }
        }
    }
}

@Composable
fun EntradaCard(
    entrada: Entrada,
    viewModel: EntradaViewModel,
    productoViewModel: ProductoViewModel
) {
    var productoNombre by remember { mutableStateOf("Cargando...") }

    LaunchedEffect(entrada.productoId) {
        val p = productoViewModel.obtenerProductoPorId(entrada.productoId)
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
                    Text(text = "Cantidad: ${entrada.cantidad}", style = MaterialTheme.typography.bodyMedium)
                    Text(text = "Fecha: ${entrada.fecha}", style = MaterialTheme.typography.bodySmall)
                    if (entrada.notas.isNotEmpty()) {
                        Text(text = "Notas: ${entrada.notas}", style = MaterialTheme.typography.bodySmall)
                    }
                }
                if (SessionManager.esAdmin()) {
                    IconButton(onClick = { viewModel.eliminarEntrada(entrada) }) {
                        Icon(Icons.Default.Delete, contentDescription = "Eliminar")
                    }
                }
            }
        }
    }
}
