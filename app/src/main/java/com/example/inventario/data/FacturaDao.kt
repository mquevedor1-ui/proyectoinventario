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
        "SELECT * FROM facturas WHERE isDeleted = 0 ORDER BY fecha DESC"
    )
    fun getAllFacturas():
            Flow<List<Factura>>

    // obtener por bodega
    @Query(
        "SELECT * FROM facturas WHERE bodegaId = :bodegaId AND isDeleted = 0 ORDER BY fecha DESC"
    )
    fun getFacturasByBodega(

        bodegaId: String

    ): Flow<List<Factura>>

    @Query(
        "SELECT * FROM facturas WHERE bodegaId = :bodegaId AND isDeleted = 0 AND (numeroFactura LIKE '%' || :query || '%' OR proveedor LIKE '%' || :query || '%') ORDER BY fecha DESC"
    )
    fun buscarFacturas(bodegaId: String, query: String): Flow<List<Factura>>

    @Query("SELECT * FROM facturas WHERE id = :id")
    suspend fun getFacturaById(id: Int): Factura?

    // eliminar todo
    @Query(
        "DELETE FROM facturas"
    )
    suspend fun deleteAll()

    // --- PAPELERA ---

    @Query("UPDATE facturas SET isDeleted = 1, deletionDate = :date WHERE id = :id")
    suspend fun softDelete(id: Int, date: Long)

    @Query("UPDATE facturas SET isDeleted = 0, deletionDate = NULL WHERE id = :id")
    suspend fun restore(id: Int)

    @Query("SELECT * FROM facturas WHERE isDeleted = 1 ORDER BY deletionDate DESC")
    fun getDeletedFacturas(): Flow<List<Factura>>

    @Query("DELETE FROM facturas WHERE isDeleted = 1 AND deletionDate <= :threshold")
    suspend fun permanentPurge(threshold: Long)

    @Query("DELETE FROM facturas WHERE id = :id")
    suspend fun deletePermanently(id: Int)
}