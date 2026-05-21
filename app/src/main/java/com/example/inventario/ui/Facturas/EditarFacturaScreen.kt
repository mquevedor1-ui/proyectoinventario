package com.example.inventario.ui.Facturas

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
import com.example.inventario.data.Factura
import com.example.inventario.ui.AppTopBar
import com.example.inventario.ui.FechaIngresar
import com.example.inventario.viewModel.FacturaViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditarFacturaScreen(
    navController: NavController,
    facturaId: Int
) {
    val viewModel: FacturaViewModel = viewModel()
    val scope = rememberCoroutineScope()

    var facturaOriginal by remember { mutableStateOf<Factura?>(null) }
    
    var numeroFactura by remember { mutableStateOf("") }
    var proveedor by remember { mutableStateOf("") }
    var fecha by remember { mutableStateOf("") }
    var total by remember { mutableStateOf("") }
    var notas by remember { mutableStateOf("") }
    var productos by remember { mutableStateOf("") }

    LaunchedEffect(facturaId) {
        val factura = viewModel.obtenerFacturaPorId(facturaId)
        if (factura != null) {
            facturaOriginal = factura
            numeroFactura = factura.numeroFactura
            proveedor = factura.proveedor
            fecha = factura.fecha
            total = factura.total.toString()
            notas = factura.notas
            productos = factura.productos
        }
    }

    Scaffold(
        topBar = {
            AppTopBar(titulo = "Editar Factura", navController = navController)
        }
    ) { padding ->
        if (facturaOriginal == null) {
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
                        Text("Modificar Factura", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(24.dp))

                        OutlinedTextField(
                            value = numeroFactura,
                            onValueChange = { numeroFactura = it },
                            label = { Text("Número Factura") },
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(16.dp))
                        OutlinedTextField(
                            value = proveedor,
                            onValueChange = { proveedor = it },
                            label = { Text("Proveedor") },
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(16.dp))
                        FechaIngresar(
                            fecha = fecha,
                            onFechaChange = { fecha = it },
                            label = "Fecha",
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(16.dp))
                        OutlinedTextField(
                            value = total,
                            onValueChange = { total = it },
                            label = { Text("Total") },
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(24.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                            TextButton(onClick = { navController.popBackStack() }) { Text("Cancelar") }
                            Spacer(modifier = Modifier.width(12.dp))
                            Button(onClick = {
                                scope.launch {
                                    val nuevaFactura = facturaOriginal!!.copy(
                                        numeroFactura = numeroFactura,
                                        proveedor = proveedor,
                                        fecha = fecha,
                                        total = total.toDoubleOrNull() ?: 0.0,
                                        notas = notas,
                                        productos = productos
                                    )
                                    viewModel.actualizarFactura(nuevaFactura)
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
