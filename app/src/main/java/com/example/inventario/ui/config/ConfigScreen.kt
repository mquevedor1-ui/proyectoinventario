package com.example.inventario.ui.config

import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ColorLens
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.inventario.data.usuario
import com.example.inventario.viewModel.AppThemeState
import com.example.inventario.viewModel.UsuarioViewModel

@Composable
fun ConfigScreen(

    navController: NavController,

    usuarioViewModel: UsuarioViewModel = viewModel()

) {

    var seccion by remember {

        mutableStateOf("temas")
    }

    Row(

        modifier = Modifier.fillMaxSize()

    ) {

        // =========================
        // MENU IZQUIERDO
        // =========================

        Column(

            modifier = Modifier
                .widthIn(min = 110.dp, max = 140.dp)
                .fillMaxHeight()
                .background(MaterialTheme.colorScheme.primary)
                .padding(8.dp),

            verticalArrangement = Arrangement.spacedBy(10.dp)

        ) {

            Text(

                text = "Config",

                fontSize = 18.sp,

                modifier = Modifier.padding(8.dp)
            )

            MenuButton(
                "Crear",
                Icons.Default.Person
            ) {

                seccion = "crear"
            }

            MenuButton(
                "Editar",
                Icons.Default.Edit
            ) {

                seccion = "editar"
            }

            MenuButton(
                "Usuarios",
                Icons.Default.List
            ) {

                seccion = "usuarios"
            }

            MenuButton(
                "Temas",
                Icons.Default.ColorLens
            ) {

                seccion = "temas"
            }

            MenuButton(
                "Notificaciones",
                Icons.Default.Notifications
            ) {

                seccion = "notificaciones"
            }
        }

        // =========================
        // CONTENIDO
        // =========================

        Column(

            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)

        ) {

            when (seccion) {

                // =========================
                // TEMAS
                // =========================

                "temas" -> {

                    Column(
                        modifier = Modifier.verticalScroll(
                            rememberScrollState()
                        )
                    ) {

                        Text(

                            text = "Cambiar Tema",

                            fontSize = 24.sp
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        FlowRow(

                            horizontalArrangement = Arrangement.spacedBy(8.dp),

                            verticalArrangement = Arrangement.spacedBy(8.dp)

                        ) {

                            listOf(
                                "verde",
                                "azul",
                                "oscuro",
                                "naranja",
                                "morado"
                            ).forEach { tema ->

                                Button(

                                    onClick = {

                                        AppThemeState.cambiarTema(tema)
                                    }

                                ) {

                                    Text(tema)
                                }
                            }
                        }
                    }
                }

                // =========================
                // CREAR USUARIO
                // =========================

                "crear" -> {

                    Column {

                        Text(
                            text = "Crear Usuario",
                            fontSize = 24.sp
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(

                            onClick = {

                                navController.navigate("crearUsuario")
                            }

                        ) {

                            Text("Ir")
                        }
                    }
                }

                // =========================
                // EDITAR USUARIOS
                // =========================

                "editar" -> {

                    var buscar by remember {

                        mutableStateOf("")
                    }

                    var usuarioSeleccionado by remember {

                        mutableStateOf<usuario?>(null)
                    }

                    var mostrarDialogo by remember {

                        mutableStateOf(false)
                    }

                    val listaUsuarios by usuarioViewModel
                        .usuarios
                        .collectAsState()

                    val usuariosFiltrados = listaUsuarios.filter {

                        it.user.contains(
                            buscar,
                            ignoreCase = true
                        ) ||

                                it.rol.contains(
                                    buscar,
                                    ignoreCase = true
                                )
                    }

                    Column {

                        Text(
                            text = "Editar Usuarios",
                            fontSize = 24.sp
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        OutlinedTextField(

                            value = buscar,

                            onValueChange = {

                                buscar = it
                            },

                            label = {

                                Text("Buscar usuario")
                            },

                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        LazyColumn(

                            verticalArrangement = Arrangement.spacedBy(8.dp)

                        ) {

                            items(usuariosFiltrados) { usuario ->

                                Row(

                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(
                                            MaterialTheme.colorScheme.surface
                                        )
                                        .combinedClickable(

                                            onClick = {
                                            },

                                            onDoubleClick = {

                                                usuarioSeleccionado = usuario

                                                mostrarDialogo = true
                                            }
                                        )
                                        .padding(16.dp),

                                    horizontalArrangement = Arrangement.SpaceBetween

                                ) {

                                    Column {

                                        Text(

                                            text = usuario.user,

                                            fontWeight = FontWeight.Bold,

                                            fontSize = 18.sp
                                        )

                                        Spacer(
                                            modifier = Modifier.height(4.dp)
                                        )

                                        Text(
                                            text = "Rol: ${usuario.rol}"
                                        )
                                    }
                                }
                            }
                        }

                        // =========================
                        // DIALOGO OPCIONES
                        // =========================

                        if (
                            mostrarDialogo &&
                            usuarioSeleccionado != null
                        ) {

                            AlertDialog(

                                onDismissRequest = {

                                    mostrarDialogo = false
                                },

                                title = {

                                    Text("Opciones Usuario")
                                },

                                text = {

                                    Text(
                                        "¿Qué deseas hacer con ${usuarioSeleccionado!!.user}?"
                                    )
                                },

                                confirmButton = {

                                    TextButton(

                                        onClick = {

                                            navController.navigate(
                                                "editarUsuario/${usuarioSeleccionado!!.id}"
                                            )

                                            mostrarDialogo = false
                                        }

                                    ) {

                                        Text("Editar")
                                    }
                                },

                                dismissButton = {

                                    Row {

                                        TextButton(

                                            onClick = {

                                                usuarioViewModel
                                                    .eliminarUsuario(
                                                        usuarioSeleccionado!!
                                                    )

                                                mostrarDialogo = false
                                            }

                                        ) {

                                            Text("Eliminar")
                                        }

                                        TextButton(

                                            onClick = {

                                                mostrarDialogo = false
                                            }

                                        ) {

                                            Text("Cancelar")
                                        }
                                    }
                                }
                            )
                        }
                    }
                }

                // =========================
                // LISTA USUARIOS
                // =========================

                "usuarios" -> {

                    val listaUsuarios by usuarioViewModel
                        .usuarios
                        .collectAsState()

                    Column {

                        Text(
                            text = "Lista Usuarios",
                            fontSize = 24.sp
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        LazyColumn(

                            verticalArrangement = Arrangement.spacedBy(8.dp)

                        ) {

                            items(listaUsuarios) { usuario ->

                                Column(

                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(
                                            MaterialTheme.colorScheme.surface
                                        )
                                        .padding(16.dp)

                                ) {

                                    Text(

                                        text = usuario.user,

                                        fontWeight = FontWeight.Bold,

                                        fontSize = 18.sp
                                    )

                                    Spacer(
                                        modifier = Modifier.height(4.dp)
                                    )

                                    Text(
                                        text = "Rol: ${usuario.rol}"
                                    )
                                }
                            }
                        }
                    }
                }

                // =========================
                // NOTIFICACIONES
                // =========================

                "notificaciones" -> {

                    Row(

                        verticalAlignment = Alignment.CenterVertically

                    ) {

                        Switch(

                            checked = true,

                            onCheckedChange = {}

                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Text("Activar notificaciones")
                    }
                }
            }
        }
    }
}

@Composable
fun MenuButton(

    texto: String,

    icono: androidx.compose.ui.graphics.vector.ImageVector,

    onClick: () -> Unit

) {

    Button(

        onClick = onClick,

        modifier = Modifier.fillMaxWidth()

    ) {

        Row(

            verticalAlignment = Alignment.CenterVertically

        ) {

            Icon(

                imageVector = icono,

                contentDescription = null
            )

            Spacer(modifier = Modifier.width(4.dp))

            Text(

                text = texto,

                fontSize = 12.sp
            )
        }
    }
}