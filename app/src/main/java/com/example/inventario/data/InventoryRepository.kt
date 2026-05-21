package com.example.inventario.data

import kotlinx.coroutines.flow.Flow

class InventoryRepository(
    private val bodegaDao: BodegaDao,
    private val productoDao: productoDao,
    private val categoriaDao: categoriaDao,
    private val firebaseRepository: FirebaseRepository
) {

    // bodegas
    val allBodegas: Flow<List<Bodega>> = bodegaDao.obtenerTodas()

    suspend fun refreshBodegas() {
        val remoteBodegas = firebaseRepository.obtenerBodegas()
        remoteBodegas.forEach { bodega ->
            bodegaDao.insertar(bodega)
        }
    }

    suspend fun insertBodega(bodega: Bodega) {
        bodegaDao.insertar(bodega)
        firebaseRepository.guardarBodega(bodega)
    }

    // productos
    fun getProductos(bodegaId: String): Flow<List<producto>> =
        productoDao.obtenerProductos(bodegaId)

    suspend fun insertProducto(producto: producto) {
        val idGenerado = productoDao.insertar(producto)
        val productoConId = producto.copy(id = idGenerado.toInt())
        firebaseRepository.guardarProducto(productoConId)
    }

    suspend fun actualizarProducto(producto: producto) {
        productoDao.actualizar(producto)
        firebaseRepository.guardarProducto(producto)
    }

    suspend fun softEliminarProducto(producto: producto) {
        val now = System.currentTimeMillis()
        productoDao.softDelete(producto.id, now)
        firebaseRepository.guardarProducto(producto.copy(isDeleted = true, deletionDate = now))
    }

    suspend fun restaurarProducto(producto: producto) {
        productoDao.restore(producto.id)
        firebaseRepository.guardarProducto(producto.copy(isDeleted = false, deletionDate = null))
    }

    suspend fun purgarProductosAntiguos(dias: Int = 90) {
        val threshold = System.currentTimeMillis() - (dias.toLong() * 24 * 60 * 60 * 1000)
        productoDao.permanentPurge(threshold)
        // Nota: Para Firebase se requeriría una función de limpieza similar o dejar que expire
    }

    fun getProductosPapelera(): Flow<List<producto>> = productoDao.getDeletedProductos()

    suspend fun eliminarProductoPermanente(producto: producto) {
        productoDao.deletePermanently(producto.id)
        firebaseRepository.eliminarProducto(producto.codigo)
    }

    suspend fun sincronizarProductos(bodegaId: String) {
        val productosFirebase = firebaseRepository.obtenerProductos(bodegaId)
        productosFirebase.forEach { productoNube ->
            val local = productoDao.obtenerProductoPorCodigo(productoNube.codigo)
            if (local == null) {
                // Si no existe localmente, lo insertamos
                productoDao.insertar(productoNube)
            } else {
                // MERGE SEGURO: Solo sobrescribimos si el campo en la nube no está vacío.
                // Esto evita que datos locales detallados se pierdan si Firebase tiene campos vacíos.
                val productoFusionado = local.copy(
                    cantidad = productoNube.cantidad, // La cantidad siempre se sincroniza
                    descripcion = if (productoNube.descripcion.isNotEmpty()) productoNube.descripcion else local.descripcion,
                    categoria = if (productoNube.categoria.isNotEmpty()) productoNube.categoria else local.categoria,
                    unidad = if (productoNube.unidad.isNotEmpty()) productoNube.unidad else local.unidad,
                    ubicacion = if (productoNube.ubicacion.isNotEmpty()) productoNube.ubicacion else local.ubicacion,
                    proveedor = if (productoNube.proveedor.isNotEmpty()) productoNube.proveedor else local.proveedor,
                    costo = if (productoNube.costo > 0) productoNube.costo else local.costo,
                    fechaIngreso = if (productoNube.fechaIngreso.isNotEmpty()) productoNube.fechaIngreso else local.fechaIngreso,
                    notas = if (productoNube.notas.isNotEmpty()) productoNube.notas else local.notas
                )
                productoDao.actualizar(productoFusionado)
            }
        }
    }

    // categorias
    val allCategorias: Flow<List<categoria>> = categoriaDao.obtenerCategorias()

    suspend fun insertCategoria(categoria: categoria) {
        categoriaDao.insertar(categoria)
        firebaseRepository.guardarCategoria(categoria)
    }

    suspend fun sincronizarCategorias() {
        val categoriasFirebase = firebaseRepository.obtenerCategorias()
        categoriasFirebase.forEach { categoria ->
            categoriaDao.insertar(categoria)
        }
    }
}
