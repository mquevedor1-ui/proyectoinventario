package com.example.inventario.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

import kotlinx.coroutines.flow.Flow

@Dao
interface EntradaDao {

    // insertar

    @Insert(
        onConflict =
            OnConflictStrategy.REPLACE
    )
    suspend fun insert(

        entrada: Entrada

    ): Long

    // actualizar

    @Update
    suspend fun update(

        entrada: Entrada

    )

    // eliminar

    @Delete
    suspend fun delete(

        entrada: Entrada

    )

    // obtener todas

    @Query(
        "SELECT * FROM entradas ORDER BY fecha DESC"
    )
    fun getAllEntradas():
            Flow<List<Entrada>>

    // obtener por codigo

    @Query(
        "SELECT * FROM entradas WHERE codigo = :codigo"
    )
    fun getEntradasByProducto(

        codigo: String

    ): Flow<List<Entrada>>

    // obtener por bodega

    @Query(
        "SELECT * FROM entradas WHERE bodegaId = :bodegaId ORDER BY fecha DESC"
    )
    fun getEntradasByBodega(

        bodegaId: String

    ): Flow<List<Entrada>>

    // eliminar todo

    @Query(
        "DELETE FROM entradas"
    )
    suspend fun deleteAll()
}