package com.example.inventario.ui.Facturas

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar

import androidx.compose.runtime.*

import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

import androidx.navigation.NavController

import com.example.inventario.ui.FechaIngresar

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.inventario.ui.AppTopBar
import com.example.inventario.viewModel.FacturaViewModel
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FacturasScreen(
    navController: NavController,
    bodegaId: String
) {
    val context = LocalContext.current
    val viewModel: FacturaViewModel = viewModel()
    val facturas by viewModel.obtenerFacturasPorBodega(bodegaId).collectAsState(initial = emptyList())

    LaunchedEffect(Unit) {
        viewModel.sincronizarDesdeFirebase()
    }

    Scaffold(
        topBar = {
            AppTopBar(
                titulo = "Reporte de Facturas",
                navController = navController
            )
        },
        containerColor = MaterialTheme.colorScheme.background,
        floatingActionButton = {
            Button(
                onClick = {
                    navController.navigate("crearFactura/$bodegaId")
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = null
                )
                Text(" Registrar Factura")
            }
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Facturas de la bodega: $bodegaId",
                    color = MaterialTheme.colorScheme.onBackground
                )

                Button(
                    onClick = { exportarFacturasPDF(context, facturas) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondary
                    )
                ) {
                    Icon(Icons.Default.PictureAsPdf, contentDescription = null)
                    Spacer(Modifier.width(4.dp))
                    Text("PDF")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (facturas.isEmpty()) {
                Text(
                    "No hay facturas registradas",
                    color = MaterialTheme.colorScheme.onBackground
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(facturas) { factura ->
                        androidx.compose.material3.Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surface
                            ),
                            elevation = CardDefaults.cardElevation(2.dp)
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(
                                    "N°: ${factura.numeroFactura}",
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Text(
                                    "Proveedor: ${factura.proveedor}",
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    "Fecha: ${factura.fecha}",
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    "Total: $ ${factura.total}",
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }
                }
            }
        }

        if (false) {
            RegistrarFacturaDialog(
                onDismiss = {
                    // mostrarDialog = false
                }
            )
        }
    }
}

@Composable
fun RegistrarFacturaDialog(

    onDismiss: () -> Unit

) {

    var numeroFactura by remember {

        mutableStateOf("")
    }

    var fecha by remember {

        mutableStateOf("")
    }

    var proveedor by remember {

        mutableStateOf("")
    }

    var total by remember {

        mutableStateOf("")
    }

    var productos by remember {

        mutableStateOf("")
    }

    var notas by remember {

        mutableStateOf("")
    }

    AlertDialog(

        onDismissRequest = {

            onDismiss()
        },

        title = {

            Text(
                "Registrar Nueva Factura"
            )
        },

        text = {

            Column(

                verticalArrangement =
                    Arrangement.spacedBy(10.dp)

            ) {

                // numero factura
                OutlinedTextField(

                    value =
                        numeroFactura,

                    onValueChange = {

                        numeroFactura = it
                    },

                    label = {

                        Text(
                            "Número Factura"
                        )
                    },

                    modifier =
                        Modifier.fillMaxWidth()
                )

                // fecha
                FechaIngresar(

                    fecha = fecha,

                    onFechaChange = {

                        fecha = it
                    }
                )

                // proveedor
                OutlinedTextField(

                    value =
                        proveedor,

                    onValueChange = {

                        proveedor = it
                    },

                    label = {

                        Text(
                            "Proveedor"
                        )
                    },

                    modifier =
                        Modifier.fillMaxWidth()
                )

                // total
                OutlinedTextField(

                    value =
                        total,

                    onValueChange = {

                        total = it
                    },

                    label = {

                        Text(
                            "Monto Total"
                        )
                    },

                    modifier =
                        Modifier.fillMaxWidth()
                )

                // productos
                OutlinedTextField(

                    value =
                        productos,

                    onValueChange = {

                        productos = it
                    },

                    label = {

                        Text(
                            "Productos"
                        )
                    },

                    modifier =
                        Modifier.fillMaxWidth()
                )

                // notas
                OutlinedTextField(

                    value =
                        notas,

                    onValueChange = {

                        notas = it
                    },

                    label = {

                        Text(
                            "Notas"
                        )
                    },

                    modifier =
                        Modifier.fillMaxWidth()
                )
            }
        },

        confirmButton = {

            Button(

                onClick = {

                    onDismiss()
                }

            ) {

                Text(
                    "Guardar"
                )
            }
        },

        dismissButton = {

            Button(

                onClick = {

                    onDismiss()
                }

            ) {

                Text(
                    "Cancelar"
                )
            }
        }
    )
}