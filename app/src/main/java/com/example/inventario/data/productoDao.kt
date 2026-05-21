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

  "SELECT * FROM productos WHERE bodegaId = :bodegaId AND isDeleted = 0 ORDER BY descripcion ASC"
 )
 fun obtenerProductos(

  bodegaId: String

 ): Flow<List<producto>>

 // obtener producto por codigo

 @Query(

  "SELECT * FROM productos WHERE codigo = :codigo AND isDeleted = 0 LIMIT 1"
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

 @Query("SELECT * FROM productos WHERE cantidad <= stockMinimo AND stockMinimo > 0 AND isDeleted = 0")
 fun obtenerProductosBajoStock(): Flow<List<producto>>

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

 // --- PAPELERA ---

 @Query("UPDATE productos SET isDeleted = 1, deletionDate = :date WHERE id = :id")
 suspend fun softDelete(id: Int, date: Long)

 @Query("UPDATE productos SET isDeleted = 0, deletionDate = NULL WHERE id = :id")
 suspend fun restore(id: Int)

 @Query("SELECT * FROM productos WHERE isDeleted = 1 ORDER BY deletionDate DESC")
 fun getDeletedProductos(): Flow<List<producto>>

 @Query("DELETE FROM productos WHERE isDeleted = 1 AND deletionDate <= :threshold")
 suspend fun permanentPurge(threshold: Long)

 @Query("DELETE FROM productos WHERE id = :id")
 suspend fun deletePermanently(id: Int)
}