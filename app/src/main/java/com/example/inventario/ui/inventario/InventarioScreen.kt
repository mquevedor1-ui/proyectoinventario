package com.example.inventario.ui.inventario

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material.icons.filled.FileUpload
import androidx.compose.material.icons.filled.PictureAsPdf

import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton

import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

import androidx.lifecycle.viewmodel.compose.viewModel

import androidx.navigation.NavController

import com.example.inventario.data.producto

import com.example.inventario.ui.AppTopBar

import com.example.inventario.viewModel.ProductoViewModel
import com.example.inventario.viewModel.SessionManager

@OptIn(ExperimentalMaterial3Api::class)

@Composable
fun InventarioScreen(

    navController: NavController,

    bodegaId: String

) {

    val context = LocalContext.current

    val productoViewModel:
            ProductoViewModel = viewModel()

    // Launcher para importar Excel
    val launcherImportar = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            val productosImportados = importarExcel(context, it, bodegaId)
            productosImportados.forEach { p ->
                productoViewModel.agregarProducto(p)
            }
        }
    }

    LaunchedEffect(bodegaId) {

        productoViewModel
            .sincronizarDesdeFirebase(
                bodegaId
            )
    }

    val productos by productoViewModel
        .obtenerProductos(bodegaId)
        .collectAsState(initial = emptyList())

    var busqueda by remember {

        mutableStateOf("")
    }

    val productosFiltrados =
        remember(productos, busqueda) {

            productos.filter {

                it.descripcion.contains(
                    busqueda,
                    ignoreCase = true
                )

                        ||

                        it.codigo.contains(
                            busqueda,
                            ignoreCase = true
                        )

                        ||

                        it.categoria.contains(
                            busqueda,
                            ignoreCase = true
                        )
            }
        }

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),

        containerColor =
            MaterialTheme.colorScheme.background,

        topBar = {

            AppTopBar(

                titulo =
                    "Inventario General",

                subtitulo =
                    "Bodega: $bodegaId",

                navController =
                    navController,

                scrollBehavior = scrollBehavior
            )
        },

        floatingActionButton = {

            if (

                SessionManager.esAdmin()

            ) {

                FloatingActionButton(

                    onClick = {

                        navController.navigate(

                            "crearProducto/$bodegaId"
                        )
                    },

                    containerColor =
                        MaterialTheme.colorScheme.primary,

                    contentColor =
                        MaterialTheme.colorScheme.onPrimary

                ) {

                    Icon(

                        Icons.Default.Add,

                        contentDescription =
                            null
                    )
                }
            }
        }

    ) { padding ->

        Column(

            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),

            verticalArrangement =
                Arrangement.spacedBy(16.dp)
        ) {

            // buscar

            OutlinedTextField(

                value = busqueda,

                onValueChange = {

                    busqueda = it
                },

                label = {

                    Text(

                        "Buscar producto"
                    )
                },

                modifier =
                    Modifier.fillMaxWidth(),

                singleLine = true
            )

            // botones exportar

            Row(

                modifier =
                    Modifier.fillMaxWidth(),

                horizontalArrangement =
                    Arrangement.spacedBy(10.dp)
            ) {

                // pdf

                Button(

                    onClick = {

                        exportarInventarioPDF(

                            context =
                                context,

                            productos =
                                productosFiltrados,

                            bodega =
                                bodegaId
                        )
                    },

                    modifier =
                        Modifier.weight(1f),

                    colors =
                        ButtonDefaults.buttonColors(

                            containerColor =
                                MaterialTheme.colorScheme.primary
                        )
                ) {

                    Icon(

                        Icons.Default.PictureAsPdf,

                        contentDescription =
                            null,

                        modifier =
                            Modifier.size(18.dp)
                    )

                    Spacer(

                        modifier =
                            Modifier.width(6.dp)
                    )

                    Text(
                        "PDF"
                    )
                }

                // excel

                Button(

                    onClick = {

                        exportarExcel(

                            context,

                            productosFiltrados
                        )
                    },

                    modifier =
                        Modifier.weight(1f),

                    colors =
                        ButtonDefaults.buttonColors(

                            containerColor =
                                MaterialTheme.colorScheme.secondary
                        )
                ) {

                    Icon(

                        Icons.Default.FileDownload,

                        contentDescription =
                            null,

                        modifier =
                            Modifier.size(18.dp)
                    )

                    Spacer(

                        modifier =
                            Modifier.width(6.dp)
                    )

                    Text(
                        "Excel"
                    )
                }
            }

            // Botón Importar Excel (Solo Admin)
            if (SessionManager.esAdmin()) {
                OutlinedButton(
                    onClick = {
                        launcherImportar.launch("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Default.FileUpload, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text("Importar Inventario desde Excel")
                }
            }

            // titulo

            Text(

                text =
                    "Productos (${productosFiltrados.size})",

                style =
                    MaterialTheme.typography.titleMedium,

                fontWeight =
                    FontWeight.Bold
            )

            // lista

            LazyColumn(

                modifier =
                    Modifier.weight(1f),

                verticalArrangement =
                    Arrangement.spacedBy(10.dp)
            ) {

                items(

                    items =
                        productosFiltrados,

                    key = {
                        it.id
                    }

                ) { producto ->

                    ProductoCard(

                        producto = producto,

                        onEditar = {
                            if (SessionManager.esAdmin()) {
                                navController.navigate("editarProducto/${producto.id}")
                            }
                        },

                        onEliminar = {
                            if (SessionManager.esAdmin()) {
                                productoViewModel.eliminarProducto(producto)
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun ProductoCard(

    producto: producto,

    onEditar: () -> Unit,

    onEliminar: () -> Unit

) {

    Card(

        modifier =
            Modifier.fillMaxWidth(),

        colors =
            CardDefaults.cardColors(

                containerColor =
                    MaterialTheme.colorScheme.surface
            ),

        elevation =
            CardDefaults.cardElevation(

                defaultElevation = 3.dp
            )
    ) {

        Column(

            modifier =
                Modifier.padding(16.dp),

            verticalArrangement =
                Arrangement.spacedBy(4.dp)
        ) {

            Row(

                modifier =
                    Modifier.fillMaxWidth(),

                horizontalArrangement =
                    Arrangement.SpaceBetween
            ) {

                Column(

                    modifier =
                        Modifier.weight(1f)
                ) {

                    Text(

                        text =
                            producto.descripcion,

                        style =
                            MaterialTheme.typography.titleMedium,

                        fontWeight =
                            FontWeight.Bold
                    )

                    Text(
                        text =
                            "Código: ${producto.codigo}"
                    )

                    Text(
                        text =
                            "Categoría: ${producto.categoria}"
                    )

                    Text(
                        text =
                            "Cantidad: ${producto.cantidad}"
                    )

                    Text(
                        text =
                            "Unidad: ${producto.unidad}"
                    )

                    Text(
                        text =
                            "Ubicación: ${producto.ubicacion}"
                    )

                    Text(
                        text =
                            "Proveedor: ${producto.proveedor}"
                    )

                    Text(
                        text =
                            "Costo: Q ${producto.costo}"
                    )

                    Text(
                        text =
                            "Fecha: ${producto.fechaIngreso}"
                    )

                    Text(
                        text =
                            "Notas: ${producto.notas}"
                    )
                }

                Column {

                    Text(

                        text =
                            "${producto.cantidad}",

                        style =
                            MaterialTheme.typography.titleLarge,

                        fontWeight =
                            FontWeight.Bold,

                        color =

                            if (

                                producto.cantidad
                                <=
                                producto.stockMinimo

                            )

                                MaterialTheme
                                    .colorScheme
                                    .error

                            else

                                MaterialTheme
                                    .colorScheme
                                    .primary
                    )
                }
            }

            // stock bajo

            if (

                producto.cantidad
                <=
                producto.stockMinimo

            ) {

                Text(

                    text =
                        "⚠ Stock bajo",

                    color =
                        MaterialTheme
                            .colorScheme
                            .error,

                    fontWeight =
                        FontWeight.Bold
                )
            }

            // botones admin

            if (

                SessionManager.esAdmin()

            ) {

                HorizontalDivider(

                    modifier =
                        Modifier.padding(vertical = 8.dp)
                )

                Row(

                    modifier =
                        Modifier.fillMaxWidth(),

                    horizontalArrangement =
                        Arrangement.End
                ) {

                    TextButton(

                        onClick = onEditar
                    ) {

                        Icon(

                            Icons.Default.Edit,

                            contentDescription =
                                null,

                            modifier =
                                Modifier.size(18.dp)
                        )

                        Spacer(

                            modifier =
                                Modifier.width(4.dp)
                        )

                        Text(
                            "Editar"
                        )
                    }

                    TextButton(

                        onClick = onEliminar
                    ) {

                        Icon(

                            Icons.Default.Delete,

                            contentDescription =
                                null,

                            tint =
                                MaterialTheme
                                    .colorScheme
                                    .error,

                            modifier =
                                Modifier.size(18.dp)
                        )

                        Spacer(

                            modifier =
                                Modifier.width(4.dp)
                        )

                        Text(

                            text =
                                "Eliminar",

                            color =
                                MaterialTheme
                                    .colorScheme
                                    .error
                        )
                    }
                }
            }
        }
    }
}