package com.example.inventario.ui.menu

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size

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

import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import com.example.inventario.data.Bodega

@Composable
fun CardBodega(

    bodega: Bodega,

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

        Column(

            modifier = Modifier.padding(16.dp),

            horizontalAlignment =
                Alignment.CenterHorizontally

        ) {

            // =========================
            // ICONO
            // =========================

            Box(

                modifier = Modifier
                    .size(60.dp)
                    .background(

                        MaterialTheme.colorScheme.primary,

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
                modifier = Modifier.height(10.dp)
            )

            // =========================
            // TITULO
            // =========================

            Text(

                text = bodega.nombre,

                fontWeight = FontWeight.Bold,

                color =
                    MaterialTheme.colorScheme.onSurface
            )

            // =========================
            // DESCRIPCION
            // =========================

            Text(

                text = "Click para acceder",

                fontSize = 12.sp,

                color =
                    MaterialTheme.colorScheme.onSurface
            )
        }
    }
}