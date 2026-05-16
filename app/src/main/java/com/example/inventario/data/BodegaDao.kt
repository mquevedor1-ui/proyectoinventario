package com.example.inventario.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface BodegaDao {

    @Insert
    suspend fun insertar(bodega: Bodega): Long

    @androidx.room.Update
    suspend fun actualizar(bodega: Bodega)

    // 🔥 CAMBIO AQUÍ
    @Query("SELECT * FROM bodegas")
    fun obtenerTodas(): Flow<List<Bodega>>

    @Query("SELECT * FROM bodegas WHERE id = :id")
    suspend fun obtenerPorId(id: String): Bodega?

    @Delete
    suspend fun eliminar(bodegas: Bodega)
}