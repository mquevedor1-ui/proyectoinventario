package com.example.inventario.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope

import com.example.inventario.data.FirebaseRepository
import com.example.inventario.data.appdatabase
import com.example.inventario.data.usuario

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class UsuarioViewModel(application: Application) : AndroidViewModel(application) {

    private val dao = appdatabase.getDatabase(application).usuarioDao()

    private val firebaseRepo = FirebaseRepository()

    private val _usuarios = MutableStateFlow<List<usuario>>(emptyList())

    val usuarios: StateFlow<List<usuario>> = _usuarios

    init {

        sincronizarUsuarios()
    }

    // SINCRONIZAR USUARIOS
    private fun sincronizarUsuarios() {

        viewModelScope.launch {

            try {

                val usuariosNube = firebaseRepo.obtenerUsuarios()

                usuariosNube.forEach { user ->

                    val existe = dao.existe(user.user)

                    if (existe == null) {

                        dao.insertar(user)
                    }
                }

                // CREAR ADMIN POR DEFECTO
                val listaLocal = dao.obtenerTodos()

                if (listaLocal.isEmpty() && usuariosNube.isEmpty()) {

                    val admin = usuario(

                        user = "admin",

                        pass = "1234",

                        rol = "admin"
                    )

                    dao.insertar(admin)

                    firebaseRepo.guardarUsuario(admin)
                }

                _usuarios.value = dao.obtenerTodos()

            } catch (e: Exception) {

                _usuarios.value = dao.obtenerTodos()
            }
        }
    }

    // LOGIN
    suspend fun login(
        user: String,
        pass: String
    ): usuario? {

        return dao.login(user, pass)
    }

    // REGISTRO NORMAL
    suspend fun registrar(
        user: String,
        pass: String
    ): Boolean {

        val existe = dao.existe(user)

        if (existe != null) {

            return false
        }

        val nuevoUser = usuario(

            user = user,

            pass = pass,

            rol = "usuario"
        )

        dao.insertar(nuevoUser)

        firebaseRepo.guardarUsuario(nuevoUser)

        _usuarios.value = dao.obtenerTodos()

        return true
    }

    // CREAR USUARIO DESDE ADMIN
    fun crearUsuario(
        user: String,
        pass: String,
        rol: String
    ) {

        viewModelScope.launch {

            val existe = dao.existe(user)

            if (existe == null) {

                val nuevoUser = usuario(

                    user = user,

                    pass = pass,

                    rol = rol
                )

                dao.insertar(nuevoUser)

                firebaseRepo.guardarUsuario(nuevoUser)

                _usuarios.value = dao.obtenerTodos()
            }
        }
    }

    // ELIMINAR USUARIO
    fun eliminarUsuario(user: usuario) {

        viewModelScope.launch {

            dao.eliminar(user)

            firebaseRepo.eliminarUsuario(user.user)

            _usuarios.value = dao.obtenerTodos()
        }
    }

    // EDITAR USUARIO
    fun editarUsuario(
        usuarioViejo: usuario,
        nuevoUser: String,
        nuevaPass: String,
        nuevoRol: String
    ) {

        viewModelScope.launch {

            // SI CAMBIÓ EL NOMBRE
            if (usuarioViejo.user != nuevoUser) {

                dao.eliminar(usuarioViejo)

                firebaseRepo.eliminarUsuario(usuarioViejo.user)
            }

            val usuarioActualizado = usuario(

                user = nuevoUser,

                pass = nuevaPass,

                rol = nuevoRol
            )

            dao.insertar(usuarioActualizado)

            firebaseRepo.guardarUsuario(usuarioActualizado)

            _usuarios.value = dao.obtenerTodos()
        }
    }
}