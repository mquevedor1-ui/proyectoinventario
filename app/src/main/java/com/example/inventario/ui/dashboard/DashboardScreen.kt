package com.example.inventario.ui.dashboard

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items

import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import androidx.lifecycle.viewmodel.compose.viewModel

import com.example.inventario.viewModel.ProductoViewModel

data class DashboardItem(

    val titulo: String,

    val valor: String
)

@Composable
fun DashboardScreen(

    bodegaId: String

) {

    val productoViewModel:
            ProductoViewModel = viewModel()

    val productos by productoViewModel
        .obtenerProductos(bodegaId)
        .collectAsState(initial = emptyList())

    val totalProductos =
        productos.size

    val totalExistencias =
        productos.sumOf {

            it.cantidad
        }

    val stockBajo =
        productos.count {

            it.cantidad <= it.stockMinimo
        }

    val presupuestoTotal =
        productos.sumOf {

            it.presupuesto
        }

    val datos = listOf(

        DashboardItem(
            "Productos",
            totalProductos.toString()
        ),

        DashboardItem(
            "Stock Bajo",
            stockBajo.toString()
        ),

        DashboardItem(
            "Existencias",
            totalExistencias.toString()
        ),

        DashboardItem(
            "Presupuesto",
            "Q ${presupuestoTotal}"
        )
    )

    Column(

        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)

    ) {

        Text(

            text = "Dashboard",

            fontSize = 26.sp,

            fontWeight = FontWeight.Bold
        )

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        LazyVerticalGrid(

            columns =
                GridCells.Fixed(2),

            verticalArrangement =
                Arrangement.spacedBy(12.dp),

            horizontalArrangement =
                Arrangement.spacedBy(12.dp)
        ) {

            items(datos) { item ->

                Card(

                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .height(140.dp)

                ) {

                    Column(

                        modifier =
                            Modifier
                                .fillMaxSize()
                                .padding(16.dp),

                        verticalArrangement =
                            Arrangement.Center,

                        horizontalAlignment =
                            Alignment.CenterHorizontally
                    ) {

                        Text(

                            text = item.valor,

                            fontSize = 24.sp,

                            fontWeight = FontWeight.Bold,

                            color =
                                MaterialTheme.colorScheme.primary
                        )

                        Spacer(
                            modifier = Modifier.height(8.dp)
                        )

                        Text(

                            text = item.titulo
                        )
                    }
                }
            }
        }
    }
}