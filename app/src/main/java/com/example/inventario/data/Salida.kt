package com.example.inventario.data
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "salidas"
)

data class Salida(

    @PrimaryKey(
        autoGenerate = true
    )
    val id: Int = 0,

    // codigo producto

    val codigo: String = "",

    // descripcion

    val descripcion: String = "",

    // categoria

    val categoria: String = "",

    // cantidad

    val cantidad: Int = 0,

    // responsable

    val responsable: String = "",

    // destino

    val destino: String = "",

    // vehiculo

    val vehiculo: String = "",

    // costo unitario (al momento de la salida)
    val costoUnitario: Double = 0.0,

    // fecha

    val fecha: String = "",

    // notas

    val notas: String = "",

    // bodega

    val bodegaId: String = "",

    // Papelera
    val isDeleted: Boolean = false,
    val deletionDate: Long? = null
)