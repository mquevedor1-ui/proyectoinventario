package com.example.inventario.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

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
    version = 6,
    exportSchema = false
)
abstract class appdatabase : RoomDatabase() {
    abstract fun usuarioDao(): usuarioDao
    abstract fun bodegaDao(): BodegaDao
    abstract fun productoDao(): productoDao
    abstract fun categoriaDao(): categoriaDao
    abstract fun entradaDao(): EntradaDao
    abstract fun salidaDao(): SalidaDao
    abstract fun facturaDao(): FacturaDao



companion object {
        @Volatile
        private var INSTANCE: appdatabase? = null

        fun getDatabase(context: Context): appdatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    appdatabase::class.java,
                    "app_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
