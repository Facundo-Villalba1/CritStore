package com.example.critstore

import androidx.room.*

@Dao
interface MaterialesDao {

    @Insert(
        onConflict = OnConflictStrategy.REPLACE
    )
    suspend fun insertarMaterial(
        material: Materiales
    )

    @Query(
        "SELECT * FROM materiales ORDER BY Marca, Color"
    )
    suspend fun obtenerMateriales():
            List<Materiales>

    @Query(
        "UPDATE materiales SET Cantidad = :cantidad WHERE uuid = :uuid"
    )
    suspend fun actualizarCantidad(
        uuid: String,
        cantidad: Int
    )

    @Delete
    suspend fun eliminarMaterial(
        material: Materiales
    )

    @Query(
        "DELETE FROM materiales"
    )
    suspend fun borrarMateriales()
}