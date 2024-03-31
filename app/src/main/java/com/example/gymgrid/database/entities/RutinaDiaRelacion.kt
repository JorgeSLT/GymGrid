package com.example.gymgrid.database.entities

import androidx.room.Entity

@Entity(primaryKeys = ["rutinaId", "diaId"])
data class RutinaDiaRelacion(
    val rutinaId: Long,
    val diaId: Long
)
