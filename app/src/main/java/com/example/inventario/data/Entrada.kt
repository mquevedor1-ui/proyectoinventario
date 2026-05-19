package com.example.inventario.data
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "entradas"
)

data class Entrada(

    @PrimaryKey(
        autoGenerate = true
    )
    val id: Int = 0,

    // codigo producto

    val codigo: String = "",

    // descripcion

    val descripcion: String = "",

    // cantidad

    val cantidad: Int = 0,

    // proveedor

    val proveedor: String = "",

    // categoria

    val categoria: String = "",

    // unidad

    val unidad: String = "",

    // ubicacion

    val ubicacion: String = "",

    // costo

    val costoUnitario: Double = 0.0,

    // fecha

    val fecha: String = "",

    // notas

    val notas: String = "",

    // bodega

    val bodegaId: String = ""
)