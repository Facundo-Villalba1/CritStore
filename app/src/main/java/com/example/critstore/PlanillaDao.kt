package com.example.critstore

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface PlanillaDao {
    @Insert
    suspend fun insertarPlanilla(
        planilla: PlanillaVenta
    ): Long
    @Insert
    suspend fun insertarDetalle(
        detalle: DetallePlanilla
    )
    @Query(
        "SELECT * FROM planillas ORDER BY id DESC"
    )
    suspend fun obtenerPlanillas():
            List<PlanillaVenta>
    @Query(
        "SELECT * FROM detalle_planillas WHERE idPlanilla = :id"
    )
    suspend fun obtenerDetallePlanilla(
        id: Int
    ): List<DetallePlanilla>
}