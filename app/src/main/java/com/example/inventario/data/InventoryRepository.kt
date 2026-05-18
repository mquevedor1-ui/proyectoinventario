package com.example.inventario.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class InventoryRepository(
    private val bodegaDao: BodegaDao,
    private val productoDao: productoDao,
    private val categoriaDao: categoriaDao,
    private val firebaseRepository: FirebaseRepository
) {
    // Bodegas
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

    // Productos
    fun getProductos(bodegaId: String): Flow<List<producto>> = productoDao.obtenerProductos(bodegaId)

    suspend fun insertProducto(producto: producto) {
        productoDao.insertar(producto)
        // TODO: Sync producto to Firebase if needed
    }

    // Categorias
    val allCategorias: Flow<List<categoria>> = categoriaDao.obtenerCategorias()

    suspend fun insertCategoria(categoria: categoria) {
        categoriaDao.insertar(categoria)
    }
}
