package com.example.inventario.data
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

import kotlinx.coroutines.flow.Flow

@Dao
interface BodegaDao {

    // =========================
    // INSERTAR
    // =========================

    @Insert(onConflict = OnConflictStrategy.REPLACE)
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

    @Query("SELECT * FROM bodegas WHERE isDeleted = 0")
    fun obtenerTodas(): Flow<List<Bodega>>

    @Query("SELECT * FROM bodegas WHERE isDeleted = 1")
    fun obtenerPapelera(): Flow<List<Bodega>>

    @Query("DELETE FROM bodegas WHERE isDeleted = 1 AND deletionDate < :timestamp")
    suspend fun purgarAntiguos(timestamp: Long)

    @Query("DELETE FROM bodegas WHERE id = :id")
    suspend fun eliminarPermanente(id: String)

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