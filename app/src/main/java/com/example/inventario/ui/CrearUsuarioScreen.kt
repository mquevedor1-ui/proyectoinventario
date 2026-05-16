package com.example.inventario.ui

import androidx.compose.foundation.background

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding

import androidx.compose.foundation.shape.RoundedCornerShape

import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

import androidx.compose.ui.Modifier

import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import androidx.lifecycle.viewmodel.compose.viewModel

import androidx.navigation.NavController

import com.example.inventario.viewModel.UsuarioViewModel

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

    var esAdmin by remember {

        mutableStateOf(false)
    }

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

        // =========================
        // TITULO
        // =========================

        Text(

            text = "Crear Usuario",

            fontSize = 24.sp,

            fontWeight = FontWeight.Bold,

            color =
                MaterialTheme.colorScheme.onBackground
        )

        Spacer(
            modifier = Modifier.height(10.dp)
        )

        // =========================
        // USUARIO
        // =========================

        OutlinedTextField(

            value = user,

            onValueChange = {

                user = it
            },

            label = {

                Text("Usuario")
            },

            modifier = Modifier,

            colors = OutlinedTextFieldDefaults.colors(

                focusedBorderColor =
                    MaterialTheme.colorScheme.primary,

                focusedLabelColor =
                    MaterialTheme.colorScheme.primary,

                cursorColor =
                    MaterialTheme.colorScheme.primary
            ),

            shape = RoundedCornerShape(12.dp)
        )

        // =========================
        // PASSWORD
        // =========================

        OutlinedTextField(

            value = pass,

            onValueChange = {

                pass = it
            },

            label = {

                Text("Contraseña")
            },

            modifier = Modifier,

            colors = OutlinedTextFieldDefaults.colors(

                focusedBorderColor =
                    MaterialTheme.colorScheme.primary,

                focusedLabelColor =
                    MaterialTheme.colorScheme.primary,

                cursorColor =
                    MaterialTheme.colorScheme.primary
            ),

            shape = RoundedCornerShape(12.dp)
        )

        Spacer(
            modifier = Modifier.height(10.dp)
        )

        // =========================
        // ROLES
        // =========================

        Row {

            RadioButton(

                selected = !esAdmin,

                onClick = {

                    esAdmin = false
                },

                colors = RadioButtonDefaults.colors(

                    selectedColor =
                        MaterialTheme.colorScheme.primary
                )
            )

            Text(

                text = "Usuario",

                color =
                    MaterialTheme.colorScheme.onBackground
            )

            Spacer(
                modifier = Modifier.height(10.dp)
            )

            RadioButton(

                selected = esAdmin,

                onClick = {

                    esAdmin = true
                },

                colors = RadioButtonDefaults.colors(

                    selectedColor =
                        MaterialTheme.colorScheme.primary
                )
            )

            Text(

                text = "Administrador",

                color =
                    MaterialTheme.colorScheme.onBackground
            )
        }

        Spacer(
            modifier = Modifier.height(20.dp)
        )

        // =========================
        // BOTON
        // =========================

        Button(

            onClick = {

                val rol = if (esAdmin)
                    "admin"
                else
                    "usuario"

                viewModel.crearUsuario(

                    user,
                    pass,
                    rol
                )

                navController.popBackStack()
            },

            colors = ButtonDefaults.buttonColors(

                containerColor =
                    MaterialTheme.colorScheme.primary
            ),

            shape = RoundedCornerShape(12.dp)

        ) {

            Text(

                text = "Guardar",

                color =
                    MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}