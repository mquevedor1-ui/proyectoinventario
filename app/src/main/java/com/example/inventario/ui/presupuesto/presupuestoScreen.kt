package com.example.inventario.ui.presupuesto

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue

import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import androidx.lifecycle.viewmodel.compose.viewModel

import com.example.inventario.viewModel.ProductoViewModel

@Composable
fun PresupuestoScreen(

    bodegaId: String

) {

    val productoViewModel:
            ProductoViewModel = viewModel()

    val productos by productoViewModel
        .obtenerProductos(bodegaId)
        .collectAsState(initial = emptyList())

    val presupuestoTotal =
        productos.sumOf {

            it.presupuesto
        }

    Column(

        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),

        verticalArrangement =
            Arrangement.Center

    ) {

        Text(

            text = "Presupuesto Total",

            fontSize = 28.sp,

            fontWeight = FontWeight.Bold
        )

        Text(

            text = "Q $presupuestoTotal",

            fontSize = 40.sp,

            color =
                MaterialTheme.colorScheme.primary,

            fontWeight = FontWeight.Bold
        )
    }
}