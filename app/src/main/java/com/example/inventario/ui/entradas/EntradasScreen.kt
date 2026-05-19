package com.example.inventario.ui.entradas


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.inventario.ui.AppTopBar
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavController
import com.example.inventario.data.Entrada
import com.example.inventario.viewModel.EntradaViewModel
import com.example.inventario.viewModel.ProductoViewModel
import com.example.inventario.viewModel.SessionManager

@OptIn(ExperimentalMaterial3Api::class)

@Composable
fun EntradasScreen(

    navController: NavController,

    bodegaId: String

) {

    val context = LocalContext.current

    val entradaViewModel:
            EntradaViewModel = viewModel()

    val productoViewModel:
            ProductoViewModel = viewModel()

    val entradas by entradaViewModel
        .allEntradas
        .collectAsState(
            initial = emptyList()
        )

    LaunchedEffect(Unit) {

        entradaViewModel
            .sincronizarDesdeFirebase()
    }

    Scaffold(

        containerColor =
            MaterialTheme.colorScheme.background,

        topBar = {

            AppTopBar(

                titulo =
                    "Reporte de Entradas",

                subtitulo =
                    "Bodega: $bodegaId",

                navController =
                    navController
            )
        },

        floatingActionButton = {

            if (

                SessionManager.esAdmin()
                ||
                SessionManager.rolUsuario()
                ==
                "encargado"

            ) {

                FloatingActionButton(

                    onClick = {

                        navController.navigate(

                            "crearEntrada/$bodegaId"
                        )
                    },

                    containerColor =
                        MaterialTheme.colorScheme.primary,

                    contentColor =
                        MaterialTheme.colorScheme.onPrimary

                ) {

                    Row(

                        modifier =
                            Modifier.padding(
                                horizontal = 12.dp
                            ),

                        verticalAlignment =
                            Alignment.CenterVertically

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
                            "Registrar"
                        )
                    }
                }
            }
        }

    ) { padding ->

        Column(

            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)

        ) {

            // tabs

            Row(

                modifier = Modifier
                    .fillMaxWidth()
                    .background(

                        MaterialTheme
                            .colorScheme
                            .surfaceVariant,

                        RoundedCornerShape(20.dp)
                    )
                    .padding(4.dp),

                horizontalArrangement =
                    Arrangement.SpaceBetween

            ) {

                Text(

                    text = "Por Día",

                    modifier = Modifier
                        .weight(1f)
                        .background(

                            MaterialTheme
                                .colorScheme
                                .surface,

                            RoundedCornerShape(
                                20.dp
                            )
                        )
                        .padding(10.dp),

                    textAlign =
                        TextAlign.Center,

                    fontWeight =
                        FontWeight.Bold
                )

                Text(

                    text = "Por Semana",

                    modifier = Modifier
                        .weight(1f)
                        .padding(10.dp),

                    textAlign =
                        TextAlign.Center
                )

                Text(

                    text = "Por Mes",

                    modifier = Modifier
                        .weight(1f)
                        .padding(10.dp),

                    textAlign =
                        TextAlign.Center
                )
            }

            Spacer(
                modifier = Modifier.height(16.dp)
            )

            // exportar

            Button(

                onClick = {

                    exportarEntradasPDF(

                        context,

                        entradas
                    )
                },

                modifier =
                    Modifier.fillMaxWidth(),

                colors =
                    ButtonDefaults.buttonColors(

                        containerColor =
                            MaterialTheme
                                .colorScheme
                                .primary
                    )
            ) {

                Icon(

                    Icons.Default.PictureAsPdf,

                    contentDescription =
                        null
                )

                Spacer(
                    modifier =
                        Modifier.width(8.dp)
                )

                Text(
                    "Exportar PDF"
                )
            }

            Spacer(
                modifier = Modifier.height(16.dp)
            )

            // tabla

            Card(

                modifier =
                    Modifier.fillMaxWidth(),

                shape =
                    RoundedCornerShape(18.dp),

                colors =
                    CardDefaults.cardColors(

                        containerColor =
                            MaterialTheme
                                .colorScheme
                                .surface
                    )
            ) {

                Column(

                    modifier =
                        Modifier.padding(16.dp)

                ) {

                    // encabezados

                    Row(

                        modifier =
                            Modifier.fillMaxWidth(),

                        horizontalArrangement =
                            Arrangement.SpaceBetween

                    ) {

                        Text(

                            "Fecha",

                            modifier =
                                Modifier.weight(1f),

                            fontWeight =
                                FontWeight.Bold
                        )

                        Text(

                            "Código",

                            modifier =
                                Modifier.weight(1f),

                            fontWeight =
                                FontWeight.Bold
                        )

                        Text(

                            "Descripción",

                            modifier =
                                Modifier.weight(1.5f),

                            fontWeight =
                                FontWeight.Bold
                        )

                        Text(

                            "Cant.",

                            modifier =
                                Modifier.weight(0.8f),

                            fontWeight =
                                FontWeight.Bold,

                            textAlign =
                                TextAlign.End
                        )

                        if (

                            SessionManager.esAdmin()

                        ) {

                            Spacer(

                                modifier =
                                    Modifier.width(50.dp)
                            )
                        }
                    }

                    HorizontalDivider(

                        modifier =
                            Modifier.padding(
                                vertical = 12.dp
                            )
                    )

                    // vacio

                    if (

                        entradas.isEmpty()

                    ) {

                        Text(

                            text =
                                "No hay entradas registradas",

                            modifier = Modifier
                                .padding(30.dp)
                                .align(
                                    Alignment.CenterHorizontally
                                ),

                            color =
                                MaterialTheme
                                    .colorScheme
                                    .onSurfaceVariant
                        )
                    }

                    // lista

                    LazyColumn(

                        verticalArrangement =
                            Arrangement.spacedBy(10.dp)

                    ) {

                        items(entradas) { entrada ->

                            EntradaRow(

                                entrada = entrada,

                                viewModel =
                                    entradaViewModel,

                                productoViewModel =
                                    productoViewModel
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable


fun EntradaRow(

    entrada: Entrada,

    viewModel: EntradaViewModel,

    productoViewModel: ProductoViewModel

) {

    var productoNombre by remember {

        mutableStateOf("...")
    }

    LaunchedEffect(

        entrada.codigo

    ) {

        val producto =

            productoViewModel
                .obtenerProductoPorCodigo(

                    entrada.codigo
                )

        productoNombre =
            producto?.descripcion ?: "N/A"
    }

    Row(

        modifier =
            Modifier.fillMaxWidth(),

        verticalAlignment =
            Alignment.CenterVertically

    ) {

        Text(

            entrada.fecha,

            modifier =
                Modifier.weight(1f),

            fontSize = 12.sp
        )

        Text(

            entrada.codigo,

            modifier =
                Modifier.weight(1f),

            fontSize = 12.sp
        )

        Text(

            productoNombre,

            modifier =
                Modifier.weight(1.5f),

            fontSize = 12.sp,

            maxLines = 1
        )

        Text(

            "${entrada.cantidad}",

            modifier =
                Modifier.weight(0.8f),

            textAlign =
                TextAlign.End,

            fontWeight =
                FontWeight.Bold
        )

        if (

            SessionManager.esAdmin()

        ) {

            IconButton(

                onClick = {

                    viewModel
                        .eliminarEntrada(
                            entrada
                        )
                }

            ) {

                Icon(

                    Icons.Default.Delete,

                    contentDescription =
                        "Eliminar",

                    tint =
                        MaterialTheme
                            .colorScheme
                            .error
                )
            }
        }
    }
}