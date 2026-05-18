package com.example.inventario.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "entradas")
data class Entrada(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val productoId: Int,
    val cantidad: Int,
    val fecha: String,
    val notas: String = ""
)
