package com.example.inventario.ui.inventario

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add

import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue

import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

import androidx.lifecycle.viewmodel.compose.viewModel

import androidx.navigation.NavController

import com.example.inventario.data.categoria
import com.example.inventario.data.producto

import com.example.inventario.ui.AppTopBar

import com.example.inventario.viewModel.CategoriaViewModel
import com.example.inventario.viewModel.ProductoViewModel
import com.example.inventario.viewModel.SessionManager

import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)

@Composable
fun CrearProductoScreen(

    navController: NavController,

    bodegaId: String

) {

    val productoViewModel: ProductoViewModel = viewModel()

    val categoriaViewModel: CategoriaViewModel = viewModel()

    val coroutineScope = rememberCoroutineScope()

    val categorias by categoriaViewModel
        .categorias
        .collectAsState(initial = emptyList())

    var descripcion by remember {

        mutableStateOf("")
    }

    var categoriaSeleccionada by remember {

        mutableStateOf("")
    }

    var prefijoSeleccionado by remember {

        mutableStateOf("P")
    }

    var codigoSiguiente by remember {

        mutableStateOf("P0001")
    }

    var expanded by remember {

        mutableStateOf(false)
    }

    var nuevaCategoria by remember {

        mutableStateOf("")
    }

    fun actualizarCodigo(

        nuevoPrefijo: String

    ) {

        coroutineScope.launch {

            codigoSiguiente =

                productoViewModel
                    .generarSiguienteCodigo(
                        nuevoPrefijo
                    )
        }
    }

    Scaffold(

        topBar = {

            AppTopBar(

                titulo =
                    "Agregar Nuevo Producto",

                navController =
                    navController
            )
        }

    ) { padding ->

        Column(

            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(
                    rememberScrollState()
                )
                .padding(20.dp),

            verticalArrangement =
                Arrangement.spacedBy(12.dp)
        ) {

            Card(

                modifier =
                    Modifier.fillMaxWidth()

            ) {

                Column(

                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),

                    verticalArrangement =
                        Arrangement.spacedBy(14.dp)
                ) {

                    // descripcion

                    OutlinedTextField(

                        value =
                            descripcion,

                        onValueChange = {

                            descripcion = it
                        },

                        label = {

                            Text(
                                "Descripción"
                            )
                        },

                        modifier =
                            Modifier.fillMaxWidth()
                    )

                    // categorias

                    ExposedDropdownMenuBox(

                        expanded = expanded,

                        onExpandedChange = {

                            expanded = !expanded
                        }

                    ) {

                        OutlinedTextField(

                            value =
                                categoriaSeleccionada,

                            onValueChange = {},

                            readOnly = true,

                            label = {

                                Text(
                                    "Seleccionar Categoría"
                                )
                            },

                            trailingIcon = {

                                ExposedDropdownMenuDefaults
                                    .TrailingIcon(
                                        expanded =
                                            expanded
                                    )
                            },

                            modifier = Modifier
                                .menuAnchor()
                                .fillMaxWidth()
                        )

                        ExposedDropdownMenu(

                            expanded = expanded,

                            onDismissRequest = {

                                expanded = false
                            }

                        ) {

                            categorias.forEach { cat ->

                                DropdownMenuItem(

                                    text = {

                                        Text(
                                            cat.nombre
                                        )
                                    },

                                    onClick = {

                                        categoriaSeleccionada =
                                            cat.nombre

                                        prefijoSeleccionado =
                                            cat.prefijo

                                        actualizarCodigo(
                                            cat.prefijo
                                        )

                                        expanded = false
                                    }
                                )
                            }
                        }
                    }

                    // codigo

                    OutlinedTextField(

                        value =
                            codigoSiguiente,

                        onValueChange = {},

                        readOnly = true,

                        label = {

                            Text(
                                "Código"
                            )
                        },

                        modifier =
                            Modifier.fillMaxWidth()
                    )

                    // nueva categoria

                    if (

                        SessionManager
                            .esAdmin()

                    ) {

                        OutlinedTextField(

                            value =
                                nuevaCategoria,

                            onValueChange = {

                                nuevaCategoria = it
                            },

                            label = {

                                Text(
                                    "Nueva Categoría"
                                )
                            },

                            modifier =
                                Modifier.fillMaxWidth()
                        )

                        Button(

                            onClick = {

                                    val nombre = nuevaCategoria.trim()
                                    if (nombre.isNotEmpty()) {
                                        coroutineScope.launch {
                                            var pfx = nombre.take(1).uppercase()
                                            var i = 1
                                            while (i < nombre.length && categoriaViewModel.buscarPorPrefijo(pfx) != null) {
                                                i++
                                                pfx = nombre.take(i).uppercase()
                                            }
                                            
                                            // Si llegamos al final y sigue existiendo, agregamos un número
                                            if (categoriaViewModel.buscarPorPrefijo(pfx) != null) {
                                                var count = 1
                                                while (categoriaViewModel.buscarPorPrefijo(pfx + count) != null) {
                                                    count++
                                                }
                                                pfx += count
                                            }

                                            categoriaViewModel.insertarCategoria(
                                                categoria(
                                                    nombre = nombre,
                                                    prefijo = pfx
                                                )
                                            )

                                            categoriaSeleccionada = nombre
                                            prefijoSeleccionado = pfx
                                            actualizarCodigo(pfx)
                                            nuevaCategoria = ""
                                        }
                                    }
                            }

                        ) {

                            Icon(

                                Icons.Default.Add,

                                contentDescription =
                                    null
                            )

                            Spacer(

                                modifier =
                                    Modifier.width(6.dp)
                            )

                            Text(
                                "Guardar Categoría"
                            )
                        }
                    }

                    // guardar producto

                    Button(

                        modifier =
                            Modifier.fillMaxWidth(),

                        enabled =

                            descripcion.isNotEmpty()
                                    &&
                                    categoriaSeleccionada.isNotEmpty(),

                        onClick = {

                            coroutineScope.launch {

                                productoViewModel
                                    .agregarProducto(

                                        producto(

                                            bodegaId =
                                                bodegaId,

                                            codigo =
                                                codigoSiguiente,

                                            descripcion =
                                                descripcion,

                                            categoria =
                                                categoriaSeleccionada,

                                            prefijoCategoria =
                                                prefijoSeleccionado,

                                            cantidad = 0,

                                            unidad = "",

                                            ubicacion = "",

                                            proveedor = "",

                                            costo = 0.0,

                                            stockMinimo = 0,

                                            fechaIngreso = "",

                                            notas = ""
                                        )
                                    )

                                navController
                                    .popBackStack()
                            }
                        }

                    ) {

                        Text(
                            "Guardar Producto"
                        )
                    }
                }
            }
        }
    }
}