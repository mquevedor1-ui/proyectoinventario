package com.example.inventario.data
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

import kotlinx.coroutines.flow.Flow

@Dao
interface productoDao {

 // insertar

 @Insert(

  onConflict =
   OnConflictStrategy.REPLACE
 )
 suspend fun insertar(

  producto: producto

 ): Long

 // actualizar

 @Update
 suspend fun actualizar(

  producto: producto

 )

 // eliminar

 @Delete
 suspend fun eliminar(

  producto: producto

 )

 // obtener productos

 @Query(

  "SELECT * FROM productos WHERE bodegaId = :bodegaId ORDER BY descripcion ASC"
 )
 fun obtenerProductos(

  bodegaId: String

 ): Flow<List<producto>>

 // obtener producto por codigo

 @Query(

  "SELECT * FROM productos WHERE codigo = :codigo LIMIT 1"
 )
 suspend fun obtenerProductoPorCodigo(

  codigo: String

 ): producto?

 // obtener producto por id

 @Query(

  "SELECT * FROM productos WHERE id = :id LIMIT 1"
 )
 suspend fun obtenerProductoPorId(

  id: Int

 ): producto?

 // obtener ultimo codigo

 @Query(

  "SELECT codigo FROM productos WHERE prefijoCategoria = :prefijo ORDER BY codigo DESC LIMIT 1"
 )
 suspend fun obtenerUltimoCodigoPorPrefijo(

  prefijo: String

 ): String?

 // eliminar todo

 @Query(

  "DELETE FROM productos"
 )
 suspend fun eliminarTodo()
}