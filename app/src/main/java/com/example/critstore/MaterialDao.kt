package com.example.critstore

import androidx.room.*

@Dao
interface MaterialDao {
    @Query("SELECT * FROM materiales")
    suspend fun obtenerMateriales(): List<Materiales>
    @Query("SELECT * FROM materiales WHERE uuid = :uuid LIMIT 1")
    suspend fun obtenerMaterial(
        uuid: String
    ): Materiales?
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarMaterial(
        material: Materiales
    )
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarMateriales(
        materiales: List<Materiales>
    )
    @Update
    suspend fun actualizarMaterial(
        material: Materiales
    )
    @Query(
        "UPDATE materiales SET cantidad = :cantidad WHERE uuid = :uuid"
    )
    suspend fun actualizarCantidad(
        uuid: String,
        cantidad: Int
    )
    @Delete
    suspend fun eliminarMaterial(
        material: Materiales
    )
}