package com.example.inventario.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.inventario.data.Factura
import com.example.inventario.data.FirebaseRepository
import com.example.inventario.data.appdatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class FacturaViewModel(application: Application) : AndroidViewModel(application) {
    private val dao = appdatabase.getDatabase(application).facturaDao()
    private val firebaseRepo = FirebaseRepository()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    private val _filtroPeriodo = MutableStateFlow("Dia") // Dia, Semana, Mes, Año, Todo
    val filtroPeriodo: StateFlow<String> = _filtroPeriodo

    private val _fechaReferencia = MutableStateFlow(Calendar.getInstance())
    val fechaReferencia: StateFlow<Calendar> = _fechaReferencia

    val periodoTexto: StateFlow<String> = combine(_filtroPeriodo, _fechaReferencia) { periodo, cal ->
        when (periodo) {
            "Dia" -> "Día: " + SimpleDateFormat("dd 'de' MMMM yyyy", Locale("es", "ES")).format(cal.time)
            "Semana" -> "Semana " + cal.get(Calendar.WEEK_OF_MONTH) + " de " + SimpleDateFormat("MMMM yyyy", Locale("es", "ES")).format(cal.time)
            "Mes" -> "Mes de " + SimpleDateFormat("MMMM yyyy", Locale("es", "ES")).format(cal.time).replaceFirstChar { it.uppercase() }
            "Año" -> "Año " + cal.get(Calendar.YEAR)
            else -> "Todo el Historial"
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "Cargando...")

    fun setFiltroPeriodo(periodo: String) {
        _filtroPeriodo.value = periodo
    }

    fun setFechaReferencia(calendar: Calendar) {
        _fechaReferencia.value = calendar
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val allFacturas: Flow<List<Factura>> = combine(_searchQuery, _filtroPeriodo, _fechaReferencia) { query, periodo, fecha ->
        Triple(query, periodo, fecha)
    }.flatMapLatest { (query, periodo, fecha) ->
        val flow = if (query.isEmpty()) {
            dao.getAllFacturas()
        } else {
            dao.buscarFacturas("", query)
        }
        flow.map { lista -> filtrarPorPeriodo(lista, periodo, fecha) }
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    fun obtenerFacturasFiltradas(bodegaId: String): Flow<List<Factura>> {
        return combine(_searchQuery, _filtroPeriodo, _fechaReferencia) { query, periodo, fecha ->
            Triple(query, periodo, fecha)
        }.flatMapLatest { (query, periodo, fecha) ->
            val flow = if (query.isEmpty()) {
                dao.getFacturasByBodega(bodegaId)
            } else {
                dao.buscarFacturas(bodegaId, query)
            }
            flow.map { lista -> filtrarPorPeriodo(lista, periodo, fecha) }
        }
    }

    private fun filtrarPorPeriodo(lista: List<Factura>, periodo: String, calRef: Calendar): List<Factura> {
        if (periodo == "Todo") return lista
        val sdf = SimpleDateFormat("d/M/yyyy", Locale.getDefault())
        return lista.filter { factura ->
            try {
                val fechaFactura = sdf.parse(factura.fecha) ?: return@filter false
                val calFactura = Calendar.getInstance().apply { time = fechaFactura }
                when (periodo) {
                    "Dia" -> {
                        calRef.get(Calendar.YEAR) == calFactura.get(Calendar.YEAR) &&
                                calRef.get(Calendar.DAY_OF_YEAR) == calFactura.get(Calendar.DAY_OF_YEAR)
                    }
                    "Semana" -> {
                        calRef.get(Calendar.YEAR) == calFactura.get(Calendar.YEAR) &&
                                calRef.get(Calendar.WEEK_OF_YEAR) == calFactura.get(Calendar.WEEK_OF_YEAR)
                    }
                    "Mes" -> {
                        calRef.get(Calendar.YEAR) == calFactura.get(Calendar.YEAR) &&
                                calRef.get(Calendar.MONTH) == calFactura.get(Calendar.MONTH)
                    }
                    "Año" -> {
                        calRef.get(Calendar.YEAR) == calFactura.get(Calendar.YEAR)
                    }
                    else -> true
                }
            } catch (e: Exception) {
                false
            }
        }
    }

    init {
        sincronizarDesdeFirebase()
    }

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

    suspend fun obtenerFacturaPorId(id: Int): Factura? {
        return dao.getFacturaById(id)
    }

    fun agregarFactura(factura: Factura) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val idGenerado = dao.insert(factura)
                val facturaConId = factura.copy(id = idGenerado.toInt())
                firebaseRepo.guardarFactura(facturaConId)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun eliminarFactura(factura: Factura) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val now = System.currentTimeMillis()
                dao.softDelete(factura.id, now)
                firebaseRepo.guardarFactura(factura.copy(isDeleted = true, deletionDate = now))
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun restaurarFactura(factura: Factura) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                dao.restore(factura.id)
                firebaseRepo.guardarFactura(factura.copy(isDeleted = false, deletionDate = null))
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun obtenerPapelera() = dao.getDeletedFacturas()

    fun purgarAntiguos() {
        viewModelScope.launch(Dispatchers.IO) {
            val threshold = System.currentTimeMillis() - (90L * 24 * 60 * 60 * 1000)
            dao.permanentPurge(threshold)
        }
    }

    fun eliminarPermanente(factura: Factura) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                dao.deletePermanently(factura.id)
                firebaseRepo.eliminarFactura(factura.id)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun actualizarFactura(factura: Factura) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                dao.update(factura)
                firebaseRepo.guardarFactura(factura)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun obtenerFacturasPorBodega(bodegaId: String): Flow<List<Factura>> {
        return dao.getFacturasByBodega(bodegaId)
    }
}
