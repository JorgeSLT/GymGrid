package com.example.gymgrid.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Ejercicio(
    @PrimaryKey(autoGenerate = true) val ejercicioId: Long = 0,
    val titulo: String,
    val descripcion: String,
    val imagen: String,
    val tieneRepeticiones: Boolean,
    val repeticiones: Int?
)
