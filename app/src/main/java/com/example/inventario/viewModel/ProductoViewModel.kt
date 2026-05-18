package com.example.inventario.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.inventario.data.FirebaseRepository
import com.example.inventario.data.appdatabase
import com.example.inventario.data.producto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProductoViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val dao = appdatabase.getDatabase(application).productoDao()
    private val firebaseRepo = FirebaseRepository()

    // Obtener productos
    fun obtenerProductos(bodegaId: String) = dao.obtenerProductos(bodegaId)

    // Generar el siguiente código secuencial (Ej: A0001 -> A0002)
    suspend fun generarSiguienteCodigo(prefijo: String): String {
        val ultimoCodigo = dao.obtenerUltimoCodigoPorPrefijo(prefijo)
        return if (ultimoCodigo == null) {
            "${prefijo}0001"
        } else {
            try {
                // Extraer la parte numérica (asumiendo que el prefijo es de longitud variable)
                val numeroStr = ultimoCodigo.removePrefix(prefijo)
                val siguienteNumero = numeroStr.toInt() + 1
                // Formatear con ceros a la izquierda (4 dígitos)
                prefijo + siguienteNumero.toString().padStart(4, '0')
            } catch (e: Exception) {
                // Si hay error en el formato, empezamos de nuevo
                "${prefijo}0001"
            }
        }
    }

    // Sincronizar desde Firebase
    fun sincronizarDesdeFirebase(bodegaId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val productosNube = firebaseRepo.obtenerProductos(bodegaId)
                productosNube.forEach { prod ->
                    dao.insertar(prod)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    suspend fun obtenerProductoPorId(id: Int): producto? {
        return dao.obtenerProductoPorId(id)
    }

    // Insertar producto -> Local + Firebase
    fun agregarProducto(producto: producto) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.insertar(producto)
            firebaseRepo.guardarProducto(producto)
        }
    }

    // Actualizar producto -> Local + Firebase
    fun actualizarProducto(producto: producto) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.actualizar(producto)
            firebaseRepo.guardarProducto(producto)
        }
    }

    // Eliminar producto -> Local + Firebase
    fun eliminarProducto(producto: producto) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.eliminar(producto)
            firebaseRepo.eliminarProducto(producto.id)
        }
    }
}
