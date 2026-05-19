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

import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack

import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

import androidx.lifecycle.viewmodel.compose.viewModel

import androidx.navigation.NavController

import com.example.inventario.data.Salida

import com.example.inventario.viewModel.ProductoViewModel
import com.example.inventario.viewModel.SalidaViewModel

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)

@Composable
fun CrearSalidasScreen(

    navController: NavController,

    bodegaId: String

) {

    // viewmodel

    val salidaViewModel:
            SalidaViewModel = viewModel()

    val productoViewModel:
            ProductoViewModel = viewModel()

    // fecha automatica

    val fechaActual = remember {

        SimpleDateFormat(

            "dd/MM/yyyy",

            Locale.getDefault()

        ).format(Date())
    }

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

        mutableStateOf(fechaActual)
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

            CenterAlignedTopAppBar(

                title = {

                    Text(
                        text = "Registrar Salida"
                    )
                },

                navigationIcon = {

                    IconButton(

                        onClick = {

                            navController
                                .popBackStack()
                        }

                    ) {

                        Icon(

                            imageVector =
                                Icons.Default.ArrowBack,

                            contentDescription =
                                "Regresar"
                        )
                    }
                },

                colors = TopAppBarDefaults
                    .centerAlignedTopAppBarColors(

                        containerColor =
                            MaterialTheme
                                .colorScheme
                                .surface
                    )
            )
        }

    ) { padding ->

        Column(

            modifier = Modifier
                .fillMaxSize()
                .background(

                    MaterialTheme
                        .colorScheme
                        .background
                )
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(

                    rememberScrollState()
                ),

            verticalArrangement =
                Arrangement.spacedBy(12.dp)

        ) {

            // codigo

            OutlinedTextField(

                value = codigo,

                onValueChange = {

                    codigo = it
                },

                label = {

                    Text("Código")
                },

                modifier = Modifier
                    .fillMaxWidth()
            )

            // descripcion

            OutlinedTextField(

                value = descripcion,

                onValueChange = {},

                readOnly = true,

                label = {

                    Text("Descripción")
                },

                modifier = Modifier
                    .fillMaxWidth()
            )

            // categoria

            OutlinedTextField(

                value = categoria,

                onValueChange = {},

                readOnly = true,

                label = {

                    Text("Categoría")
                },

                modifier = Modifier
                    .fillMaxWidth()
            )

            // cantidad

            OutlinedTextField(

                value = cantidad,

                onValueChange = {

                    cantidad = it
                },

                label = {

                    Text("Cantidad")
                },

                modifier = Modifier
                    .fillMaxWidth()
            )

            // fecha

            OutlinedTextField(

                value = fecha,

                onValueChange = {},

                readOnly = true,

                label = {

                    Text("Fecha")
                },

                modifier = Modifier
                    .fillMaxWidth()
            )

            // responsable

            OutlinedTextField(

                value = responsable,

                onValueChange = {

                    responsable = it
                },

                label = {

                    Text("Quién lo lleva")
                },

                modifier = Modifier
                    .fillMaxWidth()
            )

            // destino

            OutlinedTextField(

                value = destino,

                onValueChange = {

                    destino = it
                },

                label = {

                    Text("Para qué / Destino")
                },

                modifier = Modifier
                    .fillMaxWidth()
            )

            // vehiculo

            OutlinedTextField(

                value = vehiculo,

                onValueChange = {

                    vehiculo = it
                },

                label = {

                    Text("Vehículo / Tractor / Camión")
                },

                modifier = Modifier
                    .fillMaxWidth()
            )

            // notas

            OutlinedTextField(

                value = notas,

                onValueChange = {

                    notas = it
                },

                label = {

                    Text("Notas")
                },

                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
            )

            Spacer(

                modifier =
                    Modifier.height(12.dp)
            )

            // botones

            Row(

                modifier = Modifier
                    .fillMaxWidth(),

                horizontalArrangement =
                    Arrangement.spacedBy(12.dp)

            ) {

                Button(

                    onClick = {

                        if (

                            codigo.isNotEmpty()
                            &&
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
                                        cantidad.toInt(),

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

                            // guardar

                            salidaViewModel
                                .agregarSalida(
                                    nuevaSalida
                                )

                            // regresar

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
                                        .error
                            )

                ) {

                    Text("Registrar")
                }

                Button(

                    onClick = {

                        navController
                            .popBackStack()
                    }

                ) {

                    Text("Cancelar")
                }
            }
        }
    }
}