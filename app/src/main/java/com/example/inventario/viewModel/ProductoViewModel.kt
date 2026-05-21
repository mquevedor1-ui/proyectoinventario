package com.example.inventario.viewModel
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope

import com.example.inventario.data.FirebaseRepository
import com.example.inventario.data.InventoryRepository
import com.example.inventario.data.appdatabase
import com.example.inventario.data.producto

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProductoViewModel(

    application: Application

) : AndroidViewModel(application) {

    // database

    private val db =

        appdatabase
            .getDatabase(application)

    // repository

    private val repository =

        InventoryRepository(

            bodegaDao =
                db.bodegaDao(),

            productoDao =
                db.productoDao(),

            categoriaDao =
                db.categoriaDao(),

            firebaseRepository =
                FirebaseRepository()
        )

    // dao

    private val dao =
        db.productoDao()

    // obtener productos

    fun obtenerProductos(

        bodegaId: String

    ) = repository
        .getProductos(
            bodegaId
        )

    // generar codigo

    suspend fun generarSiguienteCodigo(

        prefijo: String

    ): String {

        val ultimoCodigo =

            dao.obtenerUltimoCodigoPorPrefijo(
                prefijo
            )

        return if (

            ultimoCodigo == null

        ) {

            "${prefijo}0001"

        } else {

            try {

                val numeroStr =

                    ultimoCodigo
                        .removePrefix(
                            prefijo
                        )

                val siguienteNumero =

                    numeroStr
                        .toInt() + 1

                prefijo +
                        siguienteNumero
                            .toString()
                            .padStart(
                                4,
                                '0'
                            )

            } catch (

                e: Exception

            ) {

                "${prefijo}0001"
            }
        }
    }

    // sincronizar firebase

    fun sincronizarDesdeFirebase(

        bodegaId: String

    ) {

        viewModelScope.launch(

            Dispatchers.IO

        ) {

            repository
                .sincronizarProductos(
                    bodegaId
                )
        }
    }

    // obtener producto por id

    suspend fun obtenerProductoPorId(

        id: Int

    ): producto? {

        return dao
            .obtenerProductoPorId(
                id
            )
    }

    // obtener producto por codigo

    suspend fun obtenerProductoPorCodigo(

        codigo: String

    ): producto? {

        return dao
            .obtenerProductoPorCodigo(
                codigo
            )
    }

    // insertar producto

    fun agregarProducto(

        producto: producto

    ) {

        viewModelScope.launch(

            Dispatchers.IO

        ) {

            repository
                .insertProducto(
                    producto
                )
        }
    }

    // actualizar producto

    fun actualizarProducto(

        producto: producto

    ) {

        viewModelScope.launch(

            Dispatchers.IO

        ) {

            repository
                .actualizarProducto(
                    producto
                )
        }
    }

    // eliminar producto (Soft Delete)
    fun eliminarProducto(
        producto: producto
    ) {
        viewModelScope.launch(
            Dispatchers.IO
        ) {
            repository
                .softEliminarProducto(
                    producto
                )
        }
    }

    fun restaurarProducto(producto: producto) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.restaurarProducto(producto)
        }
    }

    fun obtenerPapelera() = repository.getProductosPapelera()

    fun purgarAntiguos() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.purgarProductosAntiguos(90)
        }
    }

    fun eliminarPermanente(producto: producto) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.eliminarProductoPermanente(producto)
        }
    }

    fun obtenerProductosBajoStock() = dao.obtenerProductosBajoStock()

    // =========================
    // SUMAR CANTIDAD
    // =========================

    fun sumarCantidadProducto(

        codigo: String,

        cantidadEntrada: Int

    ) {

        viewModelScope.launch(

            Dispatchers.IO

        ) {

            val productoActual =

                dao.obtenerProductoPorCodigo(
                    codigo
                )

            productoActual?.let {

                val productoActualizado =

                    it.copy(

                        cantidad =
                            it.cantidad + cantidadEntrada
                    )

                repository
                    .actualizarProducto(
                        productoActualizado
                    )
            }
        }
    }

    // =========================
    // RESTAR CANTIDAD
    // =========================

    fun restarCantidadProducto(

        codigo: String,

        cantidadSalida: Int

    ) {

        viewModelScope.launch(

            Dispatchers.IO

        ) {

            val productoActual =

                dao.obtenerProductoPorCodigo(
                    codigo
                )

            productoActual?.let {

                val nuevaCantidad =

                    (it.cantidad - cantidadSalida)
                        .coerceAtLeast(0)

                val productoActualizado =

                    it.copy(

                        cantidad = nuevaCantidad
                    )

                repository
                    .actualizarProducto(
                        productoActualizado
                    )
            }
        }
    }
}