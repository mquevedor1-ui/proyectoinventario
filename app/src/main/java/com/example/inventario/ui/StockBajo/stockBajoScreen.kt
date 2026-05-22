package com.example.inventario.ui.StockBajo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items

import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue

import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import androidx.lifecycle.viewmodel.compose.viewModel

import androidx.navigation.NavController

import com.example.inventario.viewModel.ProductoViewModel

@Composable
fun StockBajoScreen(

    navController: NavController,

    bodegaId: String

) {

    val productoViewModel:
            ProductoViewModel = viewModel()

    val productos by productoViewModel
        .obtenerProductos(bodegaId)
        .collectAsState(initial = emptyList())

    val productosStockBajo =

        productos.filter {

            it.cantidad <= it.stockMinimo
        }

    Column(

        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)

    ) {

        Text(

            text = "Productos con Stock Bajo",

            fontSize = 24.sp,

            fontWeight = FontWeight.Bold,

            color = Color.Red
        )

        LazyColumn(

            verticalArrangement =
                Arrangement.spacedBy(12.dp)

        ) {

            items(productosStockBajo) { producto ->

                Card(

                    modifier =
                        Modifier.fillMaxWidth()

                ) {

                    Column(

                        modifier =
                            Modifier.padding(16.dp)

                    ) {

                        Text(

                            text =
                                producto.descripcion,

                            fontWeight =
                                FontWeight.Bold
                        )

                        Text(

                            text =
                                "Existencias: ${producto.cantidad}"
                        )

                        Text(

                            text =
                                "Stock mínimo: ${producto.stockMinimo}",

                            color = Color.Red
                        )
                    }
                }
            }
        }
    }
}