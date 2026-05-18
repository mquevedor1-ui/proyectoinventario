package com.example.inventario.navigation

import android.app.Application

import androidx.compose.foundation.layout.fillMaxSize

import androidx.compose.runtime.Composable

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel

import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

import com.example.inventario.ui.CrearUsuarioScreen
import com.example.inventario.ui.config.ConfigScreen
import com.example.inventario.ui.config.TemasScreen
import com.example.inventario.ui.config.UsuariosScreen
import com.example.inventario.ui.inventario.CrearProductoScreen
import com.example.inventario.ui.inventario.EditarProductoScreen
import com.example.inventario.ui.inventario.InventarioGeneralScreen
import com.example.inventario.ui.login.LoginScreen
import com.example.inventario.ui.menu.BodegaMenuScreen
import com.example.inventario.ui.menu.MainMenuScreen

import com.example.inventario.viewModel.BodegaViewModel
import com.example.inventario.viewModel.UsuarioViewModel

@Composable
fun NavGraph(

    modifier: Modifier = Modifier

) {

    // nav
    val navController =
        rememberNavController()

    // context
    val context =
        LocalContext.current

    // vm bodega
    val bodegaViewModel:
            BodegaViewModel = viewModel(

        factory =
            ViewModelProvider
                .AndroidViewModelFactory
                .getInstance(

                    context.applicationContext
                            as Application
                )
    )

    // vm usuario
    val usuarioViewModel:
            UsuarioViewModel = viewModel(

        factory =
            ViewModelProvider
                .AndroidViewModelFactory
                .getInstance(

                    context.applicationContext
                            as Application
                )
    )

    // navhost
    NavHost(

        navController = navController,

        startDestination = "login",

        modifier = modifier

    ) {

        // login
        composable("login") {

            LoginScreen(

                navController =
                    navController
            )
        }

        // menu
        composable("menu") {

            MainMenuScreen(

                navController =
                    navController,

                viewModel =
                    bodegaViewModel
            )
        }

        // menu bodega
        composable(

            "bodegaMenu/{bodegaId}"

        ) { backStackEntry ->

            val id =
                backStackEntry
                    .arguments
                    ?.getString(
                        "bodegaId"
                    ) ?: ""

            BodegaMenuScreen(

                navController =
                    navController,

                bodegaId = id
            )
        }

        // config
        composable("configuracion") {

            ConfigScreen(

                navController =
                    navController,

                usuarioViewModel =
                    usuarioViewModel
            )
        }

        // crear usuario
        composable("crearUsuario") {
            CrearUsuarioScreen(
                navController = navController,
                viewModel = usuarioViewModel
            )
        }

        // lista de usuarios (AÑADIDO)
        composable("usuarios") {
            UsuariosScreen(
                navController = navController,
                viewModel = usuarioViewModel
            )
        }

        // temas
        composable("temas") {
            TemasScreen(navController = navController)
        }

        // inventario
        composable(

            "inventario/{bodegaId}"

        ) { backStackEntry ->

            val bodegaId =
                backStackEntry
                    .arguments
                    ?.getString(
                        "bodegaId"
                    ) ?: ""

            InventarioGeneralScreen(

                navController =
                    navController,

                bodegaId =
                    bodegaId
            )
        }

        // crear producto
        composable(

            "crearProducto/{bodegaId}"

        ) { backStackEntry ->

            val bodegaId =
                backStackEntry
                    .arguments
                    ?.getString(
                        "bodegaId"
                    ) ?: ""

            CrearProductoScreen(

                navController =
                    navController,

                bodegaId =
                    bodegaId
            )
        }

        // editar producto
        composable(

            "editarProducto/{productoId}"

        ) { backStackEntry ->

            val productoId =
                backStackEntry
                    .arguments
                    ?.getString(
                        "productoId"
                    )
                    ?.toIntOrNull() ?: 0

            EditarProductoScreen(

                navController =
                    navController,

                productoId =
                    productoId
            )
        }

        // entradas
        composable(

            "entradas/{bodegaId}"

        ) { backStackEntry ->

            val bodegaId =
                backStackEntry
                    .arguments
                    ?.getString(
                        "bodegaId"
                    ) ?: ""

            PlaceholderScreen(

                "Entradas - $bodegaId"
            )
        }

        // salidas
        composable(

            "salidas/{bodegaId}"

        ) { backStackEntry ->

            val bodegaId =
                backStackEntry
                    .arguments
                    ?.getString(
                        "bodegaId"
                    ) ?: ""

            PlaceholderScreen(

                "Salidas - $bodegaId"
            )
        }

        // facturas
        composable(

            "facturas/{bodegaId}"

        ) { backStackEntry ->

            val bodegaId =
                backStackEntry
                    .arguments
                    ?.getString(
                        "bodegaId"
                    ) ?: ""

            PlaceholderScreen(

                "Facturas - $bodegaId"
            )
        }

        // existencias
        composable(

            "existencias/{bodegaId}"

        ) { backStackEntry ->

            val bodegaId =
                backStackEntry
                    .arguments
                    ?.getString(
                        "bodegaId"
                    ) ?: ""

            PlaceholderScreen(

                "Existencias - $bodegaId"
            )
        }

        // presupuesto
        composable(

            "presupuesto/{bodegaId}"

        ) { backStackEntry ->

            val bodegaId =
                backStackEntry
                    .arguments
                    ?.getString(
                        "bodegaId"
                    ) ?: ""

            PlaceholderScreen(

                "Presupuesto - $bodegaId"
            )
        }

        // stock
        composable(

            "stockBajo/{bodegaId}"

        ) { backStackEntry ->

            val bodegaId =
                backStackEntry
                    .arguments
                    ?.getString(
                        "bodegaId"
                    ) ?: ""

            PlaceholderScreen(

                "Stock Bajo - $bodegaId"
            )
        }
    }
}

@Composable
fun PlaceholderScreen(

    titulo: String

) {

    androidx.compose.foundation.layout.Box(

        modifier = Modifier.fillMaxSize(),

        contentAlignment =
            Alignment.Center

    ) {

        androidx.compose.material3.Text(

            text = titulo,

            style =
                androidx.compose.material3
                    .MaterialTheme
                    .typography
                    .headlineMedium
        )
    }
}