package com.example.inventario.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

import kotlinx.coroutines.tasks.await

class FirebaseRepository {

    // auth

    private val auth =
        FirebaseAuth.getInstance()

    // database

    private val db =

        FirebaseDatabase.getInstance(

            "https://inventarioagr-default-rtdb.firebaseio.com/"
        )

    // referencias

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

    private val facturasRef =
        db.getReference("facturas")

    // =========================
    // LOGIN
    // =========================

    suspend fun login(

        email: String,

        pass: String

    ): Boolean {

        return try {

            auth.signInWithEmailAndPassword(
                email,
                pass
            ).await()

            true

        } catch (e: Exception) {

            e.printStackTrace()

            false
        }
    }

    fun logout() {

        auth.signOut()
    }

    fun getCurrentUser() =

        auth.currentUser

    // =========================
    // BODEGAS
    // =========================

    suspend fun guardarBodega(

        bodega: Bodega

    ): Boolean {

        return try {

            bodegasRef
                .child(bodega.id)
                .setValue(bodega)
                .await()

            true

        } catch (e: Exception) {

            e.printStackTrace()

            false
        }
    }

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

        } catch (e: Exception) {

            e.printStackTrace()

            emptyList()
        }
    }

    suspend fun eliminarBodega(

        id: String

    ): Boolean {

        return try {

            bodegasRef
                .child(id)
                .removeValue()
                .await()

            true

        } catch (e: Exception) {

            e.printStackTrace()

            false
        }
    }

    // =========================
    // USUARIOS
    // =========================

    suspend fun guardarUsuario(

        user: usuario

    ): Boolean {

        return try {

            usuariosRef
                .child(user.user)
                .setValue(user)
                .await()

            true

        } catch (e: Exception) {

            e.printStackTrace()

            false
        }
    }

    suspend fun obtenerUsuarios():

            List<usuario> {

        return try {

            val snapshot =

                usuariosRef
                    .get()
                    .await()

            snapshot.children.mapNotNull {

                it.getValue(
                    usuario::class.java
                )
            }

        } catch (e: Exception) {

            e.printStackTrace()

            emptyList()
        }
    }

    suspend fun eliminarUsuario(

        user: String

    ): Boolean {

        return try {

            usuariosRef
                .child(user)
                .removeValue()
                .await()

            true

        } catch (e: Exception) {

            e.printStackTrace()

            false
        }
    }

    // =========================
    // PRODUCTOS
    // =========================

    suspend fun guardarProducto(

        producto: producto

    ): Boolean {

        return try {

            productosRef
                .child(producto.codigo)
                .setValue(producto)
                .await()

            true

        } catch (e: Exception) {

            e.printStackTrace()

            false
        }
    }

    suspend fun obtenerProductos(

        bodegaId: String

    ): List<producto> {

        return try {

            val snapshot =

                productosRef
                    .get()
                    .await()

            snapshot.children.mapNotNull {

                val p =

                    it.getValue(
                        producto::class.java
                    )

                if (

                    p?.bodegaId ==
                    bodegaId

                ) p else null
            }

        } catch (e: Exception) {

            e.printStackTrace()

            emptyList()
        }
    }

    suspend fun eliminarProducto(

        codigo: String

    ): Boolean {

        return try {

            productosRef
                .child(codigo)
                .removeValue()
                .await()

            true

        } catch (e: Exception) {

            e.printStackTrace()

            false
        }
    }

    // =========================
    // CATEGORIAS
    // =========================

    suspend fun guardarCategoria(

        cat: categoria

    ): Boolean {

        return try {

            categoriasRef
                .child(cat.id.toString())
                .setValue(cat)
                .await()

            true

        } catch (e: Exception) {

            e.printStackTrace()

            false
        }
    }

    suspend fun obtenerCategorias():

            List<categoria> {

        return try {

            val snapshot =

                categoriasRef
                    .get()
                    .await()

            snapshot.children.mapNotNull {

                it.getValue(
                    categoria::class.java
                )
            }

        } catch (e: Exception) {

            e.printStackTrace()

            emptyList()
        }
    }

    suspend fun eliminarCategoria(

        id: Int

    ): Boolean {

        return try {

            categoriasRef
                .child(id.toString())
                .removeValue()
                .await()

            true

        } catch (e: Exception) {

            e.printStackTrace()

            false
        }
    }

    // =========================
    // ENTRADAS
    // =========================

    suspend fun guardarEntrada(

        entrada: Entrada

    ): Boolean {

        return try {

            entradasRef
                .child(entrada.id.toString())
                .setValue(entrada)
                .await()

            true

        } catch (e: Exception) {

            e.printStackTrace()

            false
        }
    }

    suspend fun obtenerEntradas():

            List<Entrada> {

        return try {

            val snapshot =

                entradasRef
                    .get()
                    .await()

            snapshot.children.mapNotNull {

                it.getValue(
                    Entrada::class.java
                )
            }

        } catch (e: Exception) {

            e.printStackTrace()

            emptyList()
        }
    }

    suspend fun eliminarEntrada(

        id: Int

    ): Boolean {

        return try {

            entradasRef
                .child(id.toString())
                .removeValue()
                .await()

            true

        } catch (e: Exception) {

            e.printStackTrace()

            false
        }
    }

    // =========================
    // SALIDAS
    // =========================

    suspend fun guardarSalida(

        salida: Salida

    ): Boolean {

        return try {

            salidasRef
                .child(salida.id.toString())
                .setValue(salida)
                .await()

            true

        } catch (e: Exception) {

            e.printStackTrace()

            false
        }
    }

    suspend fun obtenerSalidas():

            List<Salida> {

        return try {

            val snapshot =

                salidasRef
                    .get()
                    .await()

            snapshot.children.mapNotNull {

                it.getValue(
                    Salida::class.java
                )
            }

        } catch (e: Exception) {

            e.printStackTrace()

            emptyList()
        }
    }

    suspend fun eliminarSalida(

        id: Int

    ): Boolean {

        return try {

            salidasRef
                .child(id.toString())
                .removeValue()
                .await()

            true

        } catch (e: Exception) {

            e.printStackTrace()

            false
        }
    }

    // =========================
    // FACTURAS
    // =========================

    suspend fun guardarFactura(

        factura: Factura

    ): Boolean {

        return try {

            facturasRef
                .child(factura.id.toString())
                .setValue(factura)
                .await()

            true

        } catch (e: Exception) {

            e.printStackTrace()

            false
        }
    }

    suspend fun obtenerFacturas():

            List<Factura> {

        return try {

            val snapshot =

                facturasRef
                    .get()
                    .await()

            snapshot.children.mapNotNull {

                it.getValue(
                    Factura::class.java
                )
            }

        } catch (e: Exception) {

            e.printStackTrace()

            emptyList()
        }
    }

    suspend fun eliminarFactura(

        id: Int

    ): Boolean {

        return try {

            facturasRef
                .child(id.toString())
                .removeValue()
                .await()

            true

        } catch (e: Exception) {

            e.printStackTrace()

            false
        }
    }
}
