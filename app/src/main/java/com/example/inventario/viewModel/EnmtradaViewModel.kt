package com.example.inventario.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.inventario.data.Entrada
import com.example.inventario.data.FirebaseRepository
import com.example.inventario.data.appdatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class EntradaViewModel(application: Application) : AndroidViewModel(application) {
    private val dao = appdatabase.getDatabase(application).entradaDao()
    private val productoDao = appdatabase.getDatabase(application).productoDao()
    private val firebaseRepo = FirebaseRepository()

    val allEntradas: Flow<List<Entrada>> = dao.getAllEntradas()

    fun sincronizarDesdeFirebase() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val entradasNube = firebaseRepo.obtenerEntradas()
                entradasNube.forEach { entrada ->
                    dao.insert(entrada)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun agregarEntrada(entrada: Entrada) {
        viewModelScope.launch(Dispatchers.IO) {
            // 1. Guardar entrada localmente
            dao.insert(entrada)
            
            // 2. Actualizar stock del producto
            val producto = productoDao.obtenerProductoPorId(entrada.productoId)
            if (producto != null) {
                val nuevaCantidad = producto.cantidad + entrada.cantidad
                val productoActualizado = producto.copy(cantidad = nuevaCantidad)
                productoDao.actualizar(productoActualizado)
                firebaseRepo.guardarProducto(productoActualizado)
            }
            
            // 3. Guardar en Firebase
            firebaseRepo.guardarEntrada(entrada)
        }
    }

    fun eliminarEntrada(entrada: Entrada) {
        viewModelScope.launch(Dispatchers.IO) {
            // 1. Revertir stock
            val producto = productoDao.obtenerProductoPorId(entrada.productoId)
            if (producto != null) {
                val nuevaCantidad = producto.cantidad - entrada.cantidad
                val productoActualizado = producto.copy(cantidad = nuevaCantidad)
                productoDao.actualizar(productoActualizado)
                firebaseRepo.guardarProducto(productoActualizado)
            }
            
            // 2. Eliminar localmente
            dao.delete(entrada)
            
            // 3. Eliminar de Firebase
            firebaseRepo.eliminarEntrada(entrada.id)
        }
    }
}
