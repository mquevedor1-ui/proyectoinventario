package com.example.inventario.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.inventario.data.FirebaseRepository
import com.example.inventario.data.Salida
import com.example.inventario.data.appdatabase
import com.example.inventario.data.producto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class SalidaViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val db = appdatabase.getDatabase(application)
    private val dao = db.salidaDao()
    private val productoDao = db.productoDao()
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
    val allSalidas: Flow<List<Salida>> = combine(_searchQuery, _filtroPeriodo, _fechaReferencia) { query, periodo, fecha ->
        Triple(query, periodo, fecha)
    }.flatMapLatest { (query, periodo, fecha) ->
        val flow = if (query.isEmpty()) {
            dao.getAllSalidas()
        } else {
            dao.buscarSalidas("", query)
        }
        flow.map { lista -> filtrarPorPeriodo(lista, periodo, fecha) }
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    fun obtenerSalidasFiltradas(bodegaId: String): Flow<List<Salida>> {
        return combine(_searchQuery, _filtroPeriodo, _fechaReferencia) { query, periodo, fecha ->
            Triple(query, periodo, fecha)
        }.flatMapLatest { (query, periodo, fecha) ->
            val flow = if (query.isEmpty()) {
                dao.getSalidasByBodega(bodegaId)
            } else {
                dao.buscarSalidas(bodegaId, query)
            }
            flow.map { lista -> filtrarPorPeriodo(lista, periodo, fecha) }
        }
    }

    fun obtenerTotalCostoFiltrado(bodegaId: String): Flow<Double> {
        return obtenerSalidasFiltradas(bodegaId).map { lista ->
            lista.sumOf { it.cantidad * it.costoUnitario }
        }
    }

    private fun filtrarPorPeriodo(lista: List<Salida>, periodo: String, calRef: Calendar): List<Salida> {
        if (periodo == "Todo") return lista
        val sdf = SimpleDateFormat("d/M/yyyy", Locale.getDefault())
        return lista.filter { salida ->
            try {
                val fechaSalida = sdf.parse(salida.fecha) ?: return@filter false
                val calSalida = Calendar.getInstance().apply { time = fechaSalida }
                when (periodo) {
                    "Dia" -> {
                        calRef.get(Calendar.YEAR) == calSalida.get(Calendar.YEAR) &&
                                calRef.get(Calendar.DAY_OF_YEAR) == calSalida.get(Calendar.DAY_OF_YEAR)
                    }
                    "Semana" -> {
                        calRef.get(Calendar.YEAR) == calSalida.get(Calendar.YEAR) &&
                                calRef.get(Calendar.WEEK_OF_YEAR) == calSalida.get(Calendar.WEEK_OF_YEAR)
                    }
                    "Mes" -> {
                        calRef.get(Calendar.YEAR) == calSalida.get(Calendar.YEAR) &&
                                calRef.get(Calendar.MONTH) == calSalida.get(Calendar.MONTH)
                    }
                    "Año" -> {
                        calRef.get(Calendar.YEAR) == calSalida.get(Calendar.YEAR)
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
                val salidasNube = firebaseRepo.obtenerSalidas()
                salidasNube.forEach { salida ->
                    dao.insert(salida)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    suspend fun obtenerSalidaPorId(id: Int): Salida? {
        return dao.getSalidaById(id)
    }

    fun agregarSalida(salida: Salida) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val producto = productoDao.obtenerProductoPorCodigo(salida.codigo)
                val salidaConCosto = if (producto != null) {
                    salida.copy(costoUnitario = producto.costo)
                } else {
                    salida
                }

                val idGenerado = dao.insert(salidaConCosto)
                val salidaFinal = salidaConCosto.copy(id = idGenerado.toInt())
                firebaseRepo.guardarSalida(salidaFinal)

                if (producto != null) {
                    val productoActualizado = producto.copy(
                        cantidad = (producto.cantidad - salida.cantidad).coerceAtLeast(0)
                    )
                    productoDao.actualizar(productoActualizado)
                    firebaseRepo.guardarProducto(productoActualizado)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun actualizarSalida(nuevaSalida: Salida, viejaSalida: Salida) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                dao.update(nuevaSalida)
                firebaseRepo.guardarSalida(nuevaSalida)

                if (viejaSalida.codigo == nuevaSalida.codigo) {
                    val diferencia = viejaSalida.cantidad - nuevaSalida.cantidad
                    val producto = productoDao.obtenerProductoPorCodigo(nuevaSalida.codigo)
                    if (producto != null) {
                        val productoActualizado = producto.copy(
                            cantidad = (producto.cantidad + diferencia).coerceAtLeast(0)
                        )
                        productoDao.actualizar(productoActualizado)
                        firebaseRepo.guardarProducto(productoActualizado)
                    }
                } else {
                    val productoViejo = productoDao.obtenerProductoPorCodigo(viejaSalida.codigo)
                    if (productoViejo != null) {
                        val pVActualizado = productoViejo.copy(cantidad = productoViejo.cantidad + viejaSalida.cantidad)
                        productoDao.actualizar(pVActualizado)
                        firebaseRepo.guardarProducto(pVActualizado)
                    }
                    val productoNuevo = productoDao.obtenerProductoPorCodigo(nuevaSalida.codigo)
                    if (productoNuevo != null) {
                        val pNActualizado = productoNuevo.copy(cantidad = (productoNuevo.cantidad - nuevaSalida.cantidad).coerceAtLeast(0))
                        productoDao.actualizar(pNActualizado)
                        firebaseRepo.guardarProducto(pNActualizado)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun eliminarSalida(salida: Salida) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                // Soft Delete
                val now = System.currentTimeMillis()
                dao.softDelete(salida.id, now)
                firebaseRepo.guardarSalida(salida.copy(isDeleted = true, deletionDate = now))

                // Revertir stock (sumar de nuevo lo que salió)
                val producto = productoDao.obtenerProductoPorCodigo(salida.codigo)
                if (producto != null) {
                    val productoActualizado = producto.copy(
                        cantidad = producto.cantidad + salida.cantidad
                    )
                    productoDao.actualizar(productoActualizado)
                    firebaseRepo.guardarProducto(productoActualizado)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun restaurarSalida(salida: Salida) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                dao.restore(salida.id)
                firebaseRepo.guardarSalida(salida.copy(isDeleted = false, deletionDate = null))

                // Volver a restar stock
                val producto = productoDao.obtenerProductoPorCodigo(salida.codigo)
                if (producto != null) {
                    val productoActualizado = producto.copy(
                        cantidad = (producto.cantidad - salida.cantidad).coerceAtLeast(0)
                    )
                    productoDao.actualizar(productoActualizado)
                    firebaseRepo.guardarProducto(productoActualizado)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun obtenerPapelera() = dao.getDeletedSalidas()

    fun purgarAntiguos() {
        viewModelScope.launch(Dispatchers.IO) {
            val threshold = System.currentTimeMillis() - (90L * 24 * 60 * 60 * 1000)
            dao.permanentPurge(threshold)
        }
    }

    fun eliminarPermanente(salida: Salida) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                dao.deletePermanently(salida.id)
                firebaseRepo.eliminarSalida(salida.id)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun obtenerSalidasPorBodega(bodegaId: String): Flow<List<Salida>> {
        return dao.getSalidasByBodega(bodegaId)
    }

    fun eliminarTodo() {
        viewModelScope.launch(Dispatchers.IO) {
            dao.deleteAll()
        }
    }
}
