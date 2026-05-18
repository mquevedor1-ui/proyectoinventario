package com.example.inventario.viewModel



import com.example.inventario.data.usuario

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

object SessionManager {

    // usuario actual
    private val _usuarioActual =

        MutableStateFlow<usuario?>(null)

    // state
    val usuarioActual:
            StateFlow<usuario?> =

        _usuarioActual.asStateFlow()

    // login
    fun login(

        usuario: usuario

    ) {

        _usuarioActual.value = usuario
    }

    // logout
    fun logout() {

        _usuarioActual.value = null
    }

    // cerrar sesion
    fun cerrarSesion() {

        logout()
    }

    // admin
    fun esAdmin(): Boolean {

        return _usuarioActual
            .value
            ?.rol == "admin"
    }

    // usuario
    fun nombreUsuario(): String {

        return _usuarioActual
            .value
            ?.user ?: ""
    }

    // rol
    fun rolUsuario(): String {

        return _usuarioActual
            .value
            ?.rol ?: ""
    }

    // sesion
    fun haySesion(): Boolean {

        return _usuarioActual
            .value != null
    }
}