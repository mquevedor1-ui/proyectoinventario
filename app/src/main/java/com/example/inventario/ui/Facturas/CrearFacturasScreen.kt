package com.example.inventario.ui.Facturas

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width

import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll

import androidx.compose.foundation.shape.RoundedCornerShape

import androidx.compose.material3.*

import androidx.compose.runtime.*

import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import androidx.lifecycle.viewmodel.compose.viewModel

import androidx.navigation.NavController

import com.example.inventario.ui.AppTopBar
import com.example.inventario.ui.FechaIngresar

import com.example.inventario.data.Factura

import com.example.inventario.viewModel.FacturaViewModel
import com.example.inventario.viewModel.EntradaViewModel

import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)

@Composable
fun CrearFacturasScreen(

    navController: NavController,

    bodegaId: String

) {

    val viewModel:
            FacturaViewModel = viewModel()

    val entradaViewModel:
            EntradaViewModel = viewModel()

    var codigo by remember {
        mutableStateOf("")
    }

    var descripcion by remember {
        mutableStateOf("")
    }

    var cantidad by remember {
        mutableStateOf("")
    }

    var numeroFactura by remember {
        mutableStateOf("")
    }

    var fecha by remember {

        val c = Calendar.getInstance()

        mutableStateOf(

            "${c.get(Calendar.DAY_OF_MONTH)}/" +
                    "${c.get(Calendar.MONTH) + 1}/" +
                    "${c.get(Calendar.YEAR)}"
        )
    }

    var proveedor by remember {
        mutableStateOf("")
    }

    var total by remember {
        mutableStateOf("")
    }

    var notas by remember {
        mutableStateOf("")
    }

    LaunchedEffect(codigo) {

        if (codigo.length >= 3) {

            val p =

                entradaViewModel
                    .buscarProductoPorCodigo(
                        codigo
                    )

            if (p != null) {

                descripcion =
                    p.descripcion

                proveedor =
                    p.proveedor
            }
        }
    }

    Scaffold(

        topBar = {

            AppTopBar(

                titulo =
                    "Registrar Factura",

                navController =
                    navController
            )
        },

        containerColor =
            MaterialTheme
                .colorScheme
                .background

    ) { padding ->

        Column(

            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(20.dp)
                .verticalScroll(
                    rememberScrollState()
                )
        ) {

            Card(

                shape =
                    RoundedCornerShape(24.dp),

                colors =
                    CardDefaults.cardColors(

                        containerColor =
                            MaterialTheme
                                .colorScheme
                                .surface
                    ),

                elevation =
                    CardDefaults.cardElevation(
                        4.dp
                    )
            ) {

                Column(

                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp)

                ) {

                    Text(

                        text =
                            "Registrar Nueva Factura",

                        fontSize = 28.sp,

                        fontWeight =
                            FontWeight.Bold
                    )

                    Spacer(
                        modifier =
                            Modifier.height(8.dp)
                    )

                    Text(

                        text =
                            "Complete los datos de la factura",

                        color =
                            MaterialTheme
                                .colorScheme
                                .onSurfaceVariant
                    )

                    Spacer(
                        modifier =
                            Modifier.height(24.dp)
                    )

                    // codigo y fecha

                    Row(

                        horizontalArrangement =
                            Arrangement.spacedBy(16.dp)

                    ) {

                        OutlinedTextField(

                            value =
                                codigo,

                            onValueChange = {

                                codigo = it
                            },

                            label = {

                                Text(
                                    "Código Producto"
                                )
                            },

                            modifier =
                                Modifier.weight(1f)
                        )

                        FechaIngresar(

                            fecha = fecha,

                            onFechaChange = {

                                fecha = it
                            },

                            label = "Fecha",

                            modifier =
                                Modifier.weight(1f)
                        )
                    }

                    Spacer(
                        modifier =
                            Modifier.height(16.dp)
                    )

                    // descripcion

                    OutlinedTextField(

                        value =
                            descripcion,

                        onValueChange = {

                            descripcion = it
                        },

                        label = {

                            Text(
                                "Descripción"
                            )
                        },

                        modifier = Modifier
                            .fillMaxWidth(),

                        readOnly = true
                    )

                    Spacer(
                        modifier =
                            Modifier.height(16.dp)
                    )

                    // numero factura y cantidad

                    Row(

                        horizontalArrangement =
                            Arrangement.spacedBy(16.dp)

                    ) {

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
                                Modifier.weight(1f)
                        )

                        OutlinedTextField(

                            value =
                                cantidad,

                            onValueChange = {

                                cantidad = it
                            },

                            label = {

                                Text(
                                    "Cantidad"
                                )
                            },

                            modifier =
                                Modifier.weight(1f)
                        )
                    }

                    Spacer(
                        modifier =
                            Modifier.height(16.dp)
                    )

                    // proveedor y total

                    Row(

                        horizontalArrangement =
                            Arrangement.spacedBy(16.dp)

                    ) {

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
                                Modifier.weight(1f)
                        )

                        OutlinedTextField(

                            value =
                                total,

                            onValueChange = {

                                total = it
                            },

                            label = {

                                Text(
                                    "Total"
                                )
                            },

                            modifier =
                                Modifier.weight(1f)
                        )
                    }

                    Spacer(
                        modifier =
                            Modifier.height(16.dp)
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

                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp)
                    )

                    Spacer(
                        modifier =
                            Modifier.height(24.dp)
                    )

                    // botones

                    Row(

                        modifier =
                            Modifier.fillMaxWidth(),

                        horizontalArrangement =
                            Arrangement.End

                    ) {

                        TextButton(

                            onClick = {

                                navController
                                    .popBackStack()
                            }

                        ) {

                            Text(
                                "Cancelar"
                            )
                        }

                        Spacer(
                            modifier =
                                Modifier.width(12.dp)
                        )

                        Button(

                            onClick = {

                                val factura = Factura(

                                    numeroFactura =
                                        numeroFactura,

                                    fecha =
                                        fecha,

                                    proveedor =
                                        proveedor,

                                    total =
                                        total.toDoubleOrNull()
                                            ?: 0.0,

                                    codigo =
                                        codigo,

                                    productos =
                                        "$cantidad - $descripcion",

                                    notas =
                                        notas,

                                    bodegaId =
                                        bodegaId
                                )

                                viewModel
                                    .agregarFactura(
                                        factura
                                    )

                                navController
                                    .popBackStack()
                            },

                            colors =
                                ButtonDefaults
                                    .buttonColors(

                                        containerColor =
                                            MaterialTheme
                                                .colorScheme
                                                .primary
                                    )
                        ) {

                            Text(
                                "Registrar"
                            )
                        }
                    }
                }
            }
        }
    }
}