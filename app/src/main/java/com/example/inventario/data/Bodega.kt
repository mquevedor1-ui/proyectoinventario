package com.example.inventario.data

import androidx.room.Entity
import androidx.room.PrimaryKey

    @Entity(tableName = "bodegas")
    data class Bodega(
        @PrimaryKey val id: String,
        val nombre: String,
            )