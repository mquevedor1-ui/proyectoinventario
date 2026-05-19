package com.example.inventario

import android.os.Bundle

import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge

import androidx.compose.foundation.layout.fillMaxSize

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue

import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

import com.example.inventario.navigation.NavGraph

import com.example.inventario.ui.theme.TemaAzul
import com.example.inventario.ui.theme.TemaMorado
import com.example.inventario.ui.theme.TemaNaranja
import com.example.inventario.ui.theme.TemaOscuro
import com.example.inventario.ui.theme.TemaVerde

import com.example.inventario.viewModel.AppThemeState

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {

            // tema actual

            val temaActual by
            AppThemeState.tema.collectAsState()

            // seleccionar tema

            val tema = when (temaActual) {

                "azul" -> TemaAzul

                "oscuro" -> TemaOscuro

                "naranja" -> TemaNaranja

                "morado" -> TemaMorado

                else -> TemaVerde
            }

            // colores

            val colores = if (
                temaActual == "oscuro"
            ) {

                darkColorScheme(

                    primary = tema.principal,

                    background = tema.fondo,

                    surface = tema.tarjeta,

                    onBackground = tema.texto,

                    onSurface = tema.texto,

                    onPrimary = Color.White
                )

            } else {

                lightColorScheme(

                    primary = tema.principal,

                    background = tema.fondo,

                    surface = tema.tarjeta,

                    onBackground = tema.texto,

                    onSurface = tema.texto,

                    onPrimary = Color.White
                )
            }

            // tema global

            MaterialTheme(

                colorScheme = colores

            ) {

                Surface(

                    modifier = Modifier.fillMaxSize(),

                    color =
                        MaterialTheme
                            .colorScheme
                            .background

                ) {

                    Scaffold(

                        modifier =
                            Modifier.fillMaxSize(),

                        containerColor =
                            MaterialTheme
                                .colorScheme
                                .background

                    ) {

                        NavGraph()
                    }
                }
            }
        }
    }
}
