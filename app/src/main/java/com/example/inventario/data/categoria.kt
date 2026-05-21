package com.example.inventario.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "categorias")
data class categoria(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val nombre: String,

    val prefijo: String,

    val isDeleted: Boolean = false,

    val deletionDate: Long? = null
)