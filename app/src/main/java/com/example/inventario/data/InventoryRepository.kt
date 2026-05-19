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

    suspend fun eliminarProducto(producto: producto) {
        productoDao.eliminar(producto)
        firebaseRepository.eliminarProducto(producto.codigo) // Usar codigo como key en Firebase
    }

    suspend fun sincronizarProductos(bodegaId: String) {
        val productosFirebase = firebaseRepository.obtenerProductos(bodegaId)
        productosFirebase.forEach { producto ->
            productoDao.insertar(producto)
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
