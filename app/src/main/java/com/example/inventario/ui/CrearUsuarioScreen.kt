package com.example.inventario.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape

import androidx.compose.material3.*

import androidx.compose.runtime.*

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import androidx.lifecycle.viewmodel.compose.viewModel

import androidx.navigation.NavController

import com.example.inventario.viewModel.UsuarioViewModel

import com.example.inventario.ui.AppTopBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CrearUsuarioScreen(

    navController: NavController,

    viewModel: UsuarioViewModel = viewModel()

) {

    var user by remember {

        mutableStateOf("")
    }

    var pass by remember {

        mutableStateOf("")
    }

    var confirmar by remember {

        mutableStateOf("")
    }

    var rol by remember {

        mutableStateOf("usuario")
    }

    Scaffold(

        topBar = {

            AppTopBar(

                titulo = "Crear Nuevo Usuario",

                navController = navController
            )
        },

        containerColor =
            MaterialTheme.colorScheme.background

    ) { padding ->

        Column(

            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(20.dp)

        ) {

            Card(

                shape = RoundedCornerShape(24.dp),

                colors = CardDefaults.cardColors(

                    containerColor =
                        MaterialTheme.colorScheme.surface
                ),

                modifier = Modifier.fillMaxWidth()

            ) {

                Column(

                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),

                    verticalArrangement =
                        Arrangement.spacedBy(16.dp)

                ) {

                    Text(

                        text = "Información del Usuario",

                        fontSize = 20.sp,

                        fontWeight = FontWeight.Bold,

                        color =
                            MaterialTheme.colorScheme.onSurface
                    )

                    // usuario

                    OutlinedTextField(

                        value = user,

                        onValueChange = {

                            user = it
                        },

                        label = {

                            Text("Nombre de Usuario")
                        },

                        modifier =
                            Modifier.fillMaxWidth(),

                        shape =
                            RoundedCornerShape(12.dp)
                    )

                    // contraseña

                    OutlinedTextField(

                        value = pass,

                        onValueChange = {

                            pass = it
                        },

                        label = {

                            Text("Contraseña")
                        },

                        visualTransformation =
                            PasswordVisualTransformation(),

                        modifier =
                            Modifier.fillMaxWidth(),

                        shape =
                            RoundedCornerShape(12.dp)
                    )

                    // confirmar contraseña

                    OutlinedTextField(

                        value = confirmar,

                        onValueChange = {

                            confirmar = it
                        },

                        label = {

                            Text("Confirmar Contraseña")
                        },

                        visualTransformation =
                            PasswordVisualTransformation(),

                        modifier =
                            Modifier.fillMaxWidth(),

                        shape =
                            RoundedCornerShape(12.dp)
                    )

                    HorizontalDivider(

                        modifier =
                            Modifier.padding(vertical = 8.dp)
                    )

                    Text(

                        text = "Seleccionar Rol",

                        fontSize = 16.sp,

                        fontWeight =
                            FontWeight.Medium
                    )

                    // roles

                    Row(

                        modifier =
                            Modifier.fillMaxWidth(),

                        horizontalArrangement =
                            Arrangement.SpaceEvenly
                    ) {

                        Row(

                            verticalAlignment =
                                Alignment.CenterVertically

                        ) {

                            RadioButton(

                                selected =
                                    rol == "usuario",

                                onClick = {

                                    rol = "usuario"
                                }
                            )

                            Text(
                                text = "Usuario"
                            )
                        }

                        Row(

                            verticalAlignment =
                                Alignment.CenterVertically

                        ) {

                            RadioButton(

                                selected =
                                    rol == "admin",

                                onClick = {

                                    rol = "admin"
                                }
                            )

                            Text(
                                text = "Administrador"
                            )
                        }
                    }

                    Spacer(
                        modifier = Modifier.height(16.dp)
                    )

                    Button(

                        onClick = {

                            if (
                                user.isNotEmpty()
                                &&
                                pass.isNotEmpty()
                                &&
                                confirmar.isNotEmpty()
                            ) {

                                if (
                                    pass != confirmar
                                ) {

                                    return@Button
                                }

                                viewModel.crearUsuario(

                                    user,

                                    pass,

                                    rol
                                )

                                navController.popBackStack()
                            }
                        },

                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),

                        shape =
                            RoundedCornerShape(12.dp)

                    ) {

                        Text(

                            text = "Guardar Usuario",

                            fontSize = 16.sp
                        )
                    }
                }
            }
        }
    }
}