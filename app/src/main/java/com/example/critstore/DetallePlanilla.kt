package com.example.critstore

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "detalle_planillas")
data class DetallePlanilla(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val idPlanilla: Int,
    val Nombre: String,
    val Precio: Int,
    val Ventas: Int,
    val Total: Int
)