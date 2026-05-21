package com.example.inventario.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
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
    var user by remember { mutableStateOf("") }
    var pass by remember { mutableStateOf("") }
    var esAdmin by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            AppTopBar(
                titulo = "Crear Nuevo Usuario",
                navController = navController
            )
        },
        containerColor = MaterialTheme.colorScheme.background
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
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "Información del Usuario",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    OutlinedTextField(
                        value = user,
                        onValueChange = { user = it },
                        label = { Text("Nombre de Usuario") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )

                    OutlinedTextField(
                        value = pass,
                        onValueChange = { pass = it },
                        label = { Text("Contraseña") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )

                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                    Text(
                        text = "Seleccionar Rol",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )

                    Row(modifier = Modifier.fillMaxWidth()) {
                        Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                            RadioButton(
                                selected = !esAdmin,
                                onClick = { esAdmin = false }
                            )
                            Text(text = "Usuario Común")
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                            RadioButton(
                                selected = esAdmin,
                                onClick = { esAdmin = true }
                            )
                            Text(text = "Administrador")
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            if (user.isNotEmpty() && pass.isNotEmpty()) {
                                val rol = if (esAdmin) "admin" else "usuario"
                                viewModel.crearUsuario(user, pass, rol)
                                navController.popBackStack()
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(text = "Guardar Usuario", fontSize = 16.sp)
                    }
                }
            }
        }
    }
}
