package com.example.inventario.navigation
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel

import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

import com.example.inventario.ui.login.LoginScreen

import com.example.inventario.ui.menu.MenuPScreen
import com.example.inventario.ui.menu.BodegaMenuScreen
import com.example.inventario.ui.menu.CrearBodegaScreen


import com.example.inventario.ui.inventario.CrearProductoScreen
import com.example.inventario.ui.inventario.EditarProductoScreen

import com.example.inventario.ui.entradas.EntradasScreen
import com.example.inventario.ui.entradas.CrearEntradasScreen

import com.example.inventario.ui.Salidas.SalidasScreen
import com.example.inventario.ui.Salidas.CrearSalidasScreen

import com.example.inventario.ui.Facturas.FacturasScreen
import com.example.inventario.ui.Facturas.CrearFacturasScreen

import com.example.inventario.ui.config.ConfigScreen
import com.example.inventario.ui.config.UsuariosScreen
import com.example.inventario.ui.config.TemasScreen

import com.example.inventario.ui.CrearUsuarioScreen
import com.example.inventario.ui.inventario.InventarioScreen

@Composable
fun NavGraph() {

    val navController =
        rememberNavController()

    NavHost(

        navController = navController,

        startDestination = "login"

    ) {

        // login

        composable("login") {

            LoginScreen(

                navController = navController
            )
        }

        // menu principal

        composable("menuP") {

            MenuPScreen(

                navController = navController
            )
        }

        // crear bodega

        composable("crearBodega") {

            CrearBodegaScreen(

                navController = navController,

                viewModel = viewModel()
            )
        }

        // menu bodega

        composable(

            route =
                "menuBodega/{bodegaId}",

            arguments = listOf(

                navArgument("bodegaId") {

                    type = NavType.StringType
                }
            )

        ) { backStackEntry ->

            val bodegaId =

                backStackEntry
                    .arguments
                    ?.getString("bodegaId")
                    ?: ""

            BodegaMenuScreen(

                navController = navController,

                bodegaId = bodegaId
            )
        }

        // inventario

        composable(

            route =
                "inventario/{bodegaId}",

            arguments = listOf(

                navArgument("bodegaId") {

                    type = NavType.StringType
                }
            )

        ) { backStackEntry ->

            val bodegaId =

                backStackEntry
                    .arguments
                    ?.getString("bodegaId")
                    ?: ""

            InventarioScreen(

                navController = navController,

                bodegaId = bodegaId
            )
        }

        // crear producto

        composable(

            route =
                "crearProducto/{bodegaId}",

            arguments = listOf(

                navArgument("bodegaId") {

                    type = NavType.StringType
                }
            )

        ) { backStackEntry ->

            val bodegaId =

                backStackEntry
                    .arguments
                    ?.getString("bodegaId")
                    ?: ""

            CrearProductoScreen(

                navController = navController,

                bodegaId = bodegaId
            )
        }

        // editar producto

        composable(

            route =
                "editarProducto/{productoId}",

            arguments = listOf(

                navArgument("productoId") {

                    type = NavType.StringType
                }
            )

        ) { backStackEntry ->

            val productoId =

                backStackEntry
                    .arguments
                    ?.getString("productoId")
                    ?: ""

            EditarProductoScreen(

                navController = navController,

                productoId = productoId
            )
        }

        // entradas

        composable(

            route =
                "entradas/{bodegaId}",

            arguments = listOf(

                navArgument("bodegaId") {

                    type = NavType.StringType
                }
            )

        ) { backStackEntry ->

            val bodegaId =

                backStackEntry
                    .arguments
                    ?.getString("bodegaId")
                    ?: ""

            EntradasScreen(

                navController = navController,

                bodegaId = bodegaId
            )
        }

        // crear entrada

        composable(

            route =
                "crearEntrada/{bodegaId}",

            arguments = listOf(

                navArgument("bodegaId") {

                    type = NavType.StringType
                }
            )

        ) { backStackEntry ->

            val bodegaId =

                backStackEntry
                    .arguments
                    ?.getString("bodegaId")
                    ?: ""

            CrearEntradasScreen(

                navController = navController,

                bodegaId = bodegaId
            )
        }

        // salidas

        composable(

            route =
                "salidas/{bodegaId}",

            arguments = listOf(

                navArgument("bodegaId") {

                    type = NavType.StringType
                }
            )

        ) { backStackEntry ->

            val bodegaId =

                backStackEntry
                    .arguments
                    ?.getString("bodegaId")
                    ?: ""

            SalidasScreen(

                navController = navController,

                bodegaId = bodegaId
            )
        }

        // crear salida

        composable(

            route =
                "crearSalida/{bodegaId}",

            arguments = listOf(

                navArgument("bodegaId") {

                    type = NavType.StringType
                }
            )

        ) { backStackEntry ->

            val bodegaId =

                backStackEntry
                    .arguments
                    ?.getString("bodegaId")
                    ?: ""

            CrearSalidasScreen(

                navController = navController,

                bodegaId = bodegaId
            )
        }

        // facturas

        composable(

            route =
                "facturas/{bodegaId}",

            arguments = listOf(

                navArgument("bodegaId") {

                    type = NavType.StringType
                }
            )

        ) { backStackEntry ->

            val bodegaId =

                backStackEntry
                    .arguments
                    ?.getString("bodegaId")
                    ?: ""

            FacturasScreen(

                navController = navController,

                bodegaId = bodegaId
            )
        }

        // crear factura

        composable(

            route =
                "crearFactura/{bodegaId}",

            arguments = listOf(

                navArgument("bodegaId") {

                    type = NavType.StringType
                }
            )

        ) { backStackEntry ->

            val bodegaId =

                backStackEntry
                    .arguments
                    ?.getString("bodegaId")
                    ?: ""

            CrearFacturasScreen(

                navController = navController,

                bodegaId = bodegaId
            )
        }

        // configuracion

        composable("configuracion") {

            ConfigScreen(

                navController = navController,

                usuarioViewModel =
                    viewModel()
            )
        }

        // usuarios

        composable("usuarios") {

            UsuariosScreen(

                navController = navController,

                viewModel = viewModel()
            )
        }

        // temas

        composable("temas") {

            TemasScreen(

                navController = navController
            )
        }

        // crear usuario

        composable("crearUsuario") {

            CrearUsuarioScreen(

                navController = navController,

                viewModel = viewModel()
            )
        }
    }
}