package com.example.inventario.ui.menu

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding

import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar

import androidx.compose.runtime.Composable

import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

import androidx.navigation.NavController

import com.example.inventario.viewModel.SessionManager

data class OpcionBodega(

    val titulo: String,

    val descripcion: String,

    val ruta: String,

    val color: Color
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BodegaMenuScreen(

    navController: NavController,

    bodegaId: String

) {

    val rol =
        SessionManager.rolUsuario()

    val opciones = mutableListOf(

        OpcionBodega(
            "Inventario General",
            "Ver productos",
            "inventario/$bodegaId",
            Color(0xFF2962FF)
        ),

        OpcionBodega(
            "Entradas",
            "Ver ingresos",
            "entradas/$bodegaId",
            Color(0xFF00C853)
        ),

        OpcionBodega(
            "Salidas",
            "Ver salidas",
            "salidas/$bodegaId",
            Color(0xFFD50000)
        ),

        OpcionBodega(
            "Stock Bajo",
            "Alertas",
            "stockBajo/$bodegaId",
            Color(0xFFFF6D00)
        )
    )

    // admin y encargado

    if (

        rol == "admin"
        ||
        rol == "encargado"

    ) {

        opciones.add(

            1,

            OpcionBodega(
                "Crear Producto",
                "Agregar nuevo",
                "crearProducto/$bodegaId",
                Color(0xFFAA00FF)
            )
        )

        opciones.add(

            OpcionBodega(
                "Facturas",
                "Ver facturas",
                "facturas/$bodegaId",
                Color(0xFFFFAB00)
            )
        )

        opciones.add(

            OpcionBodega(
                "Presupuesto",
                "Valor total",
                "presupuesto/$bodegaId",
                Color(0xFF6200EA)
            )
        )
    }

    Scaffold(

        topBar = {

            TopAppBar(

                title = {

                    Text(
                        text = "Sistema Inventario"
                    )
                },

                navigationIcon = {

                    IconButton(

                        onClick = {

                            navController.popBackStack()
                        }

                    ) {

                        Icon(

                            imageVector =
                                Icons.Default.ArrowBack,

                            contentDescription =
                                "Regresar"
                        )
                    }
                }
            )
        }

    ) { padding ->

        Box(

            modifier = Modifier
                .fillMaxSize()
                .background(
                    Color(0xFFEAF7EC)
                )
                .padding(padding)

        ) {

            LazyVerticalGrid(

                columns =
                    GridCells.Adaptive(
                        minSize = 160.dp
                    ),

                contentPadding =
                    PaddingValues(16.dp),

                horizontalArrangement =
                    Arrangement.spacedBy(16.dp),

                verticalArrangement =
                    Arrangement.spacedBy(16.dp)

            ) {

                items(opciones) { opcion ->

                    CardBodega(

                        titulo =
                            opcion.titulo,

                        descripcion =
                            opcion.descripcion,

                        color =
                            opcion.color,

                        ruta = when {

                            opcion.ruta.contains(
                                "inventario"
                            ) ->
                                "inventario"

                            opcion.ruta.contains(
                                "crearProducto"
                            ) ->
                                "crearProducto"

                            opcion.ruta.contains(
                                "entradas"
                            ) ->
                                "entradas"

                            opcion.ruta.contains(
                                "salidas"
                            ) ->
                                "salidas"

                            opcion.ruta.contains(
                                "facturas"
                            ) ->
                                "facturas"

                            opcion.ruta.contains(
                                "presupuesto"
                            ) ->
                                "presupuesto"

                            opcion.ruta.contains(
                                "stockBajo"
                            ) ->
                                "stockBajo"

                            else ->
                                "inventario"
                        },

                        onClick = {

                            navController.navigate(
                                opcion.ruta
                            )
                        }
                    )
                }
            }
        }
    }
}