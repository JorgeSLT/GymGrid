package com.example.gymgrid.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.gymgrid.database.entities.*

@Dao
interface GymDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarRutina(rutina: Rutina)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarDia(dia: Dia)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarEjercicio(ejercicio: Ejercicio)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarRutinaDiaRelacion(rutinaDiaRelacion: RutinaDiaRelacion)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarDiaEjercicioRelacion(diaEjercicioRelacion: DiaEjercicioRelacion)

    @Transaction
    @Query("SELECT Dia.* FROM Dia INNER JOIN RutinaDiaRelacion ON Dia.diaId=RutinaDiaRelacion.diaId WHERE RutinaDiaRelacion.rutinaId=:rutinaId")
    fun obtenerDiasParaRutina(rutinaId: Long): LiveData<List<Dia>>

    @Transaction
    @Query("SELECT Ejercicio.* FROM Ejercicio INNER JOIN DiaEjercicioRelacion ON Ejercicio.ejercicioId=DiaEjercicioRelacion.ejercicioId WHERE DiaEjercicioRelacion.diaId=:diaId")
    fun obtenerEjerciciosParaDia(diaId: Long): LiveData<List<Ejercicio>>
}
