package com.example.inventario.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [usuario::class, Bodega::class], version = 3, exportSchema = false)
abstract class appdatabase : RoomDatabase() {
    abstract fun usuarioDao(): usuarioDao
    abstract fun bodegaDao(): BodegaDao

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
