package com.example.gymgrid.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

//Clase para almacenar datos en la BBDD
//Compuesta por un ID autogenerado y por todos los elementos de un Ejercicio
@Entity
data class Ejercicio(
    @PrimaryKey(autoGenerate = true) val ejercicioId: Long = 0,
    val titulo: String,
    val descripcion: String,
    val imagen: String,
    val tieneRepeticiones: Boolean,
    val repeticiones: Int?
)
