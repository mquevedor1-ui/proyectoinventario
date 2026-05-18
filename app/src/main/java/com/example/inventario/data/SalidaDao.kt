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
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(salida: Salida)

    @Update
    suspend fun update(salida: Salida)

    @Delete
    suspend fun delete(salida: Salida)

    @Query("SELECT * FROM salidas ORDER BY fecha DESC")
    fun getAllSalidas(): Flow<List<Salida>>

    @Query("SELECT * FROM salidas WHERE productoId = :productoId")
    fun getSalidasByProducto(productoId: Int): Flow<List<Salida>>
}
