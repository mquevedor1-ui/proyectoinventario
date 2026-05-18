package com.example.inventario.ui.inventario



import android.app.Application
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.inventario.data.producto
import com.example.inventario.viewModel.ProductoViewModel
import com.example.inventario.viewModel.SessionManager

@Composable
fun InventarioGeneralScreen(

    navController: NavController,

    bodegaId: String

) {

    // context
    val context =
        LocalContext.current

    // viewmodel
    val productoViewModel: ProductoViewModel = viewModel()

    // Sincronizar con Firebase al entrar a la pantalla
    androidx.compose.runtime.LaunchedEffect(bodegaId) {
        productoViewModel.sincronizarDesdeFirebase(bodegaId)
    }

    // productos
    val productos by productoViewModel
        .obtenerProductos(bodegaId)
        .collectAsState(initial = emptyList())

    // buscar
    var busqueda by remember { mutableStateOf("") }

    // filtro optimizado
    val productosFiltrados = remember(productos, busqueda) {
        productos.filter {
            it.descripcion.contains(busqueda, ignoreCase = true) ||
                    it.codigo.contains(busqueda, ignoreCase = true) ||
                    it.categoria.contains(busqueda, ignoreCase = true)
        }
    }

    Column(

        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),

        verticalArrangement =
            Arrangement.spacedBy(12.dp)

    ) {

        // regresar
        Button(

            onClick = {

                navController.popBackStack()
            }

        ) {

            Icon(

                Icons.Default.ArrowBack,

                contentDescription = null
            )

            Text("Regresar")
        }

        // titulo
        Text(

            text =
                "Inventario General",

            style =
                MaterialTheme
                    .typography
                    .headlineMedium
        )

        // buscar
        OutlinedTextField(
            value = busqueda,
            onValueChange = { busqueda = it },
            label = { Text("Buscar producto") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        // botones exportar
        Row(

            horizontalArrangement =
                Arrangement.spacedBy(8.dp)

        ) {

            // excel
            Button(

                onClick = {

                    exportarExcel(

                        context,

                        productosFiltrados
                    )
                }

            ) {

                Text("Exportar Excel")
            }

            // pdf
            Button(

                onClick = {

                    exportarPDF(

                        context,

                        productosFiltrados
                    )
                }

            ) {

                Text("Exportar PDF")
            }
        }

        // agregar
        if (SessionManager.esAdmin()) {

            Button(

                onClick = {

                    navController.navigate(

                        "crearProducto/$bodegaId"
                    )
                }

            ) {

                Icon(

                    Icons.Default.Add,

                    contentDescription = null
                )

                Text("Agregar Producto")
            }
        }

        Spacer(

            modifier =
                Modifier.height(10.dp)
        )

        // lista productos
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(
                items = productosFiltrados,
                key = { it.id }
            ) { producto ->
                ProductoCard(
                    producto = producto,
                    onEditar = {
                        navController.navigate("editarProducto/${producto.id}")
                    },
                    onEliminar = {
                        productoViewModel.eliminarProducto(producto)
                    }
                )
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
            Modifier.fillMaxWidth()

    ) {

        Column(

            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),

            verticalArrangement =
                Arrangement.spacedBy(6.dp)

        ) {

            // descripcion
            Text(

                text =
                    producto.descripcion,

                style =
                    MaterialTheme
                        .typography
                        .titleMedium
            )

            // codigo
            Text(

                text =
                    "Código: ${producto.codigo}"
            )

            // categoria
            Text(

                text =
                    "Categoría: ${producto.categoria}"
            )

            // cantidad
            Text(

                text =
                    "Cantidad: ${producto.cantidad}"
            )

            // proveedor
            Text(

                text =
                    "Proveedor: ${producto.proveedor}"
            )

            // stock bajo
            if (

                producto.cantidad
                <=
                producto.stockMinimo

            ) {

                Text(

                    text =
                        "⚠ Stock Bajo",

                    color =
                        MaterialTheme
                            .colorScheme
                            .error
                )
            }

            // botones admin
            if (

                SessionManager.esAdmin()

            ) {

                Row(

                    horizontalArrangement =
                        Arrangement.spacedBy(8.dp)

                ) {

                    // editar
                    IconButton(

                        onClick = onEditar

                    ) {

                        Icon(

                            Icons.Default.Edit,

                            contentDescription =
                                null
                        )
                    }

                    // eliminar
                    IconButton(

                        onClick = onEliminar

                    ) {

                        Icon(

                            Icons.Default.Delete,

                            contentDescription =
                                null
                        )
                    }
                }
            }
        }
    }
}