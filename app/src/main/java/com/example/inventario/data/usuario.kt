package com.example.inventario.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "usuario")
data class usuario(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val user: String = "",

    val pass: String = "",

    val rol: String = "",

    // Papelera
    val isDeleted: Boolean = false,
    val deletionDate: Long? = null
)