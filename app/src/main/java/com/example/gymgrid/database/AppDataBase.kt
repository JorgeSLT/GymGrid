package com.example.gymgrid.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.gymgrid.database.dao.GymDao
import com.example.gymgrid.database.entities.*

@Database(entities = [Rutina::class, Dia::class, Ejercicio::class, RutinaDiaRelacion::class, DiaEjercicioRelacion::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun gymDao(): GymDao
}
