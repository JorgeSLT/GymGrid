package com.example.gymgrid.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Rutina(
    @PrimaryKey(autoGenerate = true) val rutinaId: Long = 0,
    val nombre: String,
    val descripcion: String?
)
