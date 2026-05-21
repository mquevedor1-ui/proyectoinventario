package com.example.inventario.ui.entradas

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.inventario.data.Entrada
import com.example.inventario.ui.AppTopBar
import com.example.inventario.ui.FechaIngresar
import com.example.inventario.viewModel.EntradaViewModel
import com.example.inventario.viewModel.ProductoViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditarEntradaScreen(
    navController: NavController,
    entradaId: Int
) {
    val entradaViewModel: EntradaViewModel = viewModel()
    val productoViewModel: ProductoViewModel = viewModel()
    val scope = rememberCoroutineScope()

    var entradaOriginal by remember { mutableStateOf<Entrada?>(null) }
    
    var codigo by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var cantidad by remember { mutableStateOf("") }
    var proveedor by remember { mutableStateOf("") }
    var categoria by remember { mutableStateOf("") }
    var costo by remember { mutableStateOf("") }
    var numeroFactura by remember { mutableStateOf("") }
    var notas by remember { mutableStateOf("") }
    var unidad by remember { mutableStateOf("") }
    var ubicacion by remember { mutableStateOf("") }
    var fecha by remember { mutableStateOf("") }
    var bodegaId by remember { mutableStateOf("") }

    LaunchedEffect(entradaId) {
        val entrada = entradaViewModel.obtenerEntradaPorId(entradaId)
        if (entrada != null) {
            entradaOriginal = entrada
            codigo = entrada.codigo
            descripcion = entrada.descripcion
            cantidad = entrada.cantidad.toString()
            proveedor = entrada.proveedor
            categoria = entrada.categoria
            costo = entrada.costoUnitario.toString()
            numeroFactura = entrada.numeroFactura
            notas = entrada.notas
            unidad = entrada.unidad
            ubicacion = entrada.ubicacion
            fecha = entrada.fecha
            bodegaId = entrada.bodegaId
        }
    }

    Scaffold(
        topBar = {
            AppTopBar(titulo = "Editar Entrada", navController = navController)
        }
    ) { padding ->
        if (entradaOriginal == null) {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = androidx.compose.ui.Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Card(
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(modifier = Modifier.fillMaxWidth().padding(24.dp)) {
                        Text("Modificar Entrada", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(24.dp))

                        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                            OutlinedTextField(
                                value = codigo,
                                onValueChange = { codigo = it },
                                label = { Text("Código") },
                                modifier = Modifier.weight(1f)
                            )
                            FechaIngresar(
                                fecha = fecha,
                                onFechaChange = { fecha = it },
                                label = "Fecha",
                                modifier = Modifier.weight(1f)
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))
                        OutlinedTextField(
                            value = descripcion,
                            onValueChange = { descripcion = it },
                            label = { Text("Descripción") },
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(16.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                            OutlinedTextField(
                                value = cantidad,
                                onValueChange = { cantidad = it },
                                label = { Text("Cantidad") },
                                modifier = Modifier.weight(1f)
                            )
                            OutlinedTextField(
                                value = costo,
                                onValueChange = { costo = it },
                                label = { Text("Costo") },
                                modifier = Modifier.weight(1f)
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))
                        OutlinedTextField(
                            value = numeroFactura,
                            onValueChange = { numeroFactura = it },
                            label = { Text("Número de Factura") },
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(24.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                            TextButton(onClick = { navController.popBackStack() }) { Text("Cancelar") }
                            Spacer(modifier = Modifier.width(12.dp))
                            Button(onClick = {
                                scope.launch {
                                    val nuevaEntrada = entradaOriginal!!.copy(
                                        codigo = codigo,
                                        cantidad = cantidad.toIntOrNull() ?: 0,
                                        fecha = fecha,
                                        descripcion = descripcion,
                                        costoUnitario = costo.toDoubleOrNull() ?: 0.0,
                                        numeroFactura = numeroFactura,
                                        notas = notas,
                                        proveedor = proveedor,
                                        categoria = categoria,
                                        unidad = unidad,
                                        ubicacion = ubicacion
                                    )
                                    entradaViewModel.actualizarEntrada(nuevaEntrada, entradaOriginal!!)
                                    navController.popBackStack()
                                }
                            }) {
                                Text("Actualizar")
                            }
                        }
                    }
                }
            }
        }
    }
}
