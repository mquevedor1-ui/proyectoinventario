package com.example.inventario.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import android.app.Application
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.inventario.ui.login.LoginScreen
import com.example.inventario.ui.menu.BodegaMenuScreen
import com.example.inventario.ui.menu.MainMenuScreen
import com.example.inventario.ui.CrearUsuarioScreen
import com.example.inventario.ui.config.ConfigScreen
import com.example.inventario.viewModel.BodegaViewModel
import com.example.inventario.viewModel.UsuarioViewModel


@Composable
fun NavGraph(modifier: Modifier = Modifier) {

    val navController = rememberNavController()


    val context = LocalContext.current

    val bodegaViewModel: BodegaViewModel = viewModel(
        factory = ViewModelProvider.AndroidViewModelFactory.getInstance(context.applicationContext as Application)
    )
    val usuarioViewModel: UsuarioViewModel = viewModel(
        factory = ViewModelProvider.AndroidViewModelFactory.getInstance(context.applicationContext as Application)
    )

    val esAdmin = true

    NavHost(
        navController = navController,
        startDestination = "login",
        modifier = modifier
    ) {

        // LOGIN
        composable("login") {
            LoginScreen(navController)
        }

        // MENU PRINCIPAL
        composable("menu") {
            MainMenuScreen(
                navController = navController,
                viewModel = bodegaViewModel,
                esAdmin = esAdmin
            )
        }

        // MENU BODEGA
        composable("bodegaMenu/{bodegaId}") { backStackEntry ->
            val id = backStackEntry.arguments?.getString("bodegaId") ?: ""

            BodegaMenuScreen(
                navController = navController,
                bodegaId = id
           )
        }

        // CONFIGURACION
        composable("configuracion") {
            ConfigScreen(navController, usuarioViewModel)
        }

        // CREAR USUARIO
        composable("crearUsuario") {
            CrearUsuarioScreen(navController, usuarioViewModel)
        }

        // --- DESTINOS DE BODEGA ---
        composable("inventario/{bodegaId}") { backStackEntry ->
            val bodegaId = backStackEntry.arguments?.getString("bodegaId") ?: ""
            PlaceholderScreen("Inventario - Bodega $bodegaId")
        }
        composable("crearProducto/{bodegaId}") { backStackEntry ->
            val bodegaId = backStackEntry.arguments?.getString("bodegaId") ?: ""
            PlaceholderScreen("Crear Producto - Bodega $bodegaId")
        }
        composable("entradas/{bodegaId}") { backStackEntry ->
            val bodegaId = backStackEntry.arguments?.getString("bodegaId") ?: ""
            PlaceholderScreen("Entradas - Bodega $bodegaId")
        }
        composable("salidas/{bodegaId}") { backStackEntry ->
            val bodegaId = backStackEntry.arguments?.getString("bodegaId") ?: ""
            PlaceholderScreen("Salidas - Bodega $bodegaId")
        }
        composable("facturas/{bodegaId}") { backStackEntry ->
            val bodegaId = backStackEntry.arguments?.getString("bodegaId") ?: ""
            PlaceholderScreen("Facturas - Bodega $bodegaId")
        }
        composable("existencias/{bodegaId}") { backStackEntry ->
            val bodegaId = backStackEntry.arguments?.getString("bodegaId") ?: ""
            PlaceholderScreen("Existencias - Bodega $bodegaId")
        }
        composable("presupuesto/{bodegaId}") { backStackEntry ->
            val bodegaId = backStackEntry.arguments?.getString("bodegaId") ?: ""
            PlaceholderScreen("Presupuesto - Bodega $bodegaId")
        }
        composable("stockBajo/{bodegaId}") { backStackEntry ->
            val bodegaId = backStackEntry.arguments?.getString("bodegaId") ?: ""
            PlaceholderScreen("Stock Bajo - Bodega $bodegaId")
        }
    }
}

@Composable
fun PlaceholderScreen(titulo: String) {
    androidx.compose.foundation.layout.Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = androidx.compose.ui.Alignment.Center
    ) {
        androidx.compose.material3.Text(text = titulo, style = androidx.compose.material3.MaterialTheme.typography.headlineMedium)
    }
}
