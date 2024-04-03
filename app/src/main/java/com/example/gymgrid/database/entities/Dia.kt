package com.example.gymgrid.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

//Clase para almacenar datos en la BBDD
//Compuesta por un ID que se genera automaticamente, un nombre y un dia de la semana
@Entity
data class Dia(
    @PrimaryKey(autoGenerate = true) val diaId: Long = 0,
    val nombreDia: String,
    val diaSemana: String
)
