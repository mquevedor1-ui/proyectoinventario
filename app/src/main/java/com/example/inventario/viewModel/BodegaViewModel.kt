package com.example.inventario.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope

import com.example.inventario.data.Bodega
import com.example.inventario.data.FirebaseRepository
import com.example.inventario.data.appdatabase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

import java.util.UUID

class BodegaViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val dao =
        appdatabase
            .getDatabase(application)
            .bodegaDao()

    private val firebaseRepo =
        FirebaseRepository()

    val bodegas:
            Flow<List<Bodega>> =

        dao.obtenerTodas()

    init {

        // sincronizar desde firebase

        sincronizarDesdeFirebase()
    }

    fun sincronizarDesdeFirebase() {

        viewModelScope.launch {

            try {

                val nube =
                    firebaseRepo
                        .obtenerBodegas()

                nube.forEach { bodega ->

                    dao.insertar(bodega)
                }

            } catch (e: Exception) {

                e.printStackTrace()
            }
        }
    }

    fun actualizarTodoDesdeNube() {

        sincronizarDesdeFirebase()
    }

    fun crearBodega(
        nombre: String
    ) {

        viewModelScope.launch {

            // ID UNICO PROFESIONAL

            val id =
                UUID.randomUUID()
                    .toString()

            if (

                nombre
                    .trim()
                    .isEmpty()

            ) return@launch

            val nuevaBodega =

                Bodega(

                    id = id,

                    nombre = nombre
                )

            // guardar local

            dao.insertar(
                nuevaBodega
            )

            // guardar firebase

            firebaseRepo
                .guardarBodega(
                    nuevaBodega
                )
        }
    }

    fun editarBodega(
        bodega: Bodega
    ) {

        viewModelScope.launch {

            dao.actualizar(
                bodega
            )

            firebaseRepo
                .guardarBodega(
                    bodega
                )
        }
    }

    fun eliminarBodega(
        bodega: Bodega
    ) {

        viewModelScope.launch {

            val bodegaEliminada =

                bodega.copy(

                    isDeleted = true,

                    deletionDate =
                        System.currentTimeMillis()
                )

            dao.actualizar(
                bodegaEliminada
            )

            firebaseRepo
                .guardarBodega(
                    bodegaEliminada
                )
        }
    }

    fun restaurarBodega(
        bodega: Bodega
    ) {

        viewModelScope.launch {

            val bodegaRestaurada =

                bodega.copy(

                    isDeleted = false,

                    deletionDate = null
                )

            dao.actualizar(
                bodegaRestaurada
            )

            firebaseRepo
                .guardarBodega(
                    bodegaRestaurada
                )
        }
    }

    fun obtenerPapelera():
            Flow<List<Bodega>> =

        dao.obtenerPapelera()

    fun purgarAntiguos() {

        viewModelScope.launch {

            val limite =

                System.currentTimeMillis() -
                        (
                                90L * 24 * 60 * 60 * 1000
                                )

            dao.purgarAntiguos(
                limite
            )
        }
    }

    fun eliminarPermanente(
        bodega: Bodega
    ) {

        viewModelScope.launch {

            dao.eliminarPermanente(
                bodega.id
            )

            firebaseRepo
                .eliminarBodega(
                    bodega.id
                )
        }
    }

    suspend fun obtenerBodega(
        id: String
    ): Bodega? {

        return dao.obtenerPorId(id)
    }
}