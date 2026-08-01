package com.example.critstore

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "planillas")
data class PlanillaVenta(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val NombreEvento: String,
    val FechaDesde: String,
    val FechaHasta: String,
    val TotalVenta: Int
)