package com.example.inventario.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.inventario.data.FirebaseRepository
import com.example.inventario.data.Salida
import com.example.inventario.data.appdatabase
import com.example.inventario.data.producto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class SalidaViewModel(

    application: Application

) : AndroidViewModel(application) {

    // database

    private val db =
        appdatabase.getDatabase(application)

    // dao

    private val dao =
        db.salidaDao()

    private val productoDao =
        db.productoDao()

    // firebase

    private val firebaseRepo =
        FirebaseRepository()

    // lista

    val allSalidas:
            Flow<List<Salida>> =
        dao.getAllSalidas()

    // sincronizar firebase

    fun sincronizarDesdeFirebase() {

        viewModelScope.launch(
            Dispatchers.IO
        ) {

            try {

                val salidasNube =
                    firebaseRepo.obtenerSalidas()

                salidasNube.forEach { salida ->

                    dao.insert(salida)
                }

            } catch (e: Exception) {

                e.printStackTrace()
            }
        }
    }

    // agregar salida

    fun agregarSalida(

        salida: Salida

    ) {

        viewModelScope.launch(

            Dispatchers.IO

        ) {

            try {

                // buscar producto

                val producto =

                    productoDao
                        .obtenerProductoPorCodigo(

                            salida.codigo
                        )

                // validar producto

                if (producto != null) {

                    // validar stock

                    if (

                        producto.cantidad >=
                        salida.cantidad

                    ) {

                        // guardar salida

                        val idGenerado =

                            dao.insert(
                                salida
                            )

                        val salidaConId =

                            salida.copy(

                                id =
                                    idGenerado.toInt()
                            )

                        // descontar stock

                        val nuevaCantidad =

                            producto.cantidad -
                                    salida.cantidad

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

                        // firebase salida

                        firebaseRepo
                            .guardarSalida(
                                salidaConId
                            )
                    }
                }

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

    // eliminar salida

    fun eliminarSalida(

        salida: Salida

    ) {

        viewModelScope.launch(

            Dispatchers.IO

        ) {

            try {

                // buscar producto

                val producto =

                    productoDao
                        .obtenerProductoPorCodigo(

                            salida.codigo
                        )

                if (producto != null) {

                    // regresar stock

                    val nuevaCantidad =

                        producto.cantidad +
                                salida.cantidad

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

                // eliminar salida

                dao.delete(
                    salida
                )

                // firebase salida

                firebaseRepo
                    .eliminarSalida(
                        salida.id
                    )

            } catch (e: Exception) {

                e.printStackTrace()
            }
        }
    }

    // obtener salidas por bodega

    fun obtenerSalidasPorBodega(

        bodegaId: String

    ): Flow<List<Salida>> {

        return dao
            .getSalidasByBodega(
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