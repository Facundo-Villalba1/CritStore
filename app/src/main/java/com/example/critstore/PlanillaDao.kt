package com.example.critstore

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

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
    @Update
    suspend fun actualizarPlanilla(
        planilla: PlanillaVenta
    )
   /* @Update
    suspend fun actualizarDetalle(
        detalle: DetallePlanilla
    )
*/
    @Query(
        "DELETE FROM detalle_planillas WHERE idPlanilla = :id"
    )
    suspend fun eliminarDetallesPlanilla(
        id: Int
    )
    @Query(
        "SELECT * FROM planillas WHERE id = :id LIMIT 1"
    )
    suspend fun obtenerPlanillaPorId(
        id: Int
    ): PlanillaVenta
}