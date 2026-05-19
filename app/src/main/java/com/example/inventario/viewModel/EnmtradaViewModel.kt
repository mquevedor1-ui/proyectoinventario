package com.example.inventario.viewModel
import android.app.Application

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope

import com.example.inventario.data.Entrada
import com.example.inventario.data.FirebaseRepository
import com.example.inventario.data.appdatabase
import com.example.inventario.data.producto

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class EntradaViewModel(

    application: Application

) : AndroidViewModel(application) {

    // database

    private val db =
        appdatabase.getDatabase(application)

    // dao

    private val dao =
        db.entradaDao()

    private val productoDao =
        db.productoDao()

    // firebase

    private val firebaseRepo =
        FirebaseRepository()

    // lista

    val allEntradas:
            Flow<List<Entrada>> =
        dao.getAllEntradas()

    // sincronizar firebase

    fun sincronizarDesdeFirebase() {

        viewModelScope.launch(

            Dispatchers.IO

        ) {

            try {

                val entradasNube =

                    firebaseRepo
                        .obtenerEntradas()

                entradasNube.forEach { entrada ->

                    dao.insert(entrada)
                }

            } catch (e: Exception) {

                e.printStackTrace()
            }
        }
    }

    // agregar entrada

    fun agregarEntrada(

        entrada: Entrada

    ) {

        viewModelScope.launch(

            Dispatchers.IO

        ) {

            try {

                // guardar entrada

                val idGenerado =

                    dao.insert(
                        entrada
                    )

                val entradaConId =

                    entrada.copy(

                        id =
                            idGenerado.toInt()
                    )

                // buscar producto

                val producto =

                    productoDao
                        .obtenerProductoPorCodigo(

                            entrada.codigo
                        )

                // actualizar stock

                if (producto != null) {

                    val nuevaCantidad =

                        producto.cantidad +
                                entrada.cantidad

                    val productoActualizado =

                        producto.copy(

                            cantidad =
                                nuevaCantidad,

                            proveedor =
                                entrada.proveedor,

                            costo =
                                entrada.costoUnitario
                        )

                    // actualizar producto

                    productoDao.actualizar(
                        productoActualizado
                    )

                    // firebase producto

                    firebaseRepo
                        .guardarProducto(
                            productoActualizado
                        )
                }

                // firebase entrada

                firebaseRepo
                    .guardarEntrada(
                        entradaConId
                    )

            } catch (e: Exception) {

                e.printStackTrace()
            }
        }
    }

    // buscar producto

    suspend fun buscarProductoPorCodigo(

        codigo: String

    ): producto? {

        return productoDao
            .obtenerProductoPorCodigo(
                codigo
            )
    }

    // eliminar entrada

    fun eliminarEntrada(

        entrada: Entrada

    ) {

        viewModelScope.launch(

            Dispatchers.IO

        ) {

            try {

                // buscar producto

                val producto =

                    productoDao
                        .obtenerProductoPorCodigo(

                            entrada.codigo
                        )

                if (producto != null) {

                    // restar stock

                    val nuevaCantidad =

                        (producto.cantidad -
                                entrada.cantidad)
                            .coerceAtLeast(0)

                    val productoActualizado =

                        producto.copy(

                            cantidad =
                                nuevaCantidad
                        )

                    // actualizar producto

                    productoDao.actualizar(
                        productoActualizado
                    )

                    // firebase producto

                    firebaseRepo
                        .guardarProducto(
                            productoActualizado
                        )
                }

                // eliminar entrada local

                dao.delete(
                    entrada
                )

                // eliminar firebase

                firebaseRepo
                    .eliminarEntrada(
                        entrada.id
                    )

            } catch (e: Exception) {

                e.printStackTrace()
            }
        }
    }

    // obtener por bodega

    fun obtenerEntradasPorBodega(

        bodegaId: String

    ): Flow<List<Entrada>> {

        return dao
            .getEntradasByBodega(
                bodegaId
            )
    }

    // eliminar todo

    fun eliminarTodo() {

        viewModelScope.launch(

            Dispatchers.IO

        ) {

            dao.deleteAll()
        }
    }
}