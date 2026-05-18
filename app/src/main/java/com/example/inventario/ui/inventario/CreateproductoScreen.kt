package com.example.inventario.ui.inventario

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.inventario.data.categoria
import com.example.inventario.data.producto
import com.example.inventario.viewModel.CategoriaViewModel
import com.example.inventario.viewModel.ProductoViewModel
import com.example.inventario.viewModel.SessionManager
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CrearProductoScreen(
    navController: NavController,
    bodegaId: String
) {
    val productoViewModel: ProductoViewModel = viewModel()
    val categoriaViewModel: CategoriaViewModel = viewModel()
    val coroutineScope = rememberCoroutineScope()

    val categorias by categoriaViewModel.categorias.collectAsState()

    var descripcion by remember { mutableStateOf("") }
    var categoriaSeleccionada by remember { mutableStateOf("") }
    var prefijoSeleccionado by remember { mutableStateOf("P") }
    var codigoSiguiente by remember { mutableStateOf("P0001") }
    
    var cantidad by remember { mutableStateOf("") }
    var unidad by remember { mutableStateOf("") }
    var ubicacion by remember { mutableStateOf("") }
    var proveedor by remember { mutableStateOf("") }
    var costo by remember { mutableStateOf("") }
    var stockMinimo by remember { mutableStateOf("") }
    var fechaIngreso by remember { mutableStateOf("") }
    var notas by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    var nuevaCategoria by remember { mutableStateOf("") }

    // Función para actualizar el código automáticamente
    fun actualizarCodigo(nuevoPrefijo: String) {
        coroutineScope.launch {
            codigoSiguiente = productoViewModel.generarSiguienteCodigo(nuevoPrefijo)
        }
    }

    val excelLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { _ -> }

    val pdfLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { _ -> }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Button(onClick = { navController.popBackStack() }) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
            Text("Regresar")
        }

        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(text = "Agregar Nuevo Producto", style = MaterialTheme.typography.headlineMedium)
                
                if (SessionManager.esAdmin()) {
                    Row {
                        Button(onClick = { pdfLauncher.launch("application/pdf") }) { Text("Leer PDF") }
                        Spacer(modifier = Modifier.width(10.dp))
                        Button(onClick = { excelLauncher.launch("*/*") }) { Text("Importar Excel") }
                    }
                }

                OutlinedTextField(
                    value = descripcion,
                    onValueChange = { descripcion = it },
                    label = { Text("Descripción") },
                    modifier = Modifier.fillMaxWidth()
                )

                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded }
                ) {
                    OutlinedTextField(
                        value = categoriaSeleccionada,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Seleccionar Categoría") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                        modifier = Modifier
                            .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                            .fillMaxWidth()
                    )

                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        categorias.forEach { cat ->
                            DropdownMenuItem(
                                text = { Text(cat.nombre) },
                                onClick = {
                                    categoriaSeleccionada = cat.nombre
                                    prefijoSeleccionado = cat.prefijo
                                    actualizarCodigo(cat.prefijo)
                                    expanded = false
                                }
                            )
                        }
                    }
                }

                // Permite editar el código sugerido (Debajo de categoría)
                OutlinedTextField(
                    value = codigoSiguiente,
                    onValueChange = { codigoSiguiente = it },
                    label = { Text("Código del Producto") },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Ej: P0001") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.1f),
                        unfocusedContainerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.05f)
                    )
                )

                if (SessionManager.esAdmin()) {
                    OutlinedTextField(
                        value = nuevaCategoria,
                        onValueChange = { nuevaCategoria = it },
                        label = { Text("Crear Nueva Categoría") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Button(
                        onClick = {
                            if (nuevaCategoria.isNotEmpty()) {
                                val pfx = nuevaCategoria.take(1).uppercase()
                                categoriaViewModel.insertarCategoria(
                                    categoria(nombre = nuevaCategoria, prefijo = pfx)
                                )
                                categoriaSeleccionada = nuevaCategoria
                                prefijoSeleccionado = pfx
                                actualizarCodigo(pfx)
                                nuevaCategoria = ""
                            }
                        }
                    ) {
                        Icon(Icons.Default.Add, contentDescription = null)
                        Text(" Guardar Categoría")
                    }
                }

                OutlinedTextField(value = cantidad, onValueChange = { cantidad = it }, label = { Text("Cantidad") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = unidad, onValueChange = { unidad = it }, label = { Text("Unidad") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = ubicacion, onValueChange = { ubicacion = it }, label = { Text("Ubicación") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = proveedor, onValueChange = { proveedor = it }, label = { Text("Proveedor") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = costo, onValueChange = { costo = it }, label = { Text("Costo Unitario") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = stockMinimo, onValueChange = { stockMinimo = it }, label = { Text("Stock Mínimo") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = fechaIngreso, onValueChange = { fechaIngreso = it }, label = { Text("Fecha Ingreso") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = notas, onValueChange = { notas = it }, label = { Text("Notas") }, modifier = Modifier.fillMaxWidth())

                if (SessionManager.esAdmin()) {
                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        enabled = categoriaSeleccionada.isNotEmpty(),
                        onClick = {
                            productoViewModel.agregarProducto(
                                producto(
                                    bodegaId = bodegaId,
                                    codigo = codigoSiguiente,
                                    descripcion = descripcion,
                                    categoria = categoriaSeleccionada,
                                    prefijoCategoria = prefijoSeleccionado,
                                    cantidad = cantidad.toIntOrNull() ?: 0,
                                    unidad = unidad,
                                    ubicacion = ubicacion,
                                    proveedor = proveedor,
                                    costo = costo.toDoubleOrNull() ?: 0.0,
                                    stockMinimo = stockMinimo.toIntOrNull() ?: 0,
                                    fechaIngreso = fechaIngreso,
                                    notas = notas
                                )
                            )
                            navController.popBackStack()
                        }
                    ) {
                        Text("Guardar Producto")
                    }
                }
            }
        }
    }
}
