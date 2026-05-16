package com.example.inventario.data

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.tasks.await

class FirebaseRepository {

    private val db = FirebaseDatabase.getInstance(
        "https://inventarioagr-default-rtdb.firebaseio.com/"
    )

    private val bodegasRef = db.getReference("bodegas")

    private val usuariosRef = db.getReference("usuarios")

    // BODEGAS
    fun guardarBodega(bodega: Bodega) {

        bodegasRef.child(bodega.id).setValue(bodega)
    }

    suspend fun obtenerBodegas(): List<Bodega> {

        return try {

            val snapshot = bodegasRef.get().await()

            snapshot.children.mapNotNull { child: DataSnapshot ->

                child.getValue(Bodega::class.java)
            }

        } catch (e: Exception) {

            emptyList()
        }
    }

    // ELIMINAR BODEGA
    fun eliminarBodega(id: String) {
        bodegasRef.child(id).removeValue()
    }

    // USUARIOS
    fun guardarUsuario(user: usuario) {

        usuariosRef
            .child(user.user)
            .setValue(user)

            .addOnSuccessListener {

                println("USUARIO GUARDADO FIREBASE")
            }

            .addOnFailureListener {

                println("ERROR FIREBASE: ${it.message}")
            }
    }

    suspend fun obtenerUsuarios(): List<usuario> {

        return try {

            val snapshot = usuariosRef.get().await()

            snapshot.children.mapNotNull { child: DataSnapshot ->

                child.getValue(usuario::class.java)
            }

        } catch (e: Exception) {

            emptyList()
        }
    }

    // ELIMINAR USUARIO
    fun eliminarUsuario(user: String) {

        usuariosRef
            .child(user)
            .removeValue()
    }
}