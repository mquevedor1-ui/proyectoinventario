package com.example.inventario.data
import android.content.Context

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(

    entities = [

        usuario::class,

        Bodega::class,

        producto::class,

        categoria::class,

        Entrada::class,

        Salida::class,

        Factura::class
    ],

    version = 15,

    exportSchema = false
)

abstract class appdatabase :
    RoomDatabase() {

    // usuarios

    abstract fun usuarioDao():
            usuarioDao

    // bodegas

    abstract fun bodegaDao():
            BodegaDao

    // productos

    abstract fun productoDao():
            productoDao

    // categorias

    abstract fun categoriaDao():
            categoriaDao

    // entradas

    abstract fun entradaDao():
            EntradaDao

    // salidas

    abstract fun salidaDao():
            SalidaDao

    // facturas

    abstract fun facturaDao():
            FacturaDao

    companion object {

        @Volatile

        private var INSTANCE:
                appdatabase? = null

        fun getDatabase(

            context: Context

        ): appdatabase {

            return INSTANCE
                ?: synchronized(this) {

                    val instance =

                        Room.databaseBuilder(

                            context.applicationContext,

                            appdatabase::class.java,

                            "app_database"
                        )

                            // recrea db si cambia version

                            .fallbackToDestructiveMigration()

                            .build()

                    INSTANCE = instance

                    instance
                }
        }
    }
}