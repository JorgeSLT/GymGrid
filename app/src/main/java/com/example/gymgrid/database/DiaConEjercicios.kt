package com.example.gymgrid.database

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.example.gymgrid.database.entities.Dia
import com.example.gymgrid.database.entities.Ejercicio
import com.example.gymgrid.database.entities.DiaEjercicioRelacion

//Clase para almacenar datos en la BBDD
//Contiene un dia y una lista con los ejercicios de dicho dia
//Esta clase no se considera una entidad
data class DiaConEjercicios(
    @Embedded val dia: Dia,
    @Relation(
        parentColumn = "diaId",
        entityColumn = "ejercicioId",
        associateBy = Junction(DiaEjercicioRelacion::class)
    )
    val ejercicios: List<Ejercicio>
)
