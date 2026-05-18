package com.example.inventario.viewModel



import android.app.Application

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope

import com.example.inventario.data.Bodega
import com.example.inventario.data.FirebaseRepository
import com.example.inventario.data.appdatabase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class BodegaViewModel(

    application: Application

) : AndroidViewModel(application) {

    // dao
    private val dao =
        appdatabase
            .getDatabase(application)
            .bodegaDao()

    // firebase
    private val firebaseRepo =
        FirebaseRepository()

    // lista
    val bodegas: Flow<List<Bodega>> =
        dao.obtenerTodas()

    // init
    init {

        sincronizarDesdeFirebase()
    }

    // sync
    private fun sincronizarDesdeFirebase() {

        viewModelScope.launch {

            try {

                val nube =
                    firebaseRepo.obtenerBodegas()

                nube.forEach { bodega ->

                    val local =
                        dao.obtenerPorId(
                            bodega.id
                        )

                    if (local == null) {

                        dao.insertar(bodega)
                    }
                }

            } catch (_: Exception) {

            }
        }
    }

    // crear
    fun crearBodega(

        nombre: String

    ) {

        viewModelScope.launch {

            val id = nombre
                .lowercase()
                .replace(" ", "-")

            val existe =
                dao.obtenerPorId(id)

            if (existe == null) {

                val nuevaBodega = Bodega(

                    id = id,

                    nombre = nombre
                )

                // local
                dao.insertar(nuevaBodega)

                // nube
                firebaseRepo.guardarBodega(
                    nuevaBodega
                )
            }
        }
    }

    // editar
    fun editarBodega(

        bodega: Bodega

    ) {

        viewModelScope.launch {

            // local
            dao.actualizar(bodega)

            // nube
            firebaseRepo.guardarBodega(
                bodega
            )
        }
    }

    // eliminar
    fun eliminarBodega(

        bodega: Bodega

    ) {

        viewModelScope.launch {

            // local
            dao.eliminar(bodega)

            // nube
            firebaseRepo.eliminarBodega(
                bodega.id
            )
        }
    }

    // buscar
    suspend fun obtenerBodega(

        id: String

    ): Bodega? {

        return dao.obtenerPorId(id)
    }
}