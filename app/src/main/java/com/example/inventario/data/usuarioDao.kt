package com.example.inventario.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

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
    @Query("SELECT * FROM usuario WHERE isDeleted = 0")
    suspend fun obtenerTodos(): List<usuario>

    @Query("SELECT * FROM usuario WHERE isDeleted = 1")
    fun obtenerPapelera(): Flow<List<usuario>>

    @Query("DELETE FROM usuario WHERE isDeleted = 1 AND deletionDate < :timestamp")
    suspend fun purgarAntiguos(timestamp: Long)

    @Query("DELETE FROM usuario WHERE user = :user")
    suspend fun eliminarPermanente(user: String)

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