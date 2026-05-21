package com.example.inventario.data
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

import kotlinx.coroutines.flow.Flow

@Dao
interface categoriaDao {

    // insertar
    @Insert(
        onConflict =
            OnConflictStrategy.REPLACE
    )
    suspend fun insertar(
        categoria: categoria
    ): Long

    // actualizar
    @Update
    suspend fun actualizar(

        categoria: categoria
    )

    // eliminar
    @Delete
    suspend fun eliminar(

        categoria: categoria
    )

    // obtener categorias
    @Query(
        "SELECT * FROM categorias WHERE isDeleted = 0 ORDER BY nombre ASC"
    )
    fun obtenerCategorias():
            Flow<List<categoria>>

    // obtener categoria por id
    @Query(
        "SELECT * FROM categorias WHERE id = :id AND isDeleted = 0"
    )
    suspend fun obtenerCategoriaPorId(

        id: Int

    ): categoria?

    // buscar categoria
    @Query(
        "SELECT * FROM categorias WHERE nombre = :nombre AND isDeleted = 0 LIMIT 1"
    )
    suspend fun buscarCategoria(

        nombre: String

    ): categoria?

    @Query("SELECT * FROM categorias WHERE prefijo = :prefijo AND isDeleted = 0 LIMIT 1")
    suspend fun buscarPorPrefijo(prefijo: String): categoria?

    // --- PAPELERA ---

    @Query("UPDATE categorias SET isDeleted = 1, deletionDate = :date WHERE id = :id")
    suspend fun softDelete(id: Int, date: Long)

    @Query("UPDATE categorias SET isDeleted = 0, deletionDate = NULL WHERE id = :id")
    suspend fun restore(id: Int)

    @Query("SELECT * FROM categorias WHERE isDeleted = 1 ORDER BY deletionDate DESC")
    fun getDeletedCategorias(): Flow<List<categoria>>

    @Query("DELETE FROM categorias WHERE isDeleted = 1 AND deletionDate <= :threshold")
    suspend fun permanentPurge(threshold: Long)

    @Query("DELETE FROM categorias WHERE id = :id")
    suspend fun deletePermanently(id: Int)
}