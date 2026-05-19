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

    private val dao = appdatabase.getDatabase(application).bodegaDao()
    private val firebaseRepo = FirebaseRepository()

    val bodegas: Flow<List<Bodega>> = dao.obtenerTodas()

    init {
        // Al iniciar, intentamos sincronizar desde Firebase para tener los datos frescos
        sincronizarDesdeFirebase()
    }

    fun sincronizarDesdeFirebase() {
        viewModelScope.launch {
            try {
                val nube = firebaseRepo.obtenerBodegas()
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

    fun crearBodega(nombre: String) {
        viewModelScope.launch {
            val id = nombre.lowercase().trim().replace(" ", "-")
            if (id.isEmpty()) return@launch

            val nuevaBodega = Bodega(id = id, nombre = nombre)
            
            // Local
            dao.insertar(nuevaBodega)
            
            // Firebase (ahora suspendido para asegurar envío)
            firebaseRepo.guardarBodega(nuevaBodega)
        }
    }

    fun editarBodega(bodega: Bodega) {
        viewModelScope.launch {
            dao.actualizar(bodega)
            firebaseRepo.guardarBodega(bodega)
        }
    }

    fun eliminarBodega(bodega: Bodega) {
        viewModelScope.launch {
            dao.eliminar(bodega)
            firebaseRepo.eliminarBodega(bodega.id)
        }
    }

    suspend fun obtenerBodega(id: String): Bodega? {
        return dao.obtenerPorId(id)
    }
}
