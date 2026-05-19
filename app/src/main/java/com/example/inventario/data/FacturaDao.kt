package com.example.inventario.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

import kotlinx.coroutines.flow.Flow

@Dao
interface FacturaDao {

    // insertar
    @Insert(
        onConflict =
            OnConflictStrategy.REPLACE
    )
    suspend fun insert(

        factura: Factura

    ): Long

    // actualizar
    @Update
    suspend fun update(

        factura: Factura

    )

    // eliminar
    @Delete
    suspend fun delete(

        factura: Factura

    )

    // obtener todas
    @Query(
        "SELECT * FROM facturas ORDER BY fecha DESC"
    )
    fun getAllFacturas():
            Flow<List<Factura>>

    // obtener por bodega
    @Query(
        "SELECT * FROM facturas WHERE bodegaId = :bodegaId ORDER BY fecha DESC"
    )
    fun getFacturasByBodega(

        bodegaId: String

    ): Flow<List<Factura>>

    // eliminar todo
    @Query(
        "DELETE FROM facturas"
    )
    suspend fun deleteAll()
}