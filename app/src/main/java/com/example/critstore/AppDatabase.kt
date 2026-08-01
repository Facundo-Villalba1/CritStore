package com.example.critstore

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [
        Producto::class,
        PlanillaVenta::class,
        DetallePlanilla::class
    ],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun productoDao(): ProductoDao
    abstract fun planillaDao(): PlanillaDao
}