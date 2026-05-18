package com.example.inventario.ui.inventario

import android.app.Application
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.inventario.data.producto
import com.example.inventario.viewModel.ProductoViewModel
import com.example.inventario.viewModel.SessionManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun EditarProductoScreen(

    navController: NavController,

    productoId: Int

) {

    // context
    val context =
        LocalContext.current

    // viewmodel
    val productoViewModel:
            ProductoViewModel = viewModel(

        factory =
            ViewModelProvider
                .AndroidViewModelFactory
                .getInstance(

                    context.applicationContext
                            as Application
                )
    )

    // producto
    var productoActual by remember {

        mutableStateOf<producto?>(null)
    }

    // estados
    var codigo by remember {

        mutableStateOf("")
    }

    var descripcion by remember {

        mutableStateOf("")
    }

    var categoria by remember {

        mutableStateOf("")
    }

    var cantidad by remember {

        mutableStateOf("")
    }

    var unidad by remember {

        mutableStateOf("")
    }

    var ubicacion by remember {

        mutableStateOf("")
    }

    var proveedor by remember {

        mutableStateOf("")
    }

    var costo by remember {

        mutableStateOf("")
    }

    var stockMinimo by remember {

        mutableStateOf("")
    }

    var fechaIngreso by remember {

        mutableStateOf("")
    }

    var notas by remember {

        mutableStateOf("")
    }

    // cargar producto
    LaunchedEffect(Unit) {

        val producto =

            productoViewModel
                .obtenerProductoPorId(
                    productoId
                )

        productoActual = producto

        producto?.let {

            codigo = it.codigo

            descripcion =
                it.descripcion

            categoria =
                it.categoria

            cantidad =
                it.cantidad.toString()

            unidad =
                it.unidad

            ubicacion =
                it.ubicacion

            proveedor =
                it.proveedor

            costo =
                it.costo.toString()

            stockMinimo =
                it.stockMinimo.toString()

            fechaIngreso =
                it.fechaIngreso

            notas =
                it.notas
        }
    }

    Column(

        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(
                rememberScrollState()
            )
            .padding(16.dp),

        verticalArrangement =
            Arrangement.spacedBy(16.dp)

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

        // card
        Card(

            modifier =
                Modifier.fillMaxWidth(),

            colors =
                CardDefaults.cardColors(

                    containerColor =
                        MaterialTheme
                            .colorScheme
                            .surface
                )

        ) {

            Column(

                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),

                verticalArrangement =
                    Arrangement.spacedBy(12.dp)

            ) {

                // titulo
                Text(

                    text = "Editar Producto",

                    style =
                        MaterialTheme
                            .typography
                            .headlineMedium
                )

                // codigo
                OutlinedTextField(

                    value = codigo,

                    onValueChange = {},

                    enabled = false,

                    label = {

                        Text("Código")
                    },

                    modifier =
                        Modifier.fillMaxWidth()
                )

                // descripcion
                OutlinedTextField(

                    value = descripcion,

                    onValueChange = {

                        if (SessionManager.esAdmin()) {

                            descripcion = it
                        }
                    },

                    readOnly =
                        !SessionManager.esAdmin(),

                    label = {

                        Text("Descripción")
                    },

                    modifier =
                        Modifier.fillMaxWidth()
                )

                // categoria
                OutlinedTextField(

                    value = categoria,

                    onValueChange = {

                        if (SessionManager.esAdmin()) {

                            categoria = it
                        }
                    },

                    readOnly =
                        !SessionManager.esAdmin(),

                    label = {

                        Text("Categoría")
                    },

                    modifier =
                        Modifier.fillMaxWidth()
                )

                // cantidad
                OutlinedTextField(

                    value = cantidad,

                    onValueChange = {

                        if (SessionManager.esAdmin()) {

                            cantidad = it
                        }
                    },

                    readOnly =
                        !SessionManager.esAdmin(),

                    label = {

                        Text("Cantidad")
                    },

                    modifier =
                        Modifier.fillMaxWidth()
                )

                // unidad
                OutlinedTextField(

                    value = unidad,

                    onValueChange = {

                        if (SessionManager.esAdmin()) {

                            unidad = it
                        }
                    },

                    readOnly =
                        !SessionManager.esAdmin(),

                    label = {

                        Text("Unidad")
                    },

                    modifier =
                        Modifier.fillMaxWidth()
                )

                // ubicacion
                OutlinedTextField(

                    value = ubicacion,

                    onValueChange = {

                        if (SessionManager.esAdmin()) {

                            ubicacion = it
                        }
                    },

                    readOnly =
                        !SessionManager.esAdmin(),

                    label = {

                        Text("Ubicación")
                    },

                    modifier =
                        Modifier.fillMaxWidth()
                )

                // proveedor
                OutlinedTextField(

                    value = proveedor,

                    onValueChange = {

                        if (SessionManager.esAdmin()) {

                            proveedor = it
                        }
                    },

                    readOnly =
                        !SessionManager.esAdmin(),

                    label = {

                        Text("Proveedor")
                    },

                    modifier =
                        Modifier.fillMaxWidth()
                )

                // costo
                OutlinedTextField(

                    value = costo,

                    onValueChange = {

                        if (SessionManager.esAdmin()) {

                            costo = it
                        }
                    },

                    readOnly =
                        !SessionManager.esAdmin(),

                    label = {

                        Text("Costo Unitario")
                    },

                    modifier =
                        Modifier.fillMaxWidth()
                )

                // stock
                OutlinedTextField(

                    value = stockMinimo,

                    onValueChange = {

                        if (SessionManager.esAdmin()) {

                            stockMinimo = it
                        }
                    },

                    readOnly =
                        !SessionManager.esAdmin(),

                    label = {

                        Text("Stock Mínimo")
                    },

                    modifier =
                        Modifier.fillMaxWidth()
                )

                // fecha
                OutlinedTextField(

                    value = fechaIngreso,

                    onValueChange = {},

                    enabled = false,

                    label = {

                        Text("Fecha Ingreso")
                    },

                    modifier =
                        Modifier.fillMaxWidth()
                )

                // notas
                OutlinedTextField(

                    value = notas,

                    onValueChange = {

                        if (SessionManager.esAdmin()) {

                            notas = it
                        }
                    },

                    readOnly =
                        !SessionManager.esAdmin(),

                    label = {

                        Text("Notas")
                    },

                    modifier =
                        Modifier.fillMaxWidth(),

                    minLines = 4
                )

                Spacer(
                    modifier =
                        Modifier.height(12.dp)
                )

                // actualizar
                if (SessionManager.esAdmin()) {

                    Button(

                        modifier =
                            Modifier.fillMaxWidth(),

                        onClick = {

                            productoActual?.let {

                                val actualizado =

                                    it.copy(

                                        descripcion =
                                            descripcion,

                                        categoria =
                                            categoria,

                                        cantidad =
                                            cantidad
                                                .toIntOrNull()
                                                ?: 0,

                                        unidad =
                                            unidad,

                                        ubicacion =
                                            ubicacion,

                                        proveedor =
                                            proveedor,

                                        costo =
                                            costo
                                                .toDoubleOrNull()
                                                ?: 0.0,

                                        stockMinimo =
                                            stockMinimo
                                                .toIntOrNull()
                                                ?: 0,

                                        notas =
                                            notas
                                    )

                                CoroutineScope(
                                    Dispatchers.IO
                                ).launch {

                                    productoViewModel
                                        .actualizarProducto(
                                            actualizado
                                        )
                                }

                                navController
                                    .popBackStack()
                            }
                        }

                    ) {

                        Text(
                            "Actualizar Producto"
                        )
                    }
                }
            }
        }
    }
}