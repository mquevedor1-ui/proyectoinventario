package com.example.inventario.data
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "facturas"
)

data class Factura(

    @PrimaryKey(
        autoGenerate = true
    )
    val id: Int = 0,

    // numero factura

    val numeroFactura: String = "",

    // fecha

    val fecha: String = "",

    // proveedor

    val proveedor: String = "",

    // total factura

    val total: Double = 0.0,

    // codigo producto

    val codigo: String = "",

    // productos

    val productos: String = "",

    // notas

    val notas: String = "",

    // categoria

    val categoria: String = "",

    // bodega

    val bodegaId: String = "",

    // usuario

    val usuario: String = "",

    // Papelera
    val isDeleted: Boolean = false,
    val deletionDate: Long? = null
)