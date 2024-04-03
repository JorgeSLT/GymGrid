package com.example.gymgrid.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.gymgrid.database.dao.GymDao
import com.example.gymgrid.database.entities.*

//Clase abstracta que sirve como Base de Datos
//Extiende a RoomDataBase
//"entities"    -> Entidades que se guardan en la BBDD
//"version"     -> Version de la BBDD
//"exportSchema -> No se exporta como archivo
@Database(entities = [Ejercicio::class, Rutina::class, Dia::class, RutinaDiaRelacion::class, DiaEjercicioRelacion::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    //Funcion para referir al objeto GymDAO
    abstract fun gymDao(): GymDao

    //Objeto Singleton para asegurar que solo haya una BBDD en la aplicacion
    companion object {
        //Con Volatile se asegura que el cambio de la variable sea automaticamente visible para
        //todos los hilos.
        @Volatile
        private var INSTANCE: AppDatabase? = null

        //Funcion para comprobar si existe una instancia y devolverla
        //Si no es asi, genera la instancia en un solo hilo y la devuelve.
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "gym_database"
                )
                    //Estrategia de migración no implementada
                    //Si se actualiza la version los datos de la BBDD se borraran
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
