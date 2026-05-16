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

@Composable
fun MainMenuScreen(

    navController: NavController,

    viewModel: BodegaViewModel,

    esAdmin: Boolean

) {

    // =========================
    // LISTA BODEGAS
    // =========================

    val bodegas by viewModel.bodegas.collectAsState(

        initial = emptyList<Bodega>()
    )

    // =========================
    // DIALOG CREAR
    // =========================

    var mostrarDialogoCrear by remember {

        mutableStateOf(false)
    }

    var nombreNuevaBodega by remember {

        mutableStateOf("")
    }

    // =========================
    // DIALOG EDITAR
    // =========================

    var mostrarDialogoEditar by remember {

        mutableStateOf(false)
    }

    var nombreEditar by remember {

        mutableStateOf("")
    }

    var bodegaSeleccionada by remember {

        mutableStateOf<Bodega?>(null)
    }

    // =========================
    // PANTALLA
    // =========================

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

        // =========================
        // HEADER
        // =========================

        Row(

            modifier = Modifier.fillMaxWidth(),

            horizontalArrangement =
                Arrangement.SpaceBetween

        ) {

            Column {

                Text(

                    text = "SISTEMA DE INVENTARIO",

                    fontSize = 22.sp,

                    fontWeight = FontWeight.Bold,

                    color =
                        MaterialTheme.colorScheme.onBackground
                )

                Text(

                    text = "Seleccione una bodega",

                    color =
                        MaterialTheme.colorScheme.onBackground
                )
            }

            // =========================
            // CONFIG
            // =========================

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
        }

        Spacer(
            modifier = Modifier.height(20.dp)
        )

        // =========================
        // GRID BODEGAS
        // =========================

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(bodegas) { bodega ->
                Column {
                    CardBodega(
                        bodega = bodega,
                        onClick = {
                            navController.navigate(
                                "bodegaMenu/${bodega.id}"
                            )
                        }
                    )

                    if (esAdmin) {
                        TextButton(
                            onClick = {
                                bodegaSeleccionada = bodega
                                nombreEditar = bodega.nombre
                                mostrarDialogoEditar = true
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Editar", fontSize = 12.sp)
                        }
                    }
                }
            }

            // =========================
            // CREAR BODEGA
            // =========================

            if (esAdmin) {

                item {

                    CardCrearBodega {

                        mostrarDialogoCrear = true
                    }
                }
            }
        }
    }

    // =========================
    // DIALOG CREAR
    // =========================

    if (mostrarDialogoCrear) {

        AlertDialog(

            onDismissRequest = {

                mostrarDialogoCrear = false
            },

            title = {

                Text("Crear Bodega")
            },

            text = {

                OutlinedTextField(

                    value = nombreNuevaBodega,

                    onValueChange = {

                        nombreNuevaBodega = it
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
                            nombreNuevaBodega.isNotEmpty()
                        ) {

                            viewModel.crearBodega(

                                nombreNuevaBodega
                            )

                            nombreNuevaBodega = ""

                            mostrarDialogoCrear = false
                        }
                    }

                ) {

                    Text("Guardar")
                }
            },

            dismissButton = {

                Button(

                    onClick = {

                        mostrarDialogoCrear = false
                    }

                ) {

                    Text("Cancelar")
                }
            }
        )
    }

    // =========================
    // DIALOG EDITAR
    // =========================

    if (mostrarDialogoEditar && bodegaSeleccionada != null) {

        AlertDialog(

            onDismissRequest = {

                mostrarDialogoEditar = false
            },

            title = {

                Text("Editar Bodega")
            },

            text = {

                OutlinedTextField(

                    value = nombreEditar,

                    onValueChange = {

                        nombreEditar = it
                    },

                    label = {

                        Text("Nuevo Nombre")
                    }
                )
            },

            confirmButton = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    TextButton(
                        onClick = {
                            bodegaSeleccionada?.let { bodega ->
                                viewModel.eliminarBodega(bodega)
                            }
                            mostrarDialogoEditar = false
                        },
                        colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error)
                    ) {
                        Text("Eliminar")
                    }

                    Button(
                        onClick = {
                            bodegaSeleccionada?.let { bodega ->
                                viewModel.editarBodega(
                                    bodega.copy(nombre = nombreEditar)
                                )
                            }
                            mostrarDialogoEditar = false
                        }
                    ) {
                        Text("Guardar")
                    }
                }
            },

            dismissButton = {

                Button(

                    onClick = {

                        mostrarDialogoEditar = false
                    }

                ) {

                    Text("Cancelar")
                }
            }
        )
    }
}