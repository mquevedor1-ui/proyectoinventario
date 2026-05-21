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
        "SELECT * FROM entradas WHERE isDeleted = 0 ORDER BY fecha DESC"
    )
    fun getAllEntradas():
            Flow<List<Entrada>>

    // obtener por codigo

    @Query(
        "SELECT * FROM entradas WHERE codigo = :codigo AND isDeleted = 0"
    )
    fun getEntradasByProducto(

        codigo: String

    ): Flow<List<Entrada>>

    // obtener por bodega

    @Query(
        "SELECT * FROM entradas WHERE bodegaId = :bodegaId AND isDeleted = 0 ORDER BY fecha DESC"
    )
    fun getEntradasByBodega(

        bodegaId: String

    ): Flow<List<Entrada>>

    @Query(
        "SELECT * FROM entradas WHERE bodegaId = :bodegaId AND isDeleted = 0 AND (codigo LIKE '%' || :query || '%' OR descripcion LIKE '%' || :query || '%') ORDER BY fecha DESC"
    )
    fun buscarEntradas(bodegaId: String, query: String): Flow<List<Entrada>>

    @Query("SELECT * FROM entradas WHERE id = :id")
    suspend fun getEntradaById(id: Int): Entrada?

    // eliminar todo

    @Query(
        "DELETE FROM entradas"
    )
    suspend fun deleteAll()

    // --- PAPELERA ---

    @Query("UPDATE entradas SET isDeleted = 1, deletionDate = :date WHERE id = :id")
    suspend fun softDelete(id: Int, date: Long)

    @Query("UPDATE entradas SET isDeleted = 0, deletionDate = NULL WHERE id = :id")
    suspend fun restore(id: Int)

    @Query("SELECT * FROM entradas WHERE isDeleted = 1 ORDER BY deletionDate DESC")
    fun getDeletedEntradas(): Flow<List<Entrada>>

    @Query("DELETE FROM entradas WHERE isDeleted = 1 AND deletionDate <= :threshold")
    suspend fun permanentPurge(threshold: Long)

    @Query("DELETE FROM entradas WHERE id = :id")
    suspend fun deletePermanently(id: Int)
}