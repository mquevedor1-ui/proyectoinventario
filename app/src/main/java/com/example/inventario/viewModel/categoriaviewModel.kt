package com.example.inventario.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.inventario.data.FirebaseRepository
import com.example.inventario.data.appdatabase
import com.example.inventario.data.categoria
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CategoriaViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val dao = appdatabase.getDatabase(application).categoriaDao()
    private val firebaseRepo = FirebaseRepository()

    private val _categorias = MutableStateFlow<List<categoria>>(emptyList())
    val categorias: StateFlow<List<categoria>> = _categorias

    init {
        // 1. Cargar datos locales de inmediato
        cargarCategoriasLocales()
        // 2. Sincronizar con la nube
        sincronizarConFirebase()
    }

    private fun cargarCategoriasLocales() {
        viewModelScope.launch {
            dao.obtenerCategorias().collect {
                _categorias.value = it
            }
        }
    }

    private fun sincronizarConFirebase() {
        viewModelScope.launch {
            try {
                val categoriasNube = firebaseRepo.obtenerCategorias()
                categoriasNube.forEach { cat ->
                    // Si no existe localmente por nombre o ID, la insertamos
                    val local = dao.obtenerCategoriaPorId(cat.id)
                    if (local == null) {
                        dao.insertar(cat)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // Insertar -> Local + Firebase
    fun insertarCategoria(categoria: categoria) {
        viewModelScope.launch {
            // Room genera el ID, luego lo subimos
            val idGenerado = dao.insertar(categoria).toInt()
            val categoriaConId = categoria.copy(id = idGenerado)
            firebaseRepo.guardarCategoria(categoriaConId)
        }
    }

    // Actualizar -> Local + Firebase
    fun actualizarCategoria(categoria: categoria) {
        viewModelScope.launch {
            dao.actualizar(categoria)
            firebaseRepo.guardarCategoria(categoria)
        }
    }

    // Eliminar -> Local + Firebase
    fun eliminarCategoria(categoria: categoria) {
        viewModelScope.launch {
            dao.eliminar(categoria)
            firebaseRepo.eliminarCategoria(categoria.id)
        }
    }

    suspend fun buscarCategoria(nombre: String): categoria? {
        return dao.buscarCategoria(nombre)
    }

    suspend fun obtenerCategoriaPorId(id: Int): categoria? {
        return dao.obtenerCategoriaPorId(id)
    }
}
