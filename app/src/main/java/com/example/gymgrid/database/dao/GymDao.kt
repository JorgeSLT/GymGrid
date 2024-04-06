package com.example.gymgrid.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.gymgrid.database.entities.*
import com.example.gymgrid.database.DiaConEjercicios

//DAO <DATA ACCESS OBJECT>

//Aqui estan definidas las funciones para acceder a mi base de datos local

//El comando onConflict que aparece en algunas funciones sirve para reemplazar
//un objeto antiguo por el nuevo en caso de que coincidan en algun valor unico
@Dao
interface GymDao {
    //Inserta un nuevo objeto Rutina en la base de datos
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertarRutina(rutina: Rutina): Long

    //Inserta un nuevo objeto Dia en la base de datos
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertarDia(dia: Dia): Long

    //Inserta un nuevo objeto Ejercicio en la base de datos
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertarEjercicio(ejercicio: Ejercicio): Long

    //Inserta una relacion entre Rutina y Dia en la base de datos
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertarRutinaDiaRelacion(rutinaDiaRelacion: RutinaDiaRelacion)

    //Inserta una relacion entre Dia y Ejercicio en la base de datos
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertarDiaEjercicioRelacion(diaEjercicioRelacion: DiaEjercicioRelacion)

    //Inserta una lista de ejercicios en la base de datos y devuelve una lista con sus IDs
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertarEjercicios(ejercicios: List<Ejercicio>): List<Long>

    //Realiza una consulta para obtener el ID de un ejercicio dado su nombre
    @Query("SELECT ejercicioId FROM Ejercicio WHERE titulo = :titulo")
    fun obtenerIdEjercicioPorTitulo(titulo: String): Long

    //Realiza una consulta para obtener el ID de un dia dado su nombre
    @Query("SELECT diaId FROM Dia WHERE nombreDia = :nombreDia")
    fun obtenerIdDiaPorNombre(nombreDia: String): Long

    //Realiza una consulta para obtener una lista con todos los ejercicios guardados
    @Query("SELECT * FROM Ejercicio")
    fun obtenerTodosLosEjercicios(): LiveData<List<Ejercicio>>

    //Realiza una consulta para obtener una rutina dado su objetivo y su duracion
    @Query("SELECT * FROM Rutina WHERE objetivo = :objetivo AND duracionDias = :duracionDias")
    fun getRutinasPorObjetivoYDias(objetivo: String, duracionDias: Int): LiveData<List<Rutina>>

    //Realiza una consulta para obtener el id de una rutina dado su objetivo y su duracion
    @Query("SELECT rutinaId FROM Rutina WHERE objetivo = :objetivo AND duracionDias = :duracionDias LIMIT 1")
    fun getRutinaIdPorObjetivoYDias(objetivo: String, duracionDias: Int): LiveData<Long?>

    //Realiza una consulta para obtener una lista de objetos DiaConEjercicios para una rutina dado su id
    @Query("""SELECT * FROM Dia INNER JOIN RutinaDiaRelacion ON Dia.diaId = RutinaDiaRelacion.diaId WHERE RutinaDiaRelacion.rutinaId = :rutinaId""")
    fun obtenerDiasConEjerciciosPorRutinaId(rutinaId: Long): LiveData<List<DiaConEjercicios>>

    //Realiza una consulta para obtener todas las rutinas guardadas
    @Query("SELECT * FROM Rutina")
    fun obtenerTodasLasRutinas(): LiveData<List<Rutina>>
}
