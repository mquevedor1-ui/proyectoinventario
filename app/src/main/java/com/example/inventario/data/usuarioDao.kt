package com.example.inventario.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface usuarioDao {

    // insertar
    @Insert(
        onConflict =
            OnConflictStrategy.REPLACE
    )
    suspend fun insertar(

        usuario: usuario

    ): Long

    // login
    @Query(
        "SELECT * FROM usuario WHERE user = :user AND pass = :pass"
    )
    suspend fun login(

        user: String,

        pass: String

    ): usuario?

    // existe
    @Query(
        "SELECT * FROM usuario WHERE user = :user LIMIT 1"
    )
    suspend fun existe(

        user: String

    ): usuario?

    // lista
    @Query(
        "SELECT * FROM usuario"
    )
    suspend fun obtenerTodos():
            List<usuario>

    // actualizar
    @Update
    suspend fun actualizar(

        usuario: usuario

    )

    // eliminar
    @Delete
    suspend fun eliminar(

        usuario: usuario

    )
}