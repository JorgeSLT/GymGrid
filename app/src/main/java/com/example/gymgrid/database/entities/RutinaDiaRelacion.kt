package com.example.gymgrid.database.entities

import androidx.room.Entity

//Clase para almacenar datos en la BBDD
//Simula la relacion entre la clase Rutina y Dia
//Compuesta por un ID de Rutina y por otro de Dia, ambos siendo claves primarias
@Entity(primaryKeys = ["rutinaId", "diaId"])
data class RutinaDiaRelacion(
    val rutinaId: Long,
    val diaId: Long
)
