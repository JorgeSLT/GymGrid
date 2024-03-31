package com.example.gymgrid.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Dia(
    @PrimaryKey(autoGenerate = true) val diaId: Long = 0,
    val nombreDia: String
)
