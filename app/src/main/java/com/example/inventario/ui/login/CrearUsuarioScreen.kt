package com.example.inventario.ui.login

import android.app.Application
import android.widget.Toast

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding

import androidx.compose.foundation.shape.RoundedCornerShape

import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue

import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel

import androidx.navigation.NavController

import com.example.inventario.viewModel.UsuarioViewModel

import kotlinx.coroutines.launch

@Composable
fun CrearUsuarioScreen(

    navController: NavController

) {

    val context =
        androidx.compose.ui.platform.LocalContext.current

    val scope =
        rememberCoroutineScope()

    val viewModel: UsuarioViewModel = viewModel(

        factory = ViewModelProvider.AndroidViewModelFactory
            .getInstance(
                context.applicationContext as Application
            )
    )

    var usuario by remember {

        mutableStateOf("")
    }

    var password by remember {

        mutableStateOf("")
    }

    var confirmar by remember {

        mutableStateOf("")
    }

    Scaffold {

        Column(

            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),

            verticalArrangement =
                Arrangement.Center

        ) {

            Text(

                text = "Crear Usuario",

                style =
                    MaterialTheme.typography.headlineMedium
            )

            Spacer(
                modifier = Modifier.height(20.dp)
            )

            OutlinedTextField(

                value = usuario,

                onValueChange = {

                    usuario = it
                },

                label = {

                    Text("Usuario")
                },

                modifier =
                    Modifier.fillMaxWidth()
            )

            Spacer(
                modifier = Modifier.height(10.dp)
            )

            OutlinedTextField(

                value = password,

                onValueChange = {

                    password = it
                },

                label = {

                    Text("Contraseña")
                },

                visualTransformation =
                    PasswordVisualTransformation(),

                modifier =
                    Modifier.fillMaxWidth()
            )

            Spacer(
                modifier = Modifier.height(10.dp)
            )

            OutlinedTextField(

                value = confirmar,

                onValueChange = {

                    confirmar = it
                },

                label = {

                    Text("Confirmar contraseña")
                },

                visualTransformation =
                    PasswordVisualTransformation(),

                modifier =
                    Modifier.fillMaxWidth()
            )

            Spacer(
                modifier = Modifier.height(20.dp)
            )

            Button(

                onClick = {

                    scope.launch {

                        if (
                            usuario.isEmpty() ||
                            password.isEmpty() ||
                            confirmar.isEmpty()
                        ) {

                            Toast.makeText(

                                context,

                                "Complete todos los campos",

                                Toast.LENGTH_SHORT

                            ).show()

                            return@launch
                        }

                        if (
                            password != confirmar
                        ) {

                            Toast.makeText(

                                context,

                                "Las contraseñas no coinciden",

                                Toast.LENGTH_SHORT

                            ).show()

                            return@launch
                        }

                        val ok =
                            viewModel.registrar(
                                usuario,
                                password
                            )

                        if (ok) {

                            Toast.makeText(

                                context,

                                "Usuario creado",

                                Toast.LENGTH_SHORT

                            ).show()

                            navController.popBackStack()

                        } else {

                            Toast.makeText(

                                context,

                                "El usuario ya existe",

                                Toast.LENGTH_SHORT

                            ).show()
                        }
                    }
                },

                modifier =
                    Modifier.fillMaxWidth(),

                shape =
                    RoundedCornerShape(12.dp),

                colors =
                    ButtonDefaults.buttonColors(

                        containerColor =
                            MaterialTheme.colorScheme.primary
                    )

            ) {

                Text("Crear Usuario")
            }
        }
    }
}