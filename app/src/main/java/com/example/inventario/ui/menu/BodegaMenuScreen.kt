package com.example.inventario.ui.menu

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable

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
import androidx.compose.foundation.layout.width

import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home

import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text

import androidx.compose.runtime.Composable

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import androidx.navigation.NavController

// =========================
// MODELO
// =========================

data class Opcion(

    val titulo: String,

    val descripcion: String,

    val color: Color,

    val soloAdmin: Boolean,

    val ruta: String
)

// =========================
// LISTA OPCIONES
// =========================

@Composable
fun listaOpciones(): List<Opcion> {

    return listOf(

        Opcion(
            titulo = "Inventario General",
            descripcion = "Ver productos",
            color = MaterialTheme.colorScheme.primary,
            soloAdmin = false,
            ruta = "inventario"
        ),

        Opcion(
            titulo = "Crear Producto",
            descripcion = "Agregar nuevo",
            color = MaterialTheme.colorScheme.primary,
            soloAdmin = true,
            ruta = "crearProducto"
        ),

        Opcion(
            titulo = "Entradas",
            descripcion = "Ver ingresos",
            color = MaterialTheme.colorScheme.primary,
            soloAdmin = false,
            ruta = "entradas"
        ),

        Opcion(
            titulo = "Salidas",
            descripcion = "Ver salidas",
            color = MaterialTheme.colorScheme.primary,
            soloAdmin = false,
            ruta = "salidas"
        ),

        Opcion(
            titulo = "Facturas",
            descripcion = "Ver facturas",
            color = MaterialTheme.colorScheme.primary,
            soloAdmin = false,
            ruta = "facturas"
        ),

        Opcion(
            titulo = "Existencias",
            descripcion = "Stock actual",
            color = MaterialTheme.colorScheme.primary,
            soloAdmin = false,
            ruta = "existencias"
        ),

        Opcion(
            titulo = "Presupuesto",
            descripcion = "Valor total",
            color = MaterialTheme.colorScheme.primary,
            soloAdmin = false,
            ruta = "presupuesto"
        ),

        Opcion(
            titulo = "Stock Bajo",
            descripcion = "Alertas",
            color = MaterialTheme.colorScheme.primary,
            soloAdmin = false,
            ruta = "stockBajo"
        )
    )
}

// =========================
// PANTALLA MENU
// =========================

@Composable
fun BodegaMenuScreen(

    navController: NavController,

    bodegaId: String

) {

    val esAdmin = true

    val opcionesFiltradas = listaOpciones().filter {

        !it.soloAdmin || esAdmin
    }

    Column(

        modifier = Modifier
            .fillMaxSize()
            .background(

                Brush.verticalGradient(

                    listOf(

                        MaterialTheme.colorScheme.background,

                        MaterialTheme.colorScheme.surface
                    )
                )
            )
            .padding(16.dp)

    ) {

        // =========================
        // TITULO
        // =========================

        Text(

            text = "Sistema de Inventario",

            fontSize = 24.sp,

            fontWeight = FontWeight.Bold,

            color = MaterialTheme.colorScheme.onBackground
        )

        Text(

            text = "Bodega: $bodegaId",

            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(
            modifier = Modifier.height(20.dp)
        )

        // =========================
        // GRID
        // =========================

        LazyVerticalGrid(

            columns = GridCells.Fixed(2),

            verticalArrangement =
                Arrangement.spacedBy(16.dp),

            horizontalArrangement =
                Arrangement.spacedBy(16.dp)

        ) {

            items(opcionesFiltradas) { opcion ->

                CardOpcion(

                    opcion = opcion,

                    onClick = {

                        navController.navigate(

                            "${opcion.ruta}/$bodegaId"
                        )
                    }
                )
            }
        }
    }
}

// =========================
// TARJETA
// =========================

@Composable
fun CardOpcion(

    opcion: Opcion,

    onClick: () -> Unit

) {

    Card(

        modifier = Modifier
            .fillMaxWidth()
            .clickable {

                onClick()
            },

        shape = RoundedCornerShape(16.dp),

        colors = CardDefaults.cardColors(

            containerColor =
                MaterialTheme.colorScheme.surface
        ),

        elevation = CardDefaults.cardElevation(6.dp)

    ) {

        Row(

            modifier = Modifier.padding(16.dp),

            verticalAlignment = Alignment.CenterVertically

        ) {

            // =========================
            // ICONO
            // =========================

            Box(

                modifier = Modifier
                    .size(50.dp)
                    .background(

                        opcion.color,

                        CircleShape
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
                modifier = Modifier.width(10.dp)
            )

            // =========================
            // TEXTOS
            // =========================

            Column {

                Text(

                    text = opcion.titulo,

                    fontWeight = FontWeight.Bold,

                    color =
                        MaterialTheme.colorScheme.onSurface
                )

                Text(

                    text = opcion.descripcion,

                    fontSize = 12.sp,

                    color =
                        MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}