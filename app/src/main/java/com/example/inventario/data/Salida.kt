package com.example.inventario.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "salidas")
data class Salida(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val productoId: Int,
    val cantidad: Int,
    val fecha: String,
    val destino: String = "",
    val notas: String = ""
)
