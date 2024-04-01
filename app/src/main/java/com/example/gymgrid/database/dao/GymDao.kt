package com.example.gymgrid.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.gymgrid.database.entities.*

@Dao
interface GymDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertarRutina(rutina: Rutina): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertarDia(dia: Dia): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertarEjercicio(ejercicio: Ejercicio): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertarRutinaDiaRelacion(rutinaDiaRelacion: RutinaDiaRelacion)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertarDiaEjercicioRelacion(diaEjercicioRelacion: DiaEjercicioRelacion)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertarEjercicios(ejercicios: List<Ejercicio>): List<Long>

    @Query("SELECT ejercicioId FROM Ejercicio WHERE titulo = :titulo")
    fun obtenerIdEjercicioPorTitulo(titulo: String): Long

    @Query("SELECT diaId FROM Dia WHERE nombreDia = :nombreDia")
    fun obtenerIdDiaPorNombre(nombreDia: String): Long

    @Query("SELECT * FROM Ejercicio")
    fun obtenerTodosLosEjercicios(): LiveData<List<Ejercicio>>

}
