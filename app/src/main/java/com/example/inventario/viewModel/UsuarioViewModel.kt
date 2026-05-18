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
        actualizarListaCompleta()
    }

    private fun actualizarListaCompleta() {
        viewModelScope.launch {
            // 1. Cargar lo que haya localmente para respuesta rápida
            _usuarios.value = dao.obtenerTodos()
            
            // 2. Intentar traer de la nube
            try {
                val usuariosNube = firebaseRepo.obtenerUsuarios()
                if (usuariosNube.isNotEmpty()) {
                    usuariosNube.forEach { userNube ->
                        // Protección contra nulos que Firebase pueda traer de registros antiguos
                        val nombre = (userNube.user as String?).orEmpty()
                        if (nombre.isEmpty()) return@forEach

                        val local = dao.existe(nombre)
                        if (local == null) {
                            // Si no existe, lo insertamos con id=0 para que Room genere un ID único local
                            dao.insertar(userNube.copy(id = 0))
                        } else {
                            // Si existe, actualizamos pass y rol pero mantenemos el ID local para evitar conflictos
                            dao.actualizar(userNube.copy(id = local.id))
                        }
                    }
                    // 3. Actualizar la lista observable con los datos sincronizados
                    _usuarios.value = dao.obtenerTodos()
                }
            } catch (e: Exception) {
                e.printStackTrace()
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
            if (usuarioViejo.user != nuevoUser) {
                // Si cambia el nombre, eliminamos el anterior y creamos uno nuevo
                dao.eliminar(usuarioViejo)
                firebaseRepo.eliminarUsuario(usuarioViejo.user)

                val nuevoUsuario = usuario(
                    user = nuevoUser,
                    pass = nuevaPass,
                    rol = nuevoRol
                )
                dao.insertar(nuevoUsuario)
                firebaseRepo.guardarUsuario(nuevoUsuario)
            } else {
                // Si el nombre es el mismo, actualizamos el registro existente usando su ID
                val usuarioActualizado = usuarioViejo.copy(
                    pass = nuevaPass,
                    rol = nuevoRol
                )
                dao.actualizar(usuarioActualizado)
                firebaseRepo.guardarUsuario(usuarioActualizado)
            }
            _usuarios.value = dao.obtenerTodos()
        }
    }
}