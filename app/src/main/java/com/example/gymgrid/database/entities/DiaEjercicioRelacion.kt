package com.example.gymgrid.database.entities

import androidx.room.Entity

//Clase para almacenar datos en la BBDD
//Simula la relacion entre la clase Dia y Ejercicio
//Compuesta por un ID de Dia y por otro de Ejercicio, ambos siendo claves primarias
@Entity(primaryKeys = ["diaId", "ejercicioId"])
data class DiaEjercicioRelacion(
    val diaId: Long,
    val ejercicioId: Long
)
