package com.example.inventario.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "productos"
)
data class producto(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    // bodega
    val bodegaId: String = "",

    // codigo automatico
    val codigo: String = "",

    // nombre
    val descripcion: String = "",

    // categoria
    val categoria: String = "",

    // prefijo categoria
    val prefijoCategoria: String = "",

    // cantidad
    val cantidad: Int = 0,

    // presupuesto
    val presupuesto: Double = 0.0,

    // alerta de stock bajo
    val stockBajo: Boolean = false,

    // unidad
    val unidad: String = "",

    // ubicacion
    val ubicacion: String = "",

    // proveedor
    val proveedor: String = "",

    // costo
    val costo: Double = 0.0,

    // stock minimo
    val stockMinimo: Int = 0,

    // fecha ingreso
    val fechaIngreso: String = "",

    // notas
    val notas: String = "",

    // papelera
    val isDeleted: Boolean = false,

    val deletionDate: Long? = null
)