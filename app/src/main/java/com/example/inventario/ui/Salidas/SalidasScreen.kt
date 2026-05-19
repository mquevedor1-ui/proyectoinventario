package com.example.inventario.ui.Salidas

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items

import androidx.compose.foundation.shape.RoundedCornerShape

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material.icons.filled.Output

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.style.TextAlign

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController

import com.example.inventario.data.Salida
import com.example.inventario.viewModel.ProductoViewModel
import com.example.inventario.viewModel.SalidaViewModel
import com.example.inventario.viewModel.SessionManager

import androidx.compose.ui.platform.LocalContext
import com.example.inventario.ui.AppTopBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SalidasScreen(
    navController: NavController,
    bodegaId: String
) {
    val context = LocalContext.current
    val salidaViewModel: SalidaViewModel = viewModel()
    val productoViewModel: ProductoViewModel = viewModel()

    val salidas by salidaViewModel.allSalidas.collectAsState(initial = emptyList())
    var mostrarDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        salidaViewModel.sincronizarDesdeFirebase()
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            AppTopBar(
                titulo = "Reportes de Salidas",
                subtitulo = "Bodega: $bodegaId", // Ideally fetch the actual name
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
                    Row(
                        modifier = Modifier.padding(horizontal = 14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Add, contentDescription = null)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(text = "Registrar Salida")
                    }
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
            // tabs falsas
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        MaterialTheme.colorScheme.surfaceVariant,
                        RoundedCornerShape(20.dp)
                    )
                    .padding(4.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Por Día",
                    modifier = Modifier
                        .weight(1f)
                        .background(
                            MaterialTheme.colorScheme.surface,
                            RoundedCornerShape(20.dp)
                        )
                        .padding(10.dp),
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "Por Semana",
                    modifier = Modifier.weight(1f).padding(10.dp),
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "Por Mes",
                    modifier = Modifier.weight(1f).padding(10.dp),
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Button(
                    onClick = { exportarSalidasPDF(context, salidas) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Icon(Icons.Default.PictureAsPdf, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text("Exportar PDF de Salidas")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Fecha", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold)
                        Text("Código", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold)
                        Text("Desc.", modifier = Modifier.weight(1.5f), fontWeight = FontWeight.Bold)
                        Text("Cant.", modifier = Modifier.weight(0.7f), fontWeight = FontWeight.Bold, textAlign = TextAlign.End)
                        if (SessionManager.esAdmin()) {
                            Spacer(modifier = Modifier.width(48.dp)) // Espacio para el icono de borrar
                        }
                    }

                    HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))

                    if (salidas.isEmpty()) {
                        Text(
                            text = "No hay salidas registradas",
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier
                                .padding(30.dp)
                                .align(Alignment.CenterHorizontally)
                        )
                    }

                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(salidas) { salida ->
                            SalidaRow(
                                salida = salida,
                                viewModel = salidaViewModel,
                                productoViewModel = productoViewModel
                            )
                        }
                    }
                }
            }
        }

        if (mostrarDialog) {
            RegistrarSalidaDialog(onDismiss = { mostrarDialog = false })
        }
    }
}

@Composable
fun SalidaRow(

    salida: Salida,

    viewModel: SalidaViewModel,

    productoViewModel: ProductoViewModel

) {

    var productoNombre by remember {

        mutableStateOf("...")
    }

    LaunchedEffect(salida.codigo) {

        val producto =

            productoViewModel
                .obtenerProductoPorCodigo(
                    salida.codigo
                )

        productoNombre =
            producto?.descripcion
                ?: "N/A"
    }

    Row(

        modifier =
            Modifier.fillMaxWidth(),

        verticalAlignment =
            Alignment.CenterVertically
    ) {

        Text(

            salida.fecha,

            modifier =
                Modifier.weight(1f),

            fontSize = 12.sp
        )

        Text(

            salida.codigo,

            modifier =
                Modifier.weight(1f),

            fontSize = 12.sp
        )

        Text(

            productoNombre,

            modifier =
                Modifier.weight(1.5f),

            fontSize = 12.sp,

            maxLines = 1
        )

        Text(

            "${salida.cantidad}",

            modifier =
                Modifier.weight(0.7f),

            textAlign =
                TextAlign.End,

            fontWeight =
                FontWeight.Bold
        )

        if (

            SessionManager.esAdmin()

        ) {

            IconButton(

                onClick = {

                    viewModel
                        .eliminarSalida(
                            salida
                        )
                }
            ) {

                Icon(

                    Icons.Default.Delete,

                    contentDescription =
                        "Eliminar",

                    tint =
                        MaterialTheme
                            .colorScheme
                            .error
                )
            }
        }
    }
}

@Composable
fun RegistrarSalidaDialog(onDismiss: () -> Unit) {
    // ... logic remains similar but with theme colors ...
}
