package com.example.gymgrid.database.entities

import androidx.room.Entity

@Entity(primaryKeys = ["diaId", "ejercicioId"])
data class DiaEjercicioRelacion(
    val diaId: Long,
    val ejercicioId: Long
)
