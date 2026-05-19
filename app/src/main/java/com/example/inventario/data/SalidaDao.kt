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
        "SELECT * FROM salidas ORDER BY fecha DESC"
    )
    fun getAllSalidas():
            Flow<List<Salida>>

    // por codigo

    @Query(
        "SELECT * FROM salidas WHERE codigo = :codigo"
    )
    fun getSalidasByProducto(

        codigo: String

    ): Flow<List<Salida>>

    // por bodega

    @Query(
        "SELECT * FROM salidas WHERE bodegaId = :bodegaId ORDER BY fecha DESC"
    )
    fun getSalidasByBodega(

        bodegaId: String

    ): Flow<List<Salida>>

    // contar

    @Query(
        "SELECT COUNT(*) FROM salidas"
    )
    suspend fun contarSalidas():
            Int

    // eliminar todo

    @Query(
        "DELETE FROM salidas"
    )
    suspend fun deleteAll()
}