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
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entrada: Entrada)

    @Update
    suspend fun update(entrada: Entrada)

    @Delete
    suspend fun delete(entrada: Entrada)

    @Query("SELECT * FROM entradas ORDER BY fecha DESC")
    fun getAllEntradas(): Flow<List<Entrada>>

    @Query("SELECT * FROM entradas WHERE productoId = :productoId")
    fun getEntradasByProducto(productoId: Int): Flow<List<Entrada>>
}
