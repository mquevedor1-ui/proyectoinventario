package com.example.inventario.ui.theme


import androidx.compose.ui.graphics.Color

data class AppColors(
    val fondo: Color,
    val tarjeta: Color,
    val principal: Color,
    val texto: Color
)

val TemaVerde = AppColors(
    fondo = Color(0xFFE8F5E9),
    tarjeta = Color.White,
    principal = Color(0xFF2E7D32),
    texto = Color.Black
)

val TemaAzul = AppColors(
    fondo = Color(0xFFE3F2FD),
    tarjeta = Color.White,
    principal = Color(0xFF1565C0),
    texto = Color.Black
)

val TemaOscuro = AppColors(
    fondo = Color(0xFF121212),
    tarjeta = Color(0xFF1E1E1E),
    principal = Color(0xFF00C853),
    texto = Color.White
)

val TemaNaranja = AppColors(
    fondo = Color(0xFFFFF3E0),
    tarjeta = Color.White,
    principal = Color(0xFFE65100),
    texto = Color.Black
)

val TemaMorado = AppColors(
    fondo = Color(0xFFF3E5F5),
    tarjeta = Color.White,
    principal = Color(0xFF6A1B9A),
    texto = Color.Black
)
