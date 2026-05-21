package com.example.inventario.ui.Salidas

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

import com.example.inventario.data.Salida

import com.example.inventario.ui.AppTopBar
import com.example.inventario.ui.FechaIngresar

import com.example.inventario.viewModel.ProductoViewModel
import com.example.inventario.viewModel.SalidaViewModel

import java.util.Calendar

import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)

@Composable
fun CrearSalidasScreen(

    navController: NavController,

    bodegaId: String

) {

    val salidaViewModel:
            SalidaViewModel = viewModel()

    val productoViewModel:
            ProductoViewModel = viewModel()

    val scope =
        rememberCoroutineScope()

    // estados

    var codigo by remember {

        mutableStateOf("")
    }

    var descripcion by remember {

        mutableStateOf("")
    }

    var categoria by remember {

        mutableStateOf("")
    }

    var cantidad by remember {

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

    var responsable by remember {

        mutableStateOf("")
    }

    var destino by remember {

        mutableStateOf("")
    }

    var vehiculo by remember {

        mutableStateOf("")
    }

    var notas by remember {

        mutableStateOf("")
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

            if (producto != null) {

                descripcion =
                    producto.descripcion

                categoria =
                    producto.categoria
            }
        }
    }

    Scaffold(

        topBar = {

            AppTopBar(

                titulo =
                    "Registrar Salida",

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
                            "Registrar Nueva Salida",

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
                            "Complete los datos de la salida",

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

                        onValueChange = {},

                        readOnly = true,

                        label = {

                            Text(
                                "Descripción"
                            )
                        },

                        modifier = Modifier
                            .fillMaxWidth()
                    )

                    Spacer(
                        modifier =
                            Modifier.height(16.dp)
                    )

                    // cantidad y categoria

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

                    // responsable y destino

                    Row(

                        horizontalArrangement =
                            Arrangement.spacedBy(16.dp)

                    ) {

                        OutlinedTextField(

                            value =
                                responsable,

                            onValueChange = {

                                responsable = it
                            },

                            label = {

                                Text(
                                    "Quién lo lleva"
                                )
                            },

                            modifier =
                                Modifier.weight(1f)
                        )

                        OutlinedTextField(

                            value =
                                destino,

                            onValueChange = {

                                destino = it
                            },

                            label = {

                                Text(
                                    "Para qué / Destino"
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

                    // vehiculo

                    OutlinedTextField(

                        value =
                            vehiculo,

                        onValueChange = {

                            vehiculo = it
                        },

                        label = {

                            Text(
                                "Vehículo / Tractor / Camión"
                            )
                        },

                        modifier =
                            Modifier.fillMaxWidth()
                    )

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

                                    if (

                                        codigo.isNotEmpty() &&
                                        cantidad.isNotEmpty()

                                    ) {

                                        val nuevaSalida =

                                            Salida(

                                                codigo =
                                                    codigo,

                                                descripcion =
                                                    descripcion,

                                                categoria =
                                                    categoria,

                                                cantidad =
                                                    cantidad.toIntOrNull()
                                                        ?: 0,

                                                responsable =
                                                    responsable,

                                                destino =
                                                    destino,

                                                vehiculo =
                                                    vehiculo,

                                                fecha =
                                                    fecha,

                                                notas =
                                                    notas,

                                                bodegaId =
                                                    bodegaId
                                            )

                                        // guardar salida y actualizar stock (vía ViewModel)
                                        salidaViewModel
                                            .agregarSalida(
                                                nuevaSalida
                                            )

                                        navController
                                            .popBackStack()
                                    }
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
