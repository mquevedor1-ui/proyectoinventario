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
        "SELECT * FROM categorias ORDER BY nombre ASC"
    )
    fun obtenerCategorias():
            Flow<List<categoria>>

    // obtener categoria por id
    @Query(
        "SELECT * FROM categorias WHERE id = :id"
    )
    suspend fun obtenerCategoriaPorId(

        id: Int

    ): categoria?

    // buscar categoria
    @Query(
        "SELECT * FROM categorias WHERE nombre = :nombre LIMIT 1"
    )
    suspend fun buscarCategoria(

        nombre: String

    ): categoria?
}