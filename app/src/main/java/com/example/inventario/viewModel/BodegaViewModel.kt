package com.example.inventario.viewModel
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.inventario.data.Bodega
import com.example.inventario.data.appdatabase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

import com.example.inventario.data.FirebaseRepository

class BodegaViewModel(application: Application) : AndroidViewModel(application) {

    private val dao = appdatabase.getDatabase(application).bodegaDao()
    private val firebaseRepo = FirebaseRepository()

    val bodegas: Flow<List<Bodega>> = dao.obtenerTodas()

    init {
        // Al iniciar el ViewModel, intentamos bajar datos de la nube (opcional)
        sincronizarDesdeFirebase()
    }

    private fun sincronizarDesdeFirebase() {
        viewModelScope.launch {
            val deNube = firebaseRepo.obtenerBodegas()
            deNube.forEach { bodega ->
                val local = dao.obtenerPorId(bodega.id)
                if (local == null) {
                    dao.insertar(bodega)
                }
            }
        }
    }

    fun crearBodega(nombre: String) {
        viewModelScope.launch {
            val id = nombre.lowercase().replace(" ", "-")
            val nuevaBodega = Bodega(id, nombre)
            
            // Guardar local
            val existe = dao.obtenerPorId(id)
            if (existe == null) {
                dao.insertar(nuevaBodega)
                // Guardar en nube
                firebaseRepo.guardarBodega(nuevaBodega)
            }
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
}
