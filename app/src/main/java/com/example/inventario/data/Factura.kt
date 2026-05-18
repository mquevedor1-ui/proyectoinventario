package com.example.inventario.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "facturas")
data class Factura(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val numeroFactura: String,
    val fecha: String,
    val cliente: String,
    val total: Double,
    val notas: String = ""
)
