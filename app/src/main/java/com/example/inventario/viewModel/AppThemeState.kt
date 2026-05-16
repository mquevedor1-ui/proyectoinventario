package com.example.inventario.viewModel




import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

object AppThemeState {

    private val _tema = MutableStateFlow("verde")

    val tema = _tema.asStateFlow()

    fun cambiarTema(nuevo: String) {

        _tema.value = nuevo
    }
}