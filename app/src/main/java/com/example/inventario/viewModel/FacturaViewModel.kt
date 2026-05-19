package com.example.inventario.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.inventario.data.Factura
import com.example.inventario.data.FirebaseRepository
import com.example.inventario.data.appdatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class FacturaViewModel(application: Application) : AndroidViewModel(application) {
    private val dao = appdatabase.getDatabase(application).facturaDao()
    private val firebaseRepo = FirebaseRepository()

    val allFacturas: Flow<List<Factura>> = dao.getAllFacturas()

    fun sincronizarDesdeFirebase() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val facturasNube = firebaseRepo.obtenerFacturas()
                facturasNube.forEach { factura ->
                    dao.insert(factura)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun agregarFactura(factura: Factura) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                // 1. Guardar local y obtener ID
                val idGenerado = dao.insert(factura)
                // 2. Actualizar objeto con ID real
                val facturaConId = factura.copy(id = idGenerado.toInt())
                // 3. Guardar en Firebase
                firebaseRepo.guardarFactura(facturaConId)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun eliminarFactura(factura: Factura) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.delete(factura)
            firebaseRepo.eliminarFactura(factura.id)
        }
    }

    fun obtenerFacturasPorBodega(bodegaId: String): Flow<List<Factura>> {
        return dao.getFacturasByBodega(bodegaId)
    }
}
