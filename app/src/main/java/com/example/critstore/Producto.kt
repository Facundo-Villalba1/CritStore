package com.example.critstore

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "productos")
data class Producto(

    @PrimaryKey
    val uuid: String = java.util.UUID.randomUUID().toString(),
    val Nombre: String,
    val Cantidad: Int,
    val Precio: Int,
    val Tipo: String
)