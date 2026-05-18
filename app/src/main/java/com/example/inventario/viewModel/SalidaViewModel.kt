package com.example.inventario.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.inventario.data.Salida
import com.example.inventario.data.FirebaseRepository
import com.example.inventario.data.appdatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class SalidaViewModel(application: Application) : AndroidViewModel(application) {
    private val dao = appdatabase.getDatabase(application).salidaDao()
    private val productoDao = appdatabase.getDatabase(application).productoDao()
    private val firebaseRepo = FirebaseRepository()

    val allSalidas: Flow<List<Salida>> = dao.getAllSalidas()

    fun sincronizarDesdeFirebase() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val salidasNube = firebaseRepo.obtenerSalidas()
                salidasNube.forEach { salida ->
                    dao.insert(salida)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun agregarSalida(salida: Salida) {
        viewModelScope.launch(Dispatchers.IO) {
            // 1. Guardar salida localmente
            dao.insert(salida)
            
            // 2. Actualizar stock del producto
            val producto = productoDao.obtenerProductoPorId(salida.productoId)
            if (producto != null) {
                val nuevaCantidad = producto.cantidad - salida.cantidad
                val productoActualizado = producto.copy(cantidad = nuevaCantidad)
                productoDao.actualizar(productoActualizado)
                firebaseRepo.guardarProducto(productoActualizado)
            }
            
            // 3. Guardar en Firebase
            firebaseRepo.guardarSalida(salida)
        }
    }

    fun eliminarSalida(salida: Salida) {
        viewModelScope.launch(Dispatchers.IO) {
            // 1. Revertir stock
            val producto = productoDao.obtenerProductoPorId(salida.productoId)
            if (producto != null) {
                val nuevaCantidad = producto.cantidad + salida.cantidad
                val productoActualizado = producto.copy(cantidad = nuevaCantidad)
                productoDao.actualizar(productoActualizado)
                firebaseRepo.guardarProducto(productoActualizado)
            }
            
            // 2. Eliminar localmente
            dao.delete(salida)
            
            // 3. Eliminar de Firebase
            firebaseRepo.eliminarSalida(salida.id)
        }
    }
}
