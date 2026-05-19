package com.example.inventario.ui.menu
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*

import androidx.compose.foundation.shape.RoundedCornerShape

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*

import androidx.compose.material3.*

import androidx.compose.runtime.Composable

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun CardBodega(

    titulo: String,

    descripcion: String,

    color: Color,

    ruta: String,

    onClick: () -> Unit

) {

    val icono = when (ruta) {

        "inventario" ->
            Icons.Default.Inventory

        "crearProducto" ->
            Icons.Default.AddBox

        "entradas" ->
            Icons.Default.Input

        "salidas" ->
            Icons.Default.Output

        "facturas" ->
            Icons.Default.Description

        "existencias" ->
            Icons.Default.Warning

        "presupuesto" ->
            Icons.Default.AttachMoney

        "stockBajo" ->
            Icons.Default.Warning

        else ->
            Icons.Default.Inventory
    }

    Card(

        modifier = Modifier
            .width(160.dp)
            .clickable {

                onClick()
            },

        shape =
            RoundedCornerShape(24.dp),

        colors =
            CardDefaults.cardColors(

                containerColor =
                    MaterialTheme.colorScheme.surface
            ),

        elevation =
            CardDefaults.cardElevation(

                defaultElevation = 4.dp
            )

    ) {

        Column(

            modifier =
                Modifier.padding(18.dp)

        ) {

            Box(

                modifier = Modifier
                    .size(64.dp)
                    .background(

                        color,

                        RoundedCornerShape(20.dp)
                    ),

                contentAlignment =
                    Alignment.Center

            ) {

                Icon(

                    imageVector = icono,

                    contentDescription = null,

                    tint = Color.White,

                    modifier =
                        Modifier.size(32.dp)
                )
            }

            Spacer(
                modifier =
                    Modifier.height(16.dp)
            )

            Text(

                text = titulo,

                style =
                    MaterialTheme.typography.titleSmall,

                fontWeight =
                    FontWeight.Bold,

                color =
                    MaterialTheme.colorScheme.onSurface
            )

            Spacer(
                modifier =
                    Modifier.height(6.dp)
            )

            Text(

                text = descripcion,

                style =
                    MaterialTheme.typography.bodySmall,

                color =
                    MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}