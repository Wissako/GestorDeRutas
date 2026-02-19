package com.example.gestorderutas.data // Ajusta el paquete si es necesario

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.gestorderutas.model.Ruta
import com.example.gestorderutas.model.PuntoRuta
import com.example.gestorderutas.model.Waypoint
import com.example.gestorderutas.model.GestorRutasDao

// Definimos las entidades y la versión de la base de datos
@Database(entities = [Ruta::class, PuntoRuta::class, Waypoint::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun gestorRutasDao(): GestorRutasDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            // Si la instancia ya existe, la devuelve. Si no, la crea de forma segura.
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "gestor_rutas_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}