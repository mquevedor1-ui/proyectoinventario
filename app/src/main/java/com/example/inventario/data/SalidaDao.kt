package com.example.inventario.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

import kotlinx.coroutines.flow.Flow

@Dao
interface SalidaDao {

    // insertar

    @Insert(
        onConflict =
            OnConflictStrategy.REPLACE
    )
    suspend fun insert(

        salida: Salida

    ): Long

    // actualizar

    @Update
    suspend fun update(

        salida: Salida

    )

    // eliminar

    @Delete
    suspend fun delete(

        salida: Salida

    )

    // todas las salidas

    @Query(
        "SELECT * FROM salidas WHERE isDeleted = 0 ORDER BY fecha DESC"
    )
    fun getAllSalidas():
            Flow<List<Salida>>

    // por codigo

    @Query(
        "SELECT * FROM salidas WHERE codigo = :codigo AND isDeleted = 0"
    )
    fun getSalidasByProducto(

        codigo: String

    ): Flow<List<Salida>>

    // por bodega

    @Query(
        "SELECT * FROM salidas WHERE bodegaId = :bodegaId AND isDeleted = 0 ORDER BY fecha DESC"
    )
    fun getSalidasByBodega(

        bodegaId: String

    ): Flow<List<Salida>>

    @Query(
        "SELECT * FROM salidas WHERE bodegaId = :bodegaId AND isDeleted = 0 AND (codigo LIKE '%' || :query || '%' OR descripcion LIKE '%' || :query || '%' OR vehiculo LIKE '%' || :query || '%') ORDER BY fecha DESC"
    )
    fun buscarSalidas(bodegaId: String, query: String): Flow<List<Salida>>

    // contar

    @Query(
        "SELECT COUNT(*) FROM salidas"
    )
    suspend fun contarSalidas():
            Int

    @Query("SELECT * FROM salidas WHERE id = :id")
    suspend fun getSalidaById(id: Int): Salida?

    // eliminar todo

    @Query(
        "DELETE FROM salidas"
    )
    suspend fun deleteAll()

    // --- PAPELERA ---

    @Query("UPDATE salidas SET isDeleted = 1, deletionDate = :date WHERE id = :id")
    suspend fun softDelete(id: Int, date: Long)

    @Query("UPDATE salidas SET isDeleted = 0, deletionDate = NULL WHERE id = :id")
    suspend fun restore(id: Int)

    @Query("SELECT * FROM salidas WHERE isDeleted = 1 ORDER BY deletionDate DESC")
    fun getDeletedSalidas(): Flow<List<Salida>>

    @Query("DELETE FROM salidas WHERE isDeleted = 1 AND deletionDate <= :threshold")
    suspend fun permanentPurge(threshold: Long)

    @Query("DELETE FROM salidas WHERE id = :id")
    suspend fun deletePermanently(id: Int)
}