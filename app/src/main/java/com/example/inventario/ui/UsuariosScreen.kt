package com.example.inventario.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items

import androidx.compose.foundation.shape.RoundedCornerShape

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit

import androidx.compose.material3.*

import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import androidx.lifecycle.viewmodel.compose.viewModel

import androidx.navigation.NavController

import com.example.inventario.data.usuario
import com.example.inventario.ui.AppTopBar
import com.example.inventario.viewModel.UsuarioViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UsuariosScreen(

    navController: NavController,

    viewModel: UsuarioViewModel = viewModel()

) {

    val usuarios by
    viewModel.usuarios.collectAsState()

    Scaffold(

        topBar = {

            AppTopBar(

                titulo = "Usuarios",

                navController = navController
            )
        }

    ) { padding ->

        LazyColumn(

            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),

            verticalArrangement =
                Arrangement.spacedBy(12.dp)

        ) {

            items(usuarios) { usuario ->

                Card(

                    modifier =
                        Modifier.fillMaxWidth(),

                    shape =
                        RoundedCornerShape(20.dp)

                ) {

                    Row(

                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),

                        verticalAlignment =
                            Alignment.CenterVertically

                    ) {

                        Column(

                            modifier =
                                Modifier.weight(1f)

                        ) {

                            Text(

                                text =
                                    usuario.user,

                                fontSize = 18.sp,

                                fontWeight =
                                    FontWeight.Bold
                            )

                            Spacer(
                                modifier = Modifier.height(4.dp)
                            )

                            Text(

                                text =
                                    "Rol: ${usuario.rol}"
                            )
                        }

                        IconButton(

                            onClick = {

                                viewModel.eliminarUsuario(
                                    usuario
                                )
                            }

                        ) {

                            Icon(

                                imageVector =
                                    Icons.Default.Delete,

                                contentDescription =
                                    "Eliminar"
                            )
                        }
                    }
                }
            }
        }
    }
}