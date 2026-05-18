package com.example.inventario.ui.menu



import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding

import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Settings

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

import androidx.compose.ui.Modifier

import androidx.compose.ui.graphics.Brush

import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import androidx.navigation.NavController

import com.example.inventario.data.Bodega
import com.example.inventario.viewModel.BodegaViewModel
import com.example.inventario.viewModel.SessionManager

@Composable
fun MainMenuScreen(

    navController: NavController,

    viewModel: BodegaViewModel

) {

    // admin
    val esAdmin = SessionManager.esAdmin()

    // lista
    val bodegas by viewModel.bodegas.collectAsState(

        initial = emptyList<Bodega>()
    )

    // crear
    var mostrarCrear by remember {

        mutableStateOf(false)
    }

    var nuevaBodega by remember {

        mutableStateOf("")
    }

    // editar
    var mostrarEditar by remember {
        mutableStateOf(false)
    }
    var nombreEditar by remember {
        mutableStateOf("")
    }
    var bodegaSeleccionada by remember {
        mutableStateOf<Bodega?>(null)
    }

    Column(

        modifier = Modifier
            .fillMaxSize()
            .background(

                Brush.verticalGradient(

                    listOf(

                        MaterialTheme.colorScheme.background,

                        MaterialTheme.colorScheme.surface
                    )
                )
            )
            .padding(16.dp)
    ) {

        // header
        Row(

            modifier = Modifier.fillMaxWidth(),

            horizontalArrangement =
                Arrangement.SpaceBetween

        ) {

            Column {

                Text(

                    text = "SISTEMA INVENTARIO",

                    fontSize = 22.sp,

                    fontWeight = FontWeight.Bold,

                    color =
                        MaterialTheme.colorScheme.onBackground
                )

                Text(

                    text = "Seleccione bodega",

                    color =
                        MaterialTheme.colorScheme.onBackground
                )
            }

            Row {

                // config
                IconButton(

                    onClick = {

                        navController.navigate(
                            "configuracion"
                        )
                    }

                ) {

                    Icon(

                        imageVector = Icons.Default.Settings,

                        contentDescription = null,

                        tint =
                            MaterialTheme.colorScheme.primary
                    )
                }

                // salir
                IconButton(

                    onClick = {

                        SessionManager.cerrarSesion()

                        navController.navigate("login") {

                            popUpTo(0)
                        }
                    }

                ) {

                    Icon(

                        imageVector = Icons.Default.ExitToApp,

                        contentDescription = null,

                        tint =
                            MaterialTheme.colorScheme.error
                    )
                }
            }
        }

        Spacer(
            modifier = Modifier.height(20.dp)
        )

        // grid
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(bodegas) { bodega ->
                // card
                CardBodega(
                    bodega = bodega,
                    esAdmin = esAdmin,
                    onClick = {
                        navController.navigate("bodegaMenu/${bodega.id}")
                    },
                    onEditar = {
                        viewModel.editarBodega(it)
                    },
                    onEliminar = {
                        viewModel.eliminarBodega(it)
                    }
                )
            }

            // crear
            if (esAdmin) {
                item {
                    CardCrearBodega {
                        mostrarCrear = true
                    }
                }
            }
        }
    }

    // dialog crear
    if (mostrarCrear) {

        AlertDialog(

            onDismissRequest = {

                mostrarCrear = false
            },

            title = {

                Text("Crear")
            },

            text = {

                OutlinedTextField(

                    value = nuevaBodega,

                    onValueChange = {

                        nuevaBodega = it
                    },

                    label = {

                        Text("Nombre")
                    }
                )
            },

            confirmButton = {

                Button(

                    onClick = {

                        if (
                            nuevaBodega.isNotEmpty()
                        ) {

                            viewModel.crearBodega(
                                nuevaBodega
                            )

                            nuevaBodega = ""

                            mostrarCrear = false
                        }
                    }

                ) {

                    Text("Guardar")
                }
            },

            dismissButton = {

                Button(

                    onClick = {

                        mostrarCrear = false
                    }

                ) {

                    Text("Cancelar")
                }
            }
        )
    }

    // dialog editar
    if (

        mostrarEditar &&
        bodegaSeleccionada != null

    ) {

        AlertDialog(

            onDismissRequest = {

                mostrarEditar = false
            },

            title = {

                Text("Editar")
            },

            text = {

                OutlinedTextField(

                    value = nombreEditar,

                    onValueChange = {

                        nombreEditar = it
                    },

                    label = {

                        Text("Nombre")
                    }
                )
            },

            confirmButton = {

                Row(

                    horizontalArrangement =
                        Arrangement.SpaceBetween
                ) {

                    // eliminar
                    TextButton(

                        onClick = {

                            bodegaSeleccionada?.let {

                                viewModel.eliminarBodega(it)
                            }

                            mostrarEditar = false
                        },

                        colors =
                            ButtonDefaults.textButtonColors(

                                contentColor =
                                    MaterialTheme.colorScheme.error
                            )

                    ) {

                        Text("Eliminar")
                    }

                    // guardar
                    Button(

                        onClick = {

                            bodegaSeleccionada?.let {

                                viewModel.editarBodega(

                                    it.copy(
                                        nombre = nombreEditar
                                    )
                                )
                            }

                            mostrarEditar = false
                        }

                    ) {

                        Text("Guardar")
                    }
                }
            },

            dismissButton = {

                Button(

                    onClick = {

                        mostrarEditar = false
                    }

                ) {

                    Text("Cancelar")
                }
            }
        )
    }
}