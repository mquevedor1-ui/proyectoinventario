package com.example.inventario.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding

import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember

import androidx.compose.runtime.setValue

import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import androidx.lifecycle.viewmodel.compose.viewModel

import androidx.navigation.NavController

import com.example.inventario.data.usuario
import com.example.inventario.viewModel.UsuarioViewModel

@Composable
fun EditarUsuarioScreen(

    navController: NavController,

    id: Int,

    viewModel: UsuarioViewModel = viewModel()

) {

    // usuarios
    val usuarios by viewModel
        .usuarios
        .collectAsState()

    // usuario
    var usuarioActual by remember {

        mutableStateOf<usuario?>(null)
    }

    // datos
    var user by remember {

        mutableStateOf("")
    }

    var pass by remember {

        mutableStateOf("")
    }

    var rol by remember {

        mutableStateOf("usuario")
    }

    // cargar
    LaunchedEffect(Unit) {

        val encontrado =

            usuarios.find {

                it.id == id
            }

        usuarioActual = encontrado

        encontrado?.let {

            user = it.user

            pass = it.pass

            rol = it.rol
        }
    }

    // ui
    Column(

        modifier = Modifier
            .fillMaxSize()
            .background(
                MaterialTheme.colorScheme.background
            )
            .padding(20.dp),

        verticalArrangement =
            Arrangement.spacedBy(12.dp)

    ) {

        Text(

            text = "Editar Usuario",

            fontSize = 24.sp
        )

        Spacer(
            modifier = Modifier.height(10.dp)
        )

        // user
        OutlinedTextField(

            value = user,

            onValueChange = {

                user = it
            },

            label = {

                Text("Usuario")
            },

            modifier =
                Modifier.fillMaxWidth()
        )

        // pass
        OutlinedTextField(

            value = pass,

            onValueChange = {

                pass = it
            },

            label = {

                Text("Contraseña")
            },

            modifier =
                Modifier.fillMaxWidth()
        )

        Spacer(
            modifier = Modifier.height(10.dp)
        )

        Text("Rol")

        // admin
        Row {

            RadioButton(

                selected = rol == "admin",

                onClick = {

                    rol = "admin"
                }
            )

            Text("Administrador")
        }

        // usuario
        Row {

            RadioButton(

                selected = rol == "usuario",

                onClick = {

                    rol = "usuario"
                }
            )

            Text("Usuario")
        }

        Spacer(
            modifier = Modifier.height(20.dp)
        )

        // guardar
        Button(

            onClick = {

                usuarioActual?.let {

                    viewModel.editarUsuario(

                        usuarioViejo = it,

                        nuevoUser = user,

                        nuevaPass = pass,

                        nuevoRol = rol
                    )

                    navController.popBackStack()
                }
            },

            modifier =
                Modifier.fillMaxWidth()

        ) {

            Text("Guardar")
        }
    }
}