package com.example.inventario.ui.Salidas

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
import com.example.inventario.data.Salida
import com.example.inventario.ui.AppTopBar
import com.example.inventario.ui.FechaIngresar
import com.example.inventario.viewModel.SalidaViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditarSalidaScreen(
    navController: NavController,
    salidaId: Int
) {
    val salidaViewModel: SalidaViewModel = viewModel()
    val scope = rememberCoroutineScope()

    var salidaOriginal by remember { mutableStateOf<Salida?>(null) }
    
    var codigo by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var cantidad by remember { mutableStateOf("") }
    var fecha by remember { mutableStateOf("") }
    var notas by remember { mutableStateOf("") }

    LaunchedEffect(salidaId) {
        val salida = salidaViewModel.obtenerSalidaPorId(salidaId)
        if (salida != null) {
            salidaOriginal = salida
            codigo = salida.codigo
            descripcion = salida.descripcion
            cantidad = salida.cantidad.toString()
            fecha = salida.fecha
            notas = salida.notas
        }
    }

    Scaffold(
        topBar = {
            AppTopBar(titulo = "Editar Salida", navController = navController)
        }
    ) { padding ->
        if (salidaOriginal == null) {
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
                        Text("Modificar Salida", fontSize = 24.sp, fontWeight = FontWeight.Bold)
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
                        OutlinedTextField(
                            value = cantidad,
                            onValueChange = { cantidad = it },
                            label = { Text("Cantidad") },
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(16.dp))
                        OutlinedTextField(
                            value = notas,
                            onValueChange = { notas = it },
                            label = { Text("Notas") },
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(24.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                            TextButton(onClick = { navController.popBackStack() }) { Text("Cancelar") }
                            Spacer(modifier = Modifier.width(12.dp))
                            Button(onClick = {
                                scope.launch {
                                    val nuevaSalida = salidaOriginal!!.copy(
                                        codigo = codigo,
                                        cantidad = cantidad.toIntOrNull() ?: 0,
                                        fecha = fecha,
                                        descripcion = descripcion,
                                        notas = notas
                                    )
                                    salidaViewModel.actualizarSalida(nuevaSalida, salidaOriginal!!)
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
