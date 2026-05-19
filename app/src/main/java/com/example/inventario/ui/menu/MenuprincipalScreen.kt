package com.example.inventario.ui.menu

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size

import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items

import androidx.compose.foundation.shape.CircleShape

import androidx.compose.material.icons.Icons

import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Settings

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import androidx.lifecycle.viewmodel.compose.viewModel

import androidx.navigation.NavController

import com.example.inventario.data.Bodega
import com.example.inventario.viewModel.BodegaViewModel

@Composable
fun MenuPScreen(

    navController: NavController

) {

    val bodegaViewModel:
            BodegaViewModel =
        viewModel()

    val bodegas by
    bodegaViewModel
        .bodegas
        .collectAsState(
            initial = emptyList()
        )

    var bodegaParaEditar by remember {

        mutableStateOf<Bodega?>(null)
    }

    var bodegaParaEliminar by remember {

        mutableStateOf<Bodega?>(null)
    }

    var nuevoNombre by remember {

        mutableStateOf("")
    }

    LaunchedEffect(bodegaParaEditar) {

        nuevoNombre =
            bodegaParaEditar?.nombre ?: ""
    }

    Scaffold(

        containerColor =
            MaterialTheme
                .colorScheme
                .background,

        topBar = {

            Row(

                modifier = Modifier
                    .fillMaxWidth()
                    .padding(

                        start = 16.dp,
                        end = 16.dp,
                        top = 48.dp,
                        bottom = 16.dp
                    ),

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
                            "SISTEMA INVENTARIO",

                        fontSize = 18.sp,

                        fontWeight =
                            FontWeight.Bold
                    )

                    Text(

                        text =
                            "Seleccione bodega",

                        fontSize = 14.sp,

                        color =
                            MaterialTheme
                                .colorScheme
                                .onBackground
                    )
                }

                Row {

                    // sincronizar

                    IconButton(

                        onClick = {

                            bodegaViewModel
                                .actualizarTodoDesdeNube()
                        }

                    ) {

                        Icon(

                            imageVector =
                                Icons.Default.Refresh,

                            contentDescription =
                                "Sincronizar",

                            tint =
                                MaterialTheme
                                    .colorScheme
                                    .primary
                        )
                    }

                    // configuracion

                    IconButton(

                        onClick = {

                            navController.navigate(
                                "configuracion"
                            )
                        }

                    ) {

                        Icon(

                            imageVector =
                                Icons.Default.Settings,

                            contentDescription =
                                "Configuracion",

                            tint =
                                MaterialTheme
                                    .colorScheme
                                    .primary
                        )
                    }

                    // cerrar sesion

                    IconButton(

                        onClick = {

                            navController.navigate(
                                "login"
                            ) {

                                popUpTo(0)
                            }
                        }

                    ) {

                        Icon(

                            imageVector =
                                Icons.Default.ExitToApp,

                            contentDescription =
                                "Cerrar Sesion",

                            tint =
                                MaterialTheme
                                    .colorScheme
                                    .error
                        )
                    }
                }
            }
        }

    ) { padding ->

        LazyVerticalGrid(

            columns =
                GridCells.Fixed(2),

            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),

            contentPadding =
                PaddingValues(12.dp),

            horizontalArrangement =
                Arrangement.spacedBy(12.dp),

            verticalArrangement =
                Arrangement.spacedBy(12.dp)

        ) {

            // bodegas

            items(bodegas) { bodega ->

                Card(

                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp)
                        .clickable {

                            navController.navigate(

                                "menuBodega/${bodega.id}"
                            )
                        },

                    colors =
                        CardDefaults.cardColors(

                            containerColor =
                                MaterialTheme
                                    .colorScheme
                                    .surface
                        ),

                    elevation =
                        CardDefaults.cardElevation(

                            defaultElevation = 4.dp
                        )

                ) {

                    Box(

                        modifier =
                            Modifier.fillMaxSize()

                    ) {

                        // editar

                        IconButton(

                            onClick = {

                                bodegaParaEditar =
                                    bodega
                            },

                            modifier =
                                Modifier.align(
                                    Alignment.TopStart
                                )

                        ) {

                            Icon(

                                imageVector =
                                    Icons.Default.Edit,

                                contentDescription =
                                    "Editar",

                                tint =
                                    MaterialTheme
                                        .colorScheme
                                        .primary
                            )
                        }

                        // eliminar

                        IconButton(

                            onClick = {

                                bodegaParaEliminar =
                                    bodega
                            },

                            modifier =
                                Modifier.align(
                                    Alignment.TopEnd
                                )

                        ) {

                            Icon(

                                imageVector =
                                    Icons.Default.Delete,

                                contentDescription =
                                    "Eliminar",

                                tint =
                                    MaterialTheme
                                        .colorScheme
                                        .error
                            )
                        }

                        Column(

                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp),

                            horizontalAlignment =
                                Alignment.CenterHorizontally,

                            verticalArrangement =
                                Arrangement.Center

                        ) {

                            // icono

                            Box(

                                modifier = Modifier
                                    .size(80.dp)
                                    .background(

                                        MaterialTheme
                                            .colorScheme
                                            .primary,

                                        CircleShape
                                    ),

                                contentAlignment =
                                    Alignment.Center

                            ) {

                                Icon(

                                    imageVector =
                                        Icons.Default.Home,

                                    contentDescription =
                                        null,

                                    tint =
                                        MaterialTheme
                                            .colorScheme
                                            .onPrimary,

                                    modifier =
                                        Modifier.size(
                                            40.dp
                                        )
                                )
                            }

                            Spacer(

                                modifier =
                                    Modifier.height(16.dp)
                            )

                            // nombre

                            Text(

                                text =
                                    bodega.nombre,

                                fontSize = 20.sp,

                                fontWeight =
                                    FontWeight.Bold
                            )

                            Spacer(

                                modifier =
                                    Modifier.height(8.dp)
                            )

                            // descripcion

                            Text(

                                text =
                                    "Abrir bodega",

                                fontSize = 13.sp,

                                color =
                                    MaterialTheme
                                        .colorScheme
                                        .onSurfaceVariant,

                                textAlign =
                                    TextAlign.Center
                            )
                        }
                    }
                }
            }

            // crear bodega

            item {

                CardCrearBodega(

                    onClick = {

                        navController.navigate(
                            "crearBodega"
                        )
                    }
                )
            }
        }

        // editar

        if (bodegaParaEditar != null) {

            AlertDialog(

                onDismissRequest = {

                    bodegaParaEditar = null
                },

                title = {

                    Text(
                        "Editar Bodega"
                    )
                },

                text = {

                    OutlinedTextField(

                        value =
                            nuevoNombre,

                        onValueChange = {

                            nuevoNombre = it
                        },

                        label = {

                            Text(
                                "Nuevo nombre"
                            )
                        }
                    )
                },

                confirmButton = {

                    Button(

                        onClick = {

                            bodegaParaEditar?.let {

                                bodegaViewModel
                                    .editarBodega(

                                        it.copy(
                                            nombre =
                                                nuevoNombre
                                        )
                                    )
                            }

                            bodegaParaEditar =
                                null

                            nuevoNombre = ""
                        }

                    ) {

                        Text("Guardar")
                    }
                },

                dismissButton = {

                    TextButton(

                        onClick = {

                            bodegaParaEditar =
                                null
                        }

                    ) {

                        Text("Cancelar")
                    }
                }
            )
        }

        // eliminar

        if (bodegaParaEliminar != null) {

            AlertDialog(

                onDismissRequest = {

                    bodegaParaEliminar = null
                },

                title = {

                    Text(
                        "Eliminar Bodega"
                    )
                },

                text = {

                    Text(

                        "¿Seguro que desea eliminar esta bodega?"
                    )
                },

                confirmButton = {

                    Button(

                        onClick = {

                            bodegaParaEliminar?.let {

                                bodegaViewModel
                                    .eliminarBodega(it)
                            }

                            bodegaParaEliminar =
                                null
                        },

                        colors =
                            ButtonDefaults.buttonColors(

                                containerColor =
                                    MaterialTheme
                                        .colorScheme
                                        .error
                            )

                    ) {

                        Text("Eliminar")
                    }
                },

                dismissButton = {

                    TextButton(

                        onClick = {

                            bodegaParaEliminar =
                                null
                        }

                    ) {

                        Text("Cancelar")
                    }
                }
            )
        }
    }
}