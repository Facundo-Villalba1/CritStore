package com.example.critstore

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Materiales")
data class Materiales (
    @PrimaryKey
    val uuid: String = java.util.UUID.randomUUID().toString(),
    val Cantidad: Int = 0,
    val Color: String = "",
    val Marca: String = ""
)
