package com.example.inventario.ui.Facturas

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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

import com.example.inventario.data.Factura
import com.example.inventario.ui.AppTopBar
import com.example.inventario.ui.inventario.importarFacturasExcel
import com.example.inventario.viewModel.FacturaViewModel
import com.example.inventario.viewModel.SessionManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FacturasScreen(
    navController: NavController,
    bodegaId: String
) {
    val context = LocalContext.current
    val viewModel: FacturaViewModel = viewModel()

    // Launcher para importar Facturas desde Excel
    val launcherImportar = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            val facturasImportadas = importarFacturasExcel(context, it, bodegaId)
            facturasImportadas.forEach { f ->
                viewModel.agregarFactura(f)
            }
        }
    }

    val searchQuery by viewModel.searchQuery.collectAsState()
    val filtroPeriodo by viewModel.filtroPeriodo.collectAsState()
    val periodoTexto by viewModel.periodoTexto.collectAsState()
    val fechaReferencia by viewModel.fechaReferencia.collectAsState()
    val facturas by viewModel.obtenerFacturasFiltradas(bodegaId).collectAsState(initial = emptyList())

    var showDatePicker by remember { mutableStateOf(false) }

    if (showDatePicker) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = fechaReferencia.timeInMillis
        )

        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },

            confirmButton = {

                TextButton(
                    onClick = {

                        datePickerState.selectedDateMillis?.let { millis ->

                            val cal = Calendar.getInstance().apply {

                                timeInMillis =
                                    millis + (1000 * 60 * 60 * 24)
                            }

                            viewModel.setFechaReferencia(cal)
                        }

                        showDatePicker = false
                    }
                ) {

                    Text("Aceptar")
                }
            },

            dismissButton = {

                TextButton(
                    onClick = { showDatePicker = false }
                ) {

                    Text("Cancelar")
                }
            }

        ) {

            DatePicker(state = datePickerState)
        }
    }

    LaunchedEffect(Unit) {
        viewModel.sincronizarDesdeFirebase()
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,

        topBar = {

            AppTopBar(
                titulo = "Historial de Facturas",
                subtitulo = "Bodega: $bodegaId",
                navController = navController
            )
        },

        floatingActionButton = {

            if (
                SessionManager.esAdmin()
                ||
                SessionManager.rolUsuario() == "encargado"
            ) {

                FloatingActionButton(

                    onClick = {

                        navController.navigate(
                            "crearFactura/$bodegaId"
                        )
                    },

                    containerColor = MaterialTheme.colorScheme.primary,

                    contentColor = MaterialTheme.colorScheme.onPrimary

                ) {

                    Icon(
                        Icons.Default.Add,
                        contentDescription = "Registrar"
                    )
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

            // BUSCADOR

            OutlinedTextField(

                value = searchQuery,

                onValueChange = {
                    viewModel.setSearchQuery(it)
                },

                modifier = Modifier.fillMaxWidth(),

                placeholder = {
                    Text("Buscar por N° Factura o Proveedor...")
                },

                leadingIcon = {

                    Icon(
                        Icons.Default.Search,
                        contentDescription = null
                    )
                },

                shape = RoundedCornerShape(12.dp),

                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            // SELECTOR PERIODO

            PeriodoTabsFactura(filtroPeriodo) {

                viewModel.setFiltroPeriodo(it)
            }

            Text(

                text = periodoTexto,

                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .clickable {

                        if (filtroPeriodo != "Todo") {

                            showDatePicker = true
                        }
                    },

                textAlign = TextAlign.Center,

                style = MaterialTheme.typography.labelLarge,

                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(8.dp))

            // BOTONES

            Row(
                modifier = Modifier.fillMaxWidth()
            ) {

                Button(

                    onClick = {

                        exportarFacturasPDF(
                            context,
                            facturas,
                            periodoTexto
                        )
                    },

                    modifier = Modifier.weight(1f),

                    shape = RoundedCornerShape(12.dp)

                ) {

                    Icon(
                        Icons.Default.PictureAsPdf,
                        contentDescription = null
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text("PDF")
                }

                Spacer(modifier = Modifier.width(8.dp))

                Button(

                    onClick = {

                        exportarFacturasExcel(
                            context,
                            facturas,
                            periodoTexto
                        )
                    },

                    modifier = Modifier.weight(1f),

                    shape = RoundedCornerShape(12.dp),

                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF1D6F42)
                    )

                ) {

                    Icon(
                        Icons.Default.TableChart,
                        contentDescription = null
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text("Excel")
                }
            }

            // IMPORTAR EXCEL

            if (SessionManager.esAdmin()) {

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedButton(

                    onClick = {

                        launcherImportar.launch("*/*")
                    },

                    modifier = Modifier.fillMaxWidth(),

                    shape = RoundedCornerShape(12.dp)

                ) {

                    Icon(
                        Icons.Default.FileUpload,
                        contentDescription = null
                    )

                    Spacer(Modifier.width(8.dp))

                    Text("Importar Facturas desde Excel")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (facturas.isEmpty()) {

                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {

                    Text(
                        "No se encontraron facturas",
                        color = Color.Gray
                    )
                }

            } else {

                LazyColumn(

                    verticalArrangement =
                        Arrangement.spacedBy(12.dp),

                    modifier = Modifier.fillMaxSize()

                ) {

                    items(facturas) { factura ->

                        FacturaCardItem(
                            factura,
                            viewModel,
                            navController
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun PeriodoTabsFactura(
    periodoSeleccionado: String,
    onPeriodoSelected: (String) -> Unit
) {

    val opciones =
        listOf("Dia", "Semana", "Mes", "Año", "Todo")

    Row(

        modifier = Modifier
            .fillMaxWidth()
            .background(
                MaterialTheme.colorScheme.surfaceVariant,
                RoundedCornerShape(12.dp)
            )
            .padding(4.dp),

        horizontalArrangement =
            Arrangement.SpaceEvenly

    ) {

        opciones.forEach { opcion ->

            val seleccionado =
                periodoSeleccionado == opcion

            Box(

                modifier = Modifier
                    .weight(1f)
                    .background(
                        if (seleccionado)
                            MaterialTheme.colorScheme.primary
                        else
                            Color.Transparent,
                        RoundedCornerShape(8.dp)
                    )
                    .clickable {
                        onPeriodoSelected(opcion)
                    }
                    .padding(vertical = 8.dp),

                contentAlignment = Alignment.Center

            ) {

                Text(

                    text = opcion,

                    color =
                        if (seleccionado)
                            MaterialTheme.colorScheme.onPrimary
                        else
                            MaterialTheme.colorScheme.onSurfaceVariant,

                    fontWeight =
                        if (seleccionado)
                            FontWeight.Bold
                        else
                            FontWeight.Normal,

                    fontSize = 13.sp
                )
            }
        }
    }
}
@Composable
fun FacturaCardItem(

    factura: Factura,

    viewModel: FacturaViewModel,

    navController: NavController

) {

    Card(

        modifier = Modifier.fillMaxWidth(),

        shape = RoundedCornerShape(12.dp),

        elevation =
            CardDefaults.cardElevation(
                defaultElevation = 2.dp
            )

    ) {

        Column(

            modifier =
                Modifier.padding(16.dp)

        ) {

            Row(

                modifier =
                    Modifier.fillMaxWidth(),

                horizontalArrangement =
                    Arrangement.SpaceBetween,

                verticalAlignment =
                    Alignment.CenterVertically

            ) {

                Column(

                    modifier =
                        Modifier.weight(1f)

                ) {

                    Text(

                        text =
                            "Factura N°: ${factura.numeroFactura}",

                        fontWeight =
                            FontWeight.Bold,

                        fontSize = 16.sp,

                        color =
                            MaterialTheme.colorScheme.primary
                    )

                    Text(

                        text =
                            "Proveedor: ${factura.proveedor}",

                        fontSize = 13.sp,

                        color = Color.Gray
                    )
                }

                Text(

                    text =
                        "$ ${String.format("%.2f", factura.total)}",

                    color =
                        MaterialTheme.colorScheme.onSurface,

                    fontWeight =
                        FontWeight.ExtraBold,

                    fontSize = 18.sp
                )
            }
        }
    }
}