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
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(factura: Factura)

    @Update
    suspend fun update(factura: Factura)

    @Delete
    suspend fun delete(factura: Factura)

    @Query("SELECT * FROM facturas ORDER BY fecha DESC")
    fun getAllFacturas(): Flow<List<Factura>>
}
