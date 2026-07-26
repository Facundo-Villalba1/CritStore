package com.example.critstore

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "productos")
data class Producto(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val Nombre: String,
    val Cantidad: Int,
    val Precio: Int,
    val Tipo: String
)