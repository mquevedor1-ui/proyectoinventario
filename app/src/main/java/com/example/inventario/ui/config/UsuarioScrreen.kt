package com.example.inventario.ui.config

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.inventario.data.usuario
import com.example.inventario.viewModel.SessionManager
import com.example.inventario.viewModel.UsuarioViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UsuariosScreen(
    navController: NavController,
    viewModel: UsuarioViewModel
) {
    val listaUsuarios by viewModel.usuarios.collectAsState()
    var usuarioAEditar by remember { mutableStateOf<usuario?>(null) }

    if (!SessionManager.esAdmin()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(text = "Acceso denegado", color = MaterialTheme.colorScheme.error, fontSize = 20.sp)
        }
        return
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Gestión de Usuarios", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Atrás")
                    }
                },
                actions = {
                    IconButton(onClick = { navController.navigate("crearUsuario") }) {
                        Icon(Icons.Default.PersonAdd, contentDescription = "Crear Usuario")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            if (listaUsuarios.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(listaUsuarios) { user ->
                        UsuarioItem(
                            usuario = user,
                            onEdit = { usuarioAEditar = user },
                            onDelete = { viewModel.eliminarUsuario(user) }
                        )
                    }
                }
            }
        }

        // Diálogo para editar
        if (usuarioAEditar != null) {
            EditUserDialog(
                usuario = usuarioAEditar!!,
                onDismiss = { usuarioAEditar = null },
                onConfirm = { nombre, pass, rol ->
                    viewModel.editarUsuario(usuarioAEditar!!, nombre, pass, rol)
                    usuarioAEditar = null
                }
            )
        }
    }
}

@Composable
fun UsuarioItem(
    usuario: usuario,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                // Usamos ifEmpty para asegurar que siempre haya un texto y evitamos casts innecesarios
                Text(
                    text = usuario.user.ifEmpty { "Sin nombre" },
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.primary
                )
                // Aseguramos que el rol no esté vacío antes de llamar a uppercase()
                val rolSafe = usuario.rol.ifEmpty { "usuario" }
                Text(
                    text = "Rol: ${rolSafe.uppercase()}",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Row {
                IconButton(onClick = onEdit) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Editar",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
                
                if (usuario.user != "admin") {
                    IconButton(onClick = onDelete) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Eliminar",
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun EditUserDialog(
    usuario: usuario,
    onDismiss: () -> Unit,
    onConfirm: (String, String, String) -> Unit
) {
    var nombre by remember { mutableStateOf(usuario.user) }
    var pass by remember { mutableStateOf(usuario.pass) }
    var rol by remember { mutableStateOf(usuario.rol) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Editar Usuario") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    label = { Text("Usuario") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = pass,
                    onValueChange = { pass = it },
                    label = { Text("Contraseña") },
                    modifier = Modifier.fillMaxWidth()
                )
                Text("Rol:", fontWeight = FontWeight.Bold)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(selected = rol == "usuario", onClick = { rol = "usuario" })
                    Text("Usuario")
                    Spacer(modifier = Modifier.width(8.dp))
                    RadioButton(selected = rol == "admin", onClick = { rol = "admin" })
                    Text("Admin")
                }
            }
        },
        confirmButton = {
            Button(onClick = { onConfirm(nombre, pass, rol) }) {
                Text("Guardar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}
