package com.example.inventario.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.inventario.data.Entrada
import com.example.inventario.data.Factura
import com.example.inventario.data.FirebaseRepository
import com.example.inventario.data.appdatabase
import com.example.inventario.data.producto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class EntradaViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val db = appdatabase.getDatabase(application)
    private val dao = db.entradaDao()
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
    val allEntradas: Flow<List<Entrada>> = combine(_searchQuery, _filtroPeriodo, _fechaReferencia) { query, periodo, fecha ->
        Triple(query, periodo, fecha)
    }.flatMapLatest { (query, periodo, fecha) ->
        val flow = if (query.isEmpty()) {
            dao.getAllEntradas()
        } else {
            dao.buscarEntradas("", query)
        }
        flow.map { lista -> filtrarPorPeriodo(lista, periodo, fecha) }
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    fun obtenerEntradasFiltradas(bodegaId: String): Flow<List<Entrada>> {
        return combine(_searchQuery, _filtroPeriodo, _fechaReferencia) { query, periodo, fecha ->
            Triple(query, periodo, fecha)
        }.flatMapLatest { (query, periodo, fecha) ->
            val flow = if (query.isEmpty()) {
                dao.getEntradasByBodega(bodegaId)
            } else {
                dao.buscarEntradas(bodegaId, query)
            }
            flow.map { lista -> filtrarPorPeriodo(lista, periodo, fecha) }
        }
    }

    private fun filtrarPorPeriodo(lista: List<Entrada>, periodo: String, calRef: Calendar): List<Entrada> {
        if (periodo == "Todo") return lista
        val sdf = SimpleDateFormat("d/M/yyyy", Locale.getDefault())
        return lista.filter { entrada ->
            try {
                val fechaEntrada = sdf.parse(entrada.fecha) ?: return@filter false
                val calEntrada = Calendar.getInstance().apply { time = fechaEntrada }
                when (periodo) {
                    "Dia" -> {
                        calRef.get(Calendar.YEAR) == calEntrada.get(Calendar.YEAR) &&
                                calRef.get(Calendar.DAY_OF_YEAR) == calEntrada.get(Calendar.DAY_OF_YEAR)
                    }
                    "Semana" -> {
                        calRef.get(Calendar.YEAR) == calEntrada.get(Calendar.YEAR) &&
                                calRef.get(Calendar.WEEK_OF_YEAR) == calEntrada.get(Calendar.WEEK_OF_YEAR)
                    }
                    "Mes" -> {
                        calRef.get(Calendar.YEAR) == calEntrada.get(Calendar.YEAR) &&
                                calRef.get(Calendar.MONTH) == calEntrada.get(Calendar.MONTH)
                    }
                    "Año" -> {
                        calRef.get(Calendar.YEAR) == calEntrada.get(Calendar.YEAR)
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
            try {
                val idGenerado = dao.insert(entrada)
                val entradaConId = entrada.copy(id = idGenerado.toInt())
                firebaseRepo.guardarEntrada(entradaConId)

                if (entrada.costoUnitario > 0 && entrada.proveedor.isNotEmpty()) {
                    val facturaAuto = Factura(
                        numeroFactura = if (entrada.numeroFactura.isNotEmpty()) entrada.numeroFactura else "ENT-${System.currentTimeMillis() / 1000}",
                        fecha = entrada.fecha,
                        proveedor = entrada.proveedor,
                        total = entrada.costoUnitario * entrada.cantidad,
                        codigo = entrada.codigo,
                        productos = "${entrada.cantidad} - ${entrada.descripcion}",
                        bodegaId = entrada.bodegaId,
                        notas = "Generada automáticamente desde Entrada: ${entrada.notas}",
                        categoria = entrada.categoria,
                        usuario = SessionManager.nombreUsuario()
                    )
                    val idFactura = db.facturaDao().insert(facturaAuto)
                    firebaseRepo.guardarFactura(facturaAuto.copy(id = idFactura.toInt()))
                }

                val productoExistente = productoDao.obtenerProductoPorCodigo(entrada.codigo)
                if (productoExistente != null) {
                    // Cálculo de Costo Promedio Ponderado
                    val cantidadTotal = productoExistente.cantidad + entrada.cantidad
                    val costoTotalExistente = productoExistente.cantidad * productoExistente.costo
                    val costoTotalNuevo = entrada.cantidad * entrada.costoUnitario
                    val nuevoCostoPromedio = if (cantidadTotal > 0) (costoTotalExistente + costoTotalNuevo) / cantidadTotal else entrada.costoUnitario

                    val productoActualizado = productoExistente.copy(
                        cantidad = cantidadTotal,
                        descripcion = entrada.descripcion,
                        categoria = entrada.categoria,
                        proveedor = entrada.proveedor,
                        costo = nuevoCostoPromedio,
                        stockMinimo = if (entrada.stockMinimo > 0) entrada.stockMinimo else productoExistente.stockMinimo,
                        unidad = entrada.unidad,
                        ubicacion = entrada.ubicacion,
                        fechaIngreso = entrada.fecha,
                        notas = entrada.notas
                    )
                    productoDao.actualizar(productoActualizado)
                    firebaseRepo.guardarProducto(productoActualizado)
                } else {
                    val nuevoProducto = producto(
                        bodegaId = entrada.bodegaId,
                        codigo = entrada.codigo,
                        descripcion = entrada.descripcion,
                        categoria = entrada.categoria,
                        cantidad = entrada.cantidad,
                        unidad = entrada.unidad,
                        ubicacion = entrada.ubicacion,
                        proveedor = entrada.proveedor,
                        costo = entrada.costoUnitario,
                        stockMinimo = entrada.stockMinimo,
                        fechaIngreso = entrada.fecha,
                        notas = entrada.notas
                    )
                    productoDao.insertar(nuevoProducto)
                    firebaseRepo.guardarProducto(nuevoProducto)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    suspend fun obtenerEntradaPorId(id: Int): Entrada? {
        return dao.getEntradaById(id)
    }

    fun actualizarEntrada(nuevaEntrada: Entrada, viejaEntrada: Entrada) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                dao.update(nuevaEntrada)
                firebaseRepo.guardarEntrada(nuevaEntrada)

                if (viejaEntrada.codigo == nuevaEntrada.codigo) {
                    val diferencia = nuevaEntrada.cantidad - viejaEntrada.cantidad
                    val producto = productoDao.obtenerProductoPorCodigo(nuevaEntrada.codigo)
                    if (producto != null) {
                        val productoActualizado = producto.copy(
                            cantidad = producto.cantidad + diferencia,
                            descripcion = nuevaEntrada.descripcion,
                            categoria = nuevaEntrada.categoria,
                            costo = nuevaEntrada.costoUnitario,
                            unidad = nuevaEntrada.unidad,
                            ubicacion = nuevaEntrada.ubicacion
                        )
                        productoDao.actualizar(productoActualizado)
                        firebaseRepo.guardarProducto(productoActualizado)
                    }
                } else {
                    val productoViejo = productoDao.obtenerProductoPorCodigo(viejaEntrada.codigo)
                    if (productoViejo != null) {
                        val pVActualizado = productoViejo.copy(cantidad = (productoViejo.cantidad - viejaEntrada.cantidad).coerceAtLeast(0))
                        productoDao.actualizar(pVActualizado)
                        firebaseRepo.guardarProducto(pVActualizado)
                    }
                    val productoNuevo = productoDao.obtenerProductoPorCodigo(nuevaEntrada.codigo)
                    if (productoNuevo != null) {
                        val pNActualizado = productoNuevo.copy(
                            cantidad = productoNuevo.cantidad + nuevaEntrada.cantidad,
                            descripcion = nuevaEntrada.descripcion,
                            categoria = nuevaEntrada.categoria,
                            costo = nuevaEntrada.costoUnitario,
                            unidad = nuevaEntrada.unidad,
                            ubicacion = nuevaEntrada.ubicacion
                        )
                        productoDao.actualizar(pNActualizado)
                        firebaseRepo.guardarProducto(pNActualizado)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    suspend fun buscarProductoPorCodigo(codigo: String): producto? {
        return productoDao.obtenerProductoPorCodigo(codigo)
    }

    fun eliminarEntrada(entrada: Entrada) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                // Soft Delete
                val now = System.currentTimeMillis()
                dao.softDelete(entrada.id, now)
                firebaseRepo.guardarEntrada(entrada.copy(isDeleted = true, deletionDate = now))

                // Revertir stock al eliminar entrada
                val producto = productoDao.obtenerProductoPorCodigo(entrada.codigo)
                if (producto != null) {
                    val nuevaCantidad = (producto.cantidad - entrada.cantidad).coerceAtLeast(0)
                    val productoActualizado = producto.copy(cantidad = nuevaCantidad)
                    productoDao.actualizar(productoActualizado)
                    firebaseRepo.guardarProducto(productoActualizado)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun restaurarEntrada(entrada: Entrada) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                dao.restore(entrada.id)
                firebaseRepo.guardarEntrada(entrada.copy(isDeleted = false, deletionDate = null))

                // Restaurar stock
                val producto = productoDao.obtenerProductoPorCodigo(entrada.codigo)
                if (producto != null) {
                    val productoActualizado = producto.copy(cantidad = producto.cantidad + entrada.cantidad)
                    productoDao.actualizar(productoActualizado)
                    firebaseRepo.guardarProducto(productoActualizado)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun obtenerPapelera() = dao.getDeletedEntradas()

    fun purgarAntiguos() {
        viewModelScope.launch(Dispatchers.IO) {
            val threshold = System.currentTimeMillis() - (90L * 24 * 60 * 60 * 1000)
            dao.permanentPurge(threshold)
        }
    }

    fun eliminarPermanente(entrada: Entrada) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                dao.deletePermanently(entrada.id)
                firebaseRepo.eliminarEntrada(entrada.id)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun obtenerEntradasPorBodega(bodegaId: String): Flow<List<Entrada>> {
        return dao.getEntradasByBodega(bodegaId)
    }

    fun eliminarTodo() {
        viewModelScope.launch(Dispatchers.IO) {
            dao.deleteAll()
        }
    }
}
