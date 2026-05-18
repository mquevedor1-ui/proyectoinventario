package com.example.inventario.data
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

import kotlinx.coroutines.flow.Flow

@Dao
interface BodegaDao {

    // =========================
    // INSERTAR
    // =========================

    @Insert
    suspend fun insertar(
        bodega: Bodega
    ): Long

    // =========================
    // ACTUALIZAR
    // =========================

    @Update
    suspend fun actualizar(
        bodega: Bodega
    )

    // =========================
    // OBTENER TODAS
    // =========================

    @Query(
        "SELECT * FROM bodegas"
    )
    fun obtenerTodas():
            Flow<List<Bodega>>

    // =========================
    // OBTENER POR ID
    // =========================

    @Query(
        "SELECT * FROM bodegas WHERE id = :id"
    )
    suspend fun obtenerPorId(
        id: String
    ): Bodega?

    // =========================
    // ELIMINAR
    // =========================

    @Delete
    suspend fun eliminar(
        bodega: Bodega
    )
}