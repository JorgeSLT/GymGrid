package com.example.gymgrid.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

//Clase para almacenar datos en la BBDD
//Compuesta por un ID que se genera automaticamente, un nombre, una descripcion, un objetivo y una duracion
@Entity
data class Rutina(
    @PrimaryKey(autoGenerate = true) val rutinaId: Long = 0,
    val nombre: String,
    val descripcion: String?,
    val objetivo: String,
    val duracionDias: Int
)
