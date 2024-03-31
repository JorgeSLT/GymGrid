package com.example.gymgrid.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.gymgrid.database.dao.GymDao
import com.example.gymgrid.database.entities.Ejercicio
import com.example.gymgrid.database.entities.Rutina
import com.example.gymgrid.database.entities.RutinaDiaRelacion
import com.example.gymgrid.database.entities.Dia
import com.example.gymgrid.database.entities.DiaEjercicioRelacion

// Asegúrate de actualizar los parámetros de la anotación @Database según tus entidades y versión
@Database(entities = [Ejercicio::class, Rutina::class, Dia::class, RutinaDiaRelacion::class, DiaEjercicioRelacion::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun gymDao(): GymDao

    companion object {
        // Volatile para asegurar la visibilidad de cambios entre hilos
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            // Retorna la instancia si ya existe, sino crea una nueva
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "gym_database" // Nombre de la base de datos
                )
                    // Estrategia de migración. Por simplicidad, se está omitiendo, pero considera implementar migraciones reales.
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                // Retorna la instancia creada
                instance
            }
        }
    }
}
