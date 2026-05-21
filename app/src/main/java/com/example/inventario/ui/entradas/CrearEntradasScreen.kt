package com.example.inventario.ui.entradas
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

import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

import androidx.compose.runtime.rememberCoroutineScope

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

import java.util.Calendar

import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)

@Composable
fun CrearEntradasScreen(

    navController: NavController,

    bodegaId: String

) {

    val entradaViewModel:
            EntradaViewModel = viewModel()

    val productoViewModel:
            ProductoViewModel = viewModel()

    val scope =
        rememberCoroutineScope()

    // codigo

    var codigo by remember {

        mutableStateOf("")
    }

    // descripcion

    var descripcion by remember {

        mutableStateOf("")
    }

    // cantidad

    var cantidad by remember {

        mutableStateOf("")
    }

    // proveedor

    var proveedor by remember {

        mutableStateOf("")
    }

    // categoria

    var categoria by remember {

        mutableStateOf("")
    }

    // costo

    var costo by remember {

        mutableStateOf("")
    }

    // stock bajo (mínimo)
    var stockMinimo by remember {
        mutableStateOf("")
    }

    // numero factura
    var numeroFactura by remember {
        mutableStateOf("")
    }

    // notas

    var notas by remember {

        mutableStateOf("")
    }

    // unidad

    var unidad by remember {

        mutableStateOf("")
    }

    // ubicacion

    var ubicacion by remember {

        mutableStateOf("")
    }

    // fecha

    var fecha by remember {

        val c = Calendar.getInstance()

        mutableStateOf(

            "${c.get(Calendar.DAY_OF_MONTH)}/" +
                    "${c.get(Calendar.MONTH) + 1}/" +
                    "${c.get(Calendar.YEAR)}"
        )
    }

    // autocompletar

    LaunchedEffect(codigo) {

        if (

            codigo.length >= 2

        ) {

            val producto =

                productoViewModel
                    .obtenerProductoPorCodigo(
                        codigo
                    )

            if (

                producto != null

            ) {

                descripcion =
                    producto.descripcion

                categoria =
                    producto.categoria

                costo =
                    producto.costo.toString()

                stockMinimo =
                    producto.stockMinimo.toString()

                proveedor =
                    producto.proveedor

                unidad =
                    producto.unidad

                ubicacion =
                    producto.ubicacion

                notas =
                    producto.notas
            }
        }
    }

    Scaffold(

        topBar = {

            AppTopBar(

                titulo =
                    "Registrar Entrada",

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
                    )

            ) {

                Column(

                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp)

                ) {

                    Text(

                        text =
                            "Registrar Nueva Entrada",

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
                            "Complete los datos de la entrada",

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
                            .fillMaxWidth()
                            .height(100.dp)
                    )

                    Spacer(
                        modifier =
                            Modifier.height(16.dp)
                    )

                    // cantidad y costo

                    Row(

                        horizontalArrangement =
                            Arrangement.spacedBy(16.dp)

                    ) {

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

                        OutlinedTextField(

                            value =
                                costo,

                            onValueChange = {

                                costo = it
                            },

                            label = {

                                Text(
                                    "Costo Unitario"
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

                    // Stock Bajo y Factura
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        OutlinedTextField(
                            value = stockMinimo,
                            onValueChange = { stockMinimo = it },
                            label = { Text("Stock Bajo (Alerta)") },
                            modifier = Modifier.weight(1f)
                        )

                        OutlinedTextField(
                            value = numeroFactura,
                            onValueChange = { numeroFactura = it },
                            label = { Text("N° Factura") },
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(
                        modifier =
                            Modifier.height(16.dp)
                    )

                    // unidad y ubicacion

                    Row(

                        horizontalArrangement =
                            Arrangement.spacedBy(16.dp)

                    ) {

                        OutlinedTextField(

                            value =
                                unidad,

                            onValueChange = {

                                unidad = it
                            },

                            label = {

                                Text(
                                    "Unidad"
                                )
                            },

                            modifier =
                                Modifier.weight(1f)
                        )

                        OutlinedTextField(

                            value =
                                ubicacion,

                            onValueChange = {

                                ubicacion = it
                            },

                            label = {

                                Text(
                                    "Ubicación"
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

                    // proveedor y categoria

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
                                categoria,

                            onValueChange = {},

                            readOnly = true,

                            label = {

                                Text(
                                    "Categoría"
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

                                scope.launch {

                                    val entrada = Entrada(

                                        codigo =
                                            codigo,

                                        cantidad =
                                            cantidad.toIntOrNull()
                                                ?: 0,

                                        fecha =
                                            fecha,

                                        notas =
                                            notas,

                                        descripcion =
                                            descripcion,

                                        proveedor =
                                            proveedor,

                                        categoria =
                                            categoria,

                                        unidad =
                                            unidad,

                                        ubicacion =
                                            ubicacion,

                                        costoUnitario =
                                            costo.toDoubleOrNull()
                                                ?: 0.0,

                                        stockMinimo =
                                            stockMinimo.toIntOrNull()
                                                ?: 0,

                                        numeroFactura =
                                            numeroFactura,

                                        bodegaId =
                                            bodegaId
                                    )

                                    // guardar entrada y actualizar inventario
                                    entradaViewModel
                                        .agregarEntrada(
                                            entrada
                                        )

                                    navController
                                        .popBackStack()
                                }
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