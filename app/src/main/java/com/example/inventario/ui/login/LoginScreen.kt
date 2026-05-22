package com.example.inventario.ui.login

import android.app.Application
import android.widget.Toast

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff

import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel

import androidx.navigation.NavController

import com.example.inventario.viewModel.SessionManager
import com.example.inventario.viewModel.UsuarioViewModel

import kotlinx.coroutines.launch

@Composable
fun LoginScreen(

    navController: NavController

) {

    val context = LocalContext.current

    val scope = rememberCoroutineScope()

    val viewModel: UsuarioViewModel = viewModel(

        factory = ViewModelProvider.AndroidViewModelFactory
            .getInstance(
                context.applicationContext as Application
            )
    )

    // admin o usuario

    var loginAdmin by remember {

        mutableStateOf(false)
    }

    var usuario by remember {

        mutableStateOf("")
    }

    var password by remember {

        mutableStateOf("")
    }

    // ojito contraseña

    var passwordVisible by remember {

        mutableStateOf(false)
    }

    Scaffold(

        containerColor =
            MaterialTheme.colorScheme.background

    ) { innerPadding ->

        Surface(

            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),

            color = MaterialTheme.colorScheme.background

        ) {

            Box(

                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        MaterialTheme.colorScheme.background
                    ),

                contentAlignment = Alignment.Center

            ) {

                Card(

                    shape = RoundedCornerShape(20.dp),

                    modifier = Modifier
                        .padding(20.dp)
                        .fillMaxWidth(),

                    colors = CardDefaults.cardColors(

                        containerColor =
                            MaterialTheme.colorScheme.surface
                    )

                ) {

                    Column(

                        modifier = Modifier.padding(20.dp),

                        horizontalAlignment =
                            Alignment.CenterHorizontally,

                        verticalArrangement =
                            Arrangement.Center

                    ) {

                        Box(

                            modifier = Modifier
                                .size(70.dp)
                                .clip(CircleShape)
                                .background(
                                    MaterialTheme.colorScheme.primary
                                ),

                            contentAlignment = Alignment.Center

                        ) {

                            Icon(

                                imageVector = Icons.Default.Home,

                                contentDescription = null,

                                tint =
                                    MaterialTheme.colorScheme.onPrimary
                            )
                        }

                        Spacer(
                            modifier = Modifier.height(10.dp)
                        )

                        Text(

                            text = "Sistema de Inventario",

                            fontWeight = FontWeight.Bold,

                            fontSize = 20.sp,

                            color =
                                MaterialTheme.colorScheme.onBackground
                        )

                        Text(

                            text =
                                if (loginAdmin)
                                    "Modo Administrador"
                                else
                                    "Modo Usuario",

                            fontSize = 12.sp,

                            color =
                                MaterialTheme.colorScheme.primary
                        )

                        Spacer(
                            modifier = Modifier.height(16.dp)
                        )

                        // selector admin usuario

                        Row(

                            modifier = Modifier
                                .fillMaxWidth()
                                .background(

                                    MaterialTheme.colorScheme.background,

                                    RoundedCornerShape(50.dp)
                                )
                                .padding(4.dp)
                        ) {

                            Button(

                                onClick = {

                                    loginAdmin = false
                                },

                                colors = ButtonDefaults.buttonColors(

                                    containerColor = if (!loginAdmin)

                                        MaterialTheme.colorScheme.primary

                                    else

                                        MaterialTheme.colorScheme.surface
                                ),

                                shape = RoundedCornerShape(50.dp),

                                modifier = Modifier.weight(1f)

                            ) {

                                Text(

                                    text = "Usuario",

                                    color = if (!loginAdmin)

                                        MaterialTheme.colorScheme.onPrimary

                                    else

                                        MaterialTheme.colorScheme.onBackground
                                )
                            }

                            Button(

                                onClick = {

                                    loginAdmin = true
                                },

                                colors = ButtonDefaults.buttonColors(

                                    containerColor = if (loginAdmin)

                                        MaterialTheme.colorScheme.primary

                                    else

                                        MaterialTheme.colorScheme.surface
                                ),

                                shape = RoundedCornerShape(50.dp),

                                modifier = Modifier.weight(1f)

                            ) {

                                Text(

                                    text = "Administrador",

                                    color = if (loginAdmin)

                                        MaterialTheme.colorScheme.onPrimary

                                    else

                                        MaterialTheme.colorScheme.onBackground
                                )
                            }
                        }

                        Spacer(
                            modifier = Modifier.height(16.dp)
                        )

                        OutlinedTextField(

                            value = usuario,

                            onValueChange = {

                                usuario = it
                            },

                            label = {

                                Text("Usuario")
                            },

                            modifier = Modifier.fillMaxWidth(),

                            colors = OutlinedTextFieldDefaults.colors(

                                focusedBorderColor =
                                    MaterialTheme.colorScheme.primary,

                                focusedLabelColor =
                                    MaterialTheme.colorScheme.primary
                            ),

                            shape = RoundedCornerShape(12.dp)
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

                            modifier = Modifier.fillMaxWidth(),

                            visualTransformation =

                                if (passwordVisible)

                                    VisualTransformation.None

                                else

                                    PasswordVisualTransformation(),

                            trailingIcon = {

                                val image =

                                    if (passwordVisible)

                                        Icons.Filled.Visibility

                                    else

                                        Icons.Filled.VisibilityOff

                                IconButton(

                                    onClick = {

                                        passwordVisible =
                                            !passwordVisible
                                    }

                                ) {

                                    Icon(

                                        imageVector = image,

                                        contentDescription = null
                                    )
                                }
                            },

                            colors = OutlinedTextFieldDefaults.colors(

                                focusedBorderColor =
                                    MaterialTheme.colorScheme.primary,

                                focusedLabelColor =
                                    MaterialTheme.colorScheme.primary
                            ),

                            shape = RoundedCornerShape(12.dp)
                        )

                        Spacer(
                            modifier = Modifier.height(16.dp)
                        )

                        Button(

                            onClick = {

                                if (
                                    usuario.isEmpty() ||
                                    password.isEmpty()
                                ) {

                                    Toast.makeText(

                                        context,

                                        "Complete todos los campos",

                                        Toast.LENGTH_SHORT

                                    ).show()

                                    return@Button
                                }

                                scope.launch {

                                    val user =
                                        viewModel.login(
                                            usuario,
                                            password
                                        )

                                    if (user != null) {

                                        // validar admin

                                        if (
                                            loginAdmin
                                            &&
                                            user.rol != "admin"
                                        ) {

                                            Toast.makeText(

                                                context,

                                                "Este usuario no es administrador",

                                                Toast.LENGTH_SHORT

                                            ).show()

                                            return@launch
                                        }

                                        SessionManager.login(user)

                                        Toast.makeText(

                                            context,

                                            "Bienvenido ${user.user}",

                                            Toast.LENGTH_SHORT

                                        ).show()

                                        navController.navigate("menuP") {

                                            popUpTo("login") {

                                                inclusive = true
                                            }
                                        }

                                    } else {

                                        Toast.makeText(

                                            context,

                                            "Usuario o contraseña incorrectos",

                                            Toast.LENGTH_SHORT

                                        ).show()
                                    }
                                }
                            },

                            modifier = Modifier.fillMaxWidth(),

                            colors = ButtonDefaults.buttonColors(

                                containerColor =
                                    MaterialTheme.colorScheme.primary
                            ),

                            shape = RoundedCornerShape(12.dp)

                        ) {

                            Text(

                                text = "Entrar",

                                color =
                                    MaterialTheme.colorScheme.onPrimary
                            )
                        }

                        Spacer(
                            modifier = Modifier.height(10.dp)
                        )

                        TextButton(

                            onClick = {}

                        ) {

                            Text(

                                text = "¿Necesitas ayuda?",

                                color =
                                    MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }
        }
    }
}