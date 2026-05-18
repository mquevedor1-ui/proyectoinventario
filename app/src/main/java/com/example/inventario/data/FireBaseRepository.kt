package com.example.inventario.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase

import kotlinx.coroutines.tasks.await

class FirebaseRepository {

    // auth
    private val auth = FirebaseAuth.getInstance()

    // firebase
    private val db =
        FirebaseDatabase.getInstance(
            "https://inventarioagr-default-rtdb.firebaseio.com/"
        )

    // =========================
    // AUTHENTICATION
    // =========================

    suspend fun login(email: String, pass: String): Boolean {
        return try {
            auth.signInWithEmailAndPassword(email, pass).await()
            true
        } catch (e: Exception) {
            false
        }
    }

    fun logout() {
        auth.signOut()
    }

    fun getCurrentUser() = auth.currentUser

    // refs
    private val bodegasRef =
        db.getReference("bodegas")

    private val usuariosRef =
        db.getReference("usuarios")

    private val productosRef =
        db.getReference("productos")

    private val categoriasRef =
        db.getReference("categorias")

    private val entradasRef =
        db.getReference("entradas")

    private val salidasRef =
        db.getReference("salidas")

    // =========================
    // BODEGAS
    // =========================

    // guardar
    fun guardarBodega(

        bodega: Bodega

    ) {

        bodegasRef
            .child(bodega.id)
            .setValue(bodega)
    }

    // obtener
    suspend fun obtenerBodegas():
            List<Bodega> {

        return try {

            val snapshot =
                bodegasRef
                    .get()
                    .await()

            snapshot.children.mapNotNull {

                it.getValue(
                    Bodega::class.java
                )
            }

        } catch (_: Exception) {

            emptyList()
        }
    }

    // eliminar
    fun eliminarBodega(

        id: String

    ) {

        bodegasRef
            .child(id)
            .removeValue()
    }

    // =========================
    // USUARIOS
    // =========================

    // guardar
    fun guardarUsuario(
        user: usuario
    ) {
        usuariosRef
            .child(user.user)
            .setValue(user)
    }

    // obtener
    suspend fun obtenerUsuarios(): List<usuario> {
        return try {
            val snapshot = usuariosRef.get().await()
            snapshot.children.mapNotNull { it.getValue(usuario::class.java) }
        } catch (_: Exception) {
            emptyList()
        }
    }

    // eliminar
    fun eliminarUsuario(user: String) {
        usuariosRef.child(user).removeValue()
    }

    // =========================
    // PRODUCTOS
    // =========================

    // guardar
    fun guardarProducto(producto: producto) {
        productosRef
            .child(producto.id.toString())
            .setValue(producto)
    }

    // obtener por bodega
    suspend fun obtenerProductos(bodegaId: String): List<producto> {
        return try {
            val snapshot = productosRef.get().await()
            snapshot.children.mapNotNull { 
                val p = it.getValue(producto::class.java)
                if (p?.bodegaId == bodegaId) p else null
            }
        } catch (_: Exception) {
            emptyList()
        }
    }

    // eliminar
    fun eliminarProducto(id: Int) {
        productosRef
            .child(id.toString())
            .removeValue()
    }

    // =========================
    // CATEGORIAS
    // =========================

    fun guardarCategoria(cat: categoria) {
        categoriasRef
            .child(cat.id.toString())
            .setValue(cat)
    }

    suspend fun obtenerCategorias(): List<categoria> {
        return try {
            val snapshot = categoriasRef.get().await()
            snapshot.children.mapNotNull { it.getValue(categoria::class.java) }
        } catch (_: Exception) {
            emptyList()
        }
    }

    fun eliminarCategoria(id: Int) {
        categoriasRef.child(id.toString()).removeValue()
    }

    // =========================
    // ENTRADAS
    // =========================

    fun guardarEntrada(entrada: Entrada) {
        entradasRef
            .child(entrada.id.toString())
            .setValue(entrada)
    }

    suspend fun obtenerEntradas(): List<Entrada> {
        return try {
            val snapshot = entradasRef.get().await()
            snapshot.children.mapNotNull { it.getValue(Entrada::class.java) }
        } catch (_: Exception) {
            emptyList()
        }
    }

    fun eliminarEntrada(id: Int) {
        entradasRef.child(id.toString()).removeValue()
    }

    // =========================
    // SALIDAS
    // =========================

    fun guardarSalida(salida: Salida) {
        salidasRef
            .child(salida.id.toString())
            .setValue(salida)
    }

    suspend fun obtenerSalidas(): List<Salida> {
        return try {
            val snapshot = salidasRef.get().await()
            snapshot.children.mapNotNull { it.getValue(Salida::class.java) }
        } catch (_: Exception) {
            emptyList()
        }
    }

    fun eliminarSalida(id: Int) {
        salidasRef.child(id.toString()).removeValue()
    }
}
