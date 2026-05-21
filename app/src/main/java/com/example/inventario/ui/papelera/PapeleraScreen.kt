package com.example.inventario.ui.papelera

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Input
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.Restore
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Warehouse
import androidx.compose.material.icons.filled.People
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.inventario.viewModel.CategoriaViewModel
import com.example.inventario.viewModel.EntradaViewModel
import com.example.inventario.viewModel.FacturaViewModel
import com.example.inventario.viewModel.ProductoViewModel
import com.example.inventario.viewModel.SalidaViewModel
import com.example.inventario.viewModel.BodegaViewModel
import com.example.inventario.viewModel.UsuarioViewModel
import androidx.compose.material.icons.filled.Category
import androidx.compose.ui.platform.LocalConfiguration
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PapeleraScreen(
    navController: NavController,
    productoViewModel: ProductoViewModel,
    entradaViewModel: EntradaViewModel,
    salidaViewModel: SalidaViewModel,
    facturaViewModel: FacturaViewModel,
    bodegaViewModel: BodegaViewModel,
    usuarioViewModel: UsuarioViewModel,
    categoriaViewModel: CategoriaViewModel
) {
    var viewState by remember { mutableStateOf<PapeleraView>(PapeleraView.Main) }

    // Trigger auto-purge of items older than 90 days when entering the screen
    LaunchedEffect(Unit) {
        productoViewModel.purgarAntiguos()
        entradaViewModel.purgarAntiguos()
        salidaViewModel.purgarAntiguos()
        facturaViewModel.purgarAntiguos()
        bodegaViewModel.purgarAntiguos()
        usuarioViewModel.purgarAntiguos()
        categoriaViewModel.purgarAntiguos()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        when(viewState) {
                            PapeleraView.Main -> "Papelera de Reciclaje"
                            PapeleraView.Productos -> "Productos Eliminados"
                            PapeleraView.Entradas -> "Entradas Eliminadas"
                            PapeleraView.Salidas -> "Salidas Eliminadas"
                            PapeleraView.Facturas -> "Facturas Eliminadas"
                            PapeleraView.Bodegas -> "Bodegas Eliminadas"
                            PapeleraView.Usuarios -> "Usuarios Eliminados"
                            PapeleraView.Categorias -> "Categorías Eliminadas"
                        }, 
                        fontWeight = FontWeight.Bold
                    ) 
                },
                navigationIcon = {
                    IconButton(onClick = { 
                        if (viewState == PapeleraView.Main) navController.popBackStack() 
                        else viewState = PapeleraView.Main 
                    }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Regresar")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFE8F5E9),
                    titleContentColor = Color(0xFF2E7D32)
                )
            )
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding).fillMaxSize().padding(16.dp)) {
            when (viewState) {
                PapeleraView.Main -> PapeleraMenu { viewState = it }
                PapeleraView.Productos -> ProductosPapelera(productoViewModel)
                PapeleraView.Entradas -> EntradasPapelera(entradaViewModel)
                PapeleraView.Salidas -> SalidasPapelera(salidaViewModel)
                PapeleraView.Facturas -> FacturasPapelera(facturaViewModel)
                PapeleraView.Bodegas -> BodegasPapelera(bodegaViewModel)
                PapeleraView.Usuarios -> UsuariosPapelera(usuarioViewModel)
                PapeleraView.Categorias -> CategoriasPapelera(categoriaViewModel)
            }
        }
    }
}

enum class PapeleraView {
    Main, Productos, Entradas, Salidas, Facturas, Bodegas, Usuarios, Categorias
}

@Composable
fun PapeleraMenu(onNavigate: (PapeleraView) -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        PapeleraMenuOption("Productos", "Restaurar productos eliminados", Icons.Default.Inventory, { onNavigate(PapeleraView.Productos) })
        PapeleraMenuOption("Entradas", "Historial de entradas eliminadas", Icons.AutoMirrored.Filled.Input, { onNavigate(PapeleraView.Entradas) })
        PapeleraMenuOption("Salidas", "Historial de salidas eliminadas", Icons.AutoMirrored.Filled.Logout, { onNavigate(PapeleraView.Salidas) })
        PapeleraMenuOption("Facturas", "Facturas y registros de compra", Icons.Default.Receipt, { onNavigate(PapeleraView.Facturas) })
        PapeleraMenuOption("Categorías", "Categorías de productos eliminadas", Icons.Default.Category, { onNavigate(PapeleraView.Categorias) })
        PapeleraMenuOption("Bodegas", "Bodegas y almacenes eliminados", Icons.Default.Warehouse, { onNavigate(PapeleraView.Bodegas) })
        PapeleraMenuOption("Usuarios", "Cuentas de usuario eliminadas", Icons.Default.People, { onNavigate(PapeleraView.Usuarios) })
    }
}

@Composable
fun PapeleraMenuOption(title: String, subtitle: String, icon: androidx.compose.ui.graphics.vector.ImageVector, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Surface(
                shape = androidx.compose.foundation.shape.CircleShape,
                color = Color(0xFFF1F8E9),
                modifier = Modifier.size(48.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(icon, contentDescription = null, tint = Color(0xFF2E7D32))
                }
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(title, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text(subtitle, fontSize = 12.sp, color = Color.Gray)
            }
            Icon(Icons.Default.ChevronRight, contentDescription = null, tint = Color.LightGray)
        }
    }
}

@Composable
fun CategoriasPapelera(viewModel: CategoriaViewModel) {
    val items by viewModel.obtenerPapelera().collectAsState(initial = emptyList())
    if (items.isEmpty()) {
        EmptyPapelera()
    } else {
        LazyColumn(modifier = Modifier.fillMaxSize().padding(8.dp)) {
            items(items) { categoria ->
                PapeleraItemCard(
                    title = "Categoría: ${categoria.nombre}",
                    subtitle = "Prefijo: ${categoria.prefijo}",
                    date = categoria.deletionDate,
                    onRestore = { viewModel.restaurarCategoria(categoria) },
                    onDeletePermanent = { viewModel.eliminarPermanente(categoria) }
                )
            }
        }
    }
}

@Composable
fun BodegasPapelera(viewModel: BodegaViewModel) {
    val items by viewModel.obtenerPapelera().collectAsState(initial = emptyList())
    if (items.isEmpty()) {
        EmptyPapelera()
    } else {
        LazyColumn(modifier = Modifier.fillMaxSize().padding(8.dp)) {
            items(items) { bodega ->
                PapeleraItemCard(
                    title = "Bodega: ${bodega.nombre}",
                    subtitle = "ID: ${bodega.id}",
                    date = bodega.deletionDate,
                    onRestore = { viewModel.restaurarBodega(bodega) },
                    onDeletePermanent = { viewModel.eliminarPermanente(bodega) }
                )
            }
        }
    }
}

@Composable
fun UsuariosPapelera(viewModel: UsuarioViewModel) {
    val items by viewModel.obtenerPapelera().collectAsState(initial = emptyList())
    
    if (items.isEmpty()) {
        EmptyPapelera()
    } else {
        LazyColumn(modifier = Modifier.fillMaxSize().padding(8.dp)) {
            items(items) { usuario ->
                PapeleraItemCard(
                    title = "Usuario: ${usuario.user}",
                    subtitle = "Rol: ${usuario.rol}",
                    date = usuario.deletionDate,
                    onRestore = { viewModel.restaurarUsuario(usuario) },
                    onDeletePermanent = { viewModel.eliminarPermanente(usuario) }
                )
            }
        }
    }
}

@Composable
fun ProductosPapelera(viewModel: ProductoViewModel) {
    val items by viewModel.obtenerPapelera().collectAsState(initial = emptyList())
    
    if (items.isEmpty()) {
        EmptyPapelera()
    } else {
        LazyColumn(modifier = Modifier.fillMaxSize().padding(8.dp)) {
            items(items) { producto ->
                PapeleraItemCard(
                    title = producto.descripcion,
                    subtitle = "Código: ${producto.codigo}",
                    date = producto.deletionDate,
                    onRestore = { viewModel.restaurarProducto(producto) },
                    onDeletePermanent = { viewModel.eliminarPermanente(producto) }
                )
            }
        }
    }
}

@Composable
fun EntradasPapelera(viewModel: EntradaViewModel) {
    val items by viewModel.obtenerPapelera().collectAsState(initial = emptyList())
    if (items.isEmpty()) {
        EmptyPapelera()
    } else {
        LazyColumn(modifier = Modifier.fillMaxSize().padding(8.dp)) {
            items(items) { entrada ->
                PapeleraItemCard(
                    title = "Ingreso de ${entrada.descripcion} x${entrada.cantidad}",
                    subtitle = "Se eliminó registro de ${entrada.unidad}",
                    date = entrada.deletionDate,
                    onRestore = { viewModel.restaurarEntrada(entrada) },
                    onDeletePermanent = { viewModel.eliminarPermanente(entrada) }
                )
            }
        }
    }
}

@Composable
fun SalidasPapelera(viewModel: SalidaViewModel) {
    val items by viewModel.obtenerPapelera().collectAsState(initial = emptyList())
    if (items.isEmpty()) {
        EmptyPapelera()
    } else {
        LazyColumn(modifier = Modifier.fillMaxSize().padding(8.dp)) {
            items(items) { salida ->
                PapeleraItemCard(
                    title = "Salida de ${salida.descripcion} x${salida.cantidad}",
                    subtitle = "Llevado por ${salida.responsable}",
                    date = salida.deletionDate,
                    onRestore = { viewModel.restaurarSalida(salida) },
                    onDeletePermanent = { viewModel.eliminarPermanente(salida) }
                )
            }
        }
    }
}

@Composable
fun FacturasPapelera(viewModel: FacturaViewModel) {
    val items by viewModel.obtenerPapelera().collectAsState(initial = emptyList())
    if (items.isEmpty()) {
        EmptyPapelera()
    } else {
        LazyColumn(modifier = Modifier.fillMaxSize().padding(8.dp)) {
            items(items) { factura ->
                PapeleraItemCard(
                    title = "Factura: ${factura.numeroFactura}",
                    subtitle = "Proveedor: ${factura.proveedor}",
                    date = factura.deletionDate,
                    onRestore = { viewModel.restaurarFactura(factura) },
                    onDeletePermanent = { viewModel.eliminarPermanente(factura) }
                )
            }
        }
    }
}

@Composable
fun PapeleraItemCard(
    title: String,
    subtitle: String,
    date: Long?,
    onRestore: () -> Unit,
    onDeletePermanent: () -> Unit
) {
    val configuration = LocalConfiguration.current
    val locale = remember(configuration) { configuration.locales[0] }
    val sdf = remember(locale) { SimpleDateFormat("dd/MM/yyyy HH:mm", locale) }
    val fechaEliminacion = remember(date, sdf) { 
        if (date != null) sdf.format(Date(date)) else "Desconocida" 
    }
    var showDeleteConfirm by remember { mutableStateOf(false) }

    if (showDeleteConfirm) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirm = false },
            title = { Text("Eliminar definitivamente") },
            text = { Text("¿Está seguro que desea eliminar este registro de forma permanente? Esta acción no se puede deshacer.") },
            confirmButton = {
                TextButton(onClick = {
                    onDeletePermanent()
                    showDeleteConfirm = false
                }) {
                    Text("Eliminar", color = Color.Red)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteConfirm = false }) {
                    Text("Cancelar")
                }
            }
        )
    }

    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(title, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text(subtitle, style = MaterialTheme.typography.bodySmall)
                Text("Eliminado: $fechaEliminacion", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
            }
            Row {
                IconButton(onClick = onRestore) {
                    Icon(Icons.Default.Restore, contentDescription = "Restaurar", tint = Color(0xFF4CAF50))
                }
                IconButton(onClick = { showDeleteConfirm = true }) {
                    Icon(Icons.Default.DeleteForever, contentDescription = "Eliminar Permanente", tint = Color(0xFFF44336))
                }
            }
        }
    }
}


@Composable
fun EmptyPapelera() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(Icons.Default.Delete, contentDescription = null, modifier = Modifier.size(64.dp), tint = Color.LightGray)
            Text("Papelera vacía", color = Color.Gray)
        }
    }
}
