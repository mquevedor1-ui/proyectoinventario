package com.example.inventario.ui.config

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.inventario.viewModel.UsuarioViewModel

@Composable
fun UsuariosScreen(

    viewModel: UsuarioViewModel

) {

    var user by remember {

        mutableStateOf("")
    }

    var pass by remember {

        mutableStateOf("")
    }

    var rol by remember {

        mutableStateOf("usuario")
    }

    Column(

        modifier = Modifier
            .fillMaxSize()
            .background(
                MaterialTheme.colorScheme.background
            )
            .padding(20.dp),

        verticalArrangement = Arrangement.spacedBy(12.dp)

    ) {

        // =========================
        // TITULO
        // =========================

        Text(

            text = "Crear Usuario",

            fontSize = 22.sp,

            color = MaterialTheme.colorScheme.onBackground
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

            colors = OutlinedTextFieldDefaults.colors(

                focusedBorderColor =
                    MaterialTheme.colorScheme.primary,

                unfocusedBorderColor =
                    MaterialTheme.colorScheme.onBackground,

                focusedLabelColor =
                    MaterialTheme.colorScheme.primary
            )
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

            colors = OutlinedTextFieldDefaults.colors(

                focusedBorderColor =
                    MaterialTheme.colorScheme.primary,

                unfocusedBorderColor =
                    MaterialTheme.colorScheme.onBackground,

                focusedLabelColor =
                    MaterialTheme.colorScheme.primary
            )
        )

        Spacer(modifier = Modifier.height(10.dp))

        // =========================
        // ROL
        // =========================

        Text(

            text = "Rol",

            color = MaterialTheme.colorScheme.onBackground
        )

        Row {

            RadioButton(

                selected = rol == "admin",

                onClick = {

                    rol = "admin"
                },

                colors = RadioButtonDefaults.colors(

                    selectedColor =
                        MaterialTheme.colorScheme.primary
                )
            )

            Text(

                text = "Administrador",

                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.width(10.dp))

            RadioButton(

                selected = rol == "usuario",

                onClick = {

                    rol = "usuario"
                },

                colors = RadioButtonDefaults.colors(

                    selectedColor =
                        MaterialTheme.colorScheme.primary
                )
            )

            Text(

                text = "Usuario",

                color = MaterialTheme.colorScheme.onBackground
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        // =========================
        // BOTON
        // =========================

        Button(

            onClick = {

                viewModel.crearUsuario(
                    user,
                    pass,
                    rol
                )
            },

            colors = ButtonDefaults.buttonColors(

                containerColor =
                    MaterialTheme.colorScheme.primary
            )

        ) {

            Text(

                text = "Guardar",

                color = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}