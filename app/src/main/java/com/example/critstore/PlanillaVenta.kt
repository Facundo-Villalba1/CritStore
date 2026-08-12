package com.example.critstore

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "planillas")
data class PlanillaVenta(
    @PrimaryKey(autoGenerate = true)
    val id: Int=0,
    val Uudd: String = UUID.randomUUID().toString(),

    val NombreEvento: String,
    val FechaDesde: String,
    val FechaHasta: String,
    val totalVenta: Int
)