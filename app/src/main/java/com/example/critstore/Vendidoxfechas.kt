package com.example.critstore

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import java.net.URL

suspend fun obtenerVentasPorFecha(
    fechaDesde: String,
    fechaHasta: String
): List<ReportexFecha> {
    return withContext(Dispatchers.IO) {
        try {
            val url =
                "https://script.google.com/macros/s/AKfycbx7rwGp8kC21GdfUcN40JZaqtfj6lnpnNiAue2N-bz6_w3N7NGFBhczHf-JW9pufV7Ueg/exec?tabla=DetallePlanilla"
            val respuesta =
                URL(url).readText()
            android.util.Log.d(
                "GOOGLE_RESPUESTA",
                respuesta
            )
            val json =
                JSONArray(respuesta)
            val mapa =
                mutableMapOf<Int, ReportexFecha>()
            for (i in 0 until json.length()) {
                val item =
                    json.getJSONObject(i)
                val idPlanilla =
                    item.optString("idPlanilla")
                        .toIntOrNull()
                        ?: 0
                val nombreEvento =
                    item.optString("nombreEvento")
                val fecha =
                    item.optString("fechaDesde")
                val totalVenta =
                    item.optString("totalVenta")
                        .toIntOrNull()
                        ?: 0
                if (!mapa.containsKey(idPlanilla)) {
                    mapa[idPlanilla] =
                        ReportexFecha(
                            idPlanilla = idPlanilla,
                            nombreEvento = nombreEvento,
                            fechaDesde = fecha,
                            totalVenta = totalVenta
                        )
                }
            }
            mapa.values.toList()
        } catch (e: Exception) {
            android.util.Log.e(
                "ERROR_GOOGLE",
                e.toString()
            )
            emptyList()
        }
    }
}