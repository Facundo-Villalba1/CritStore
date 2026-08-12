package com.example.critstore

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import java.net.URL

suspend fun obtenerMayorVentaEvento(): List<ReporteEvento> {
    return withContext(Dispatchers.IO) {
        try {
            val url =
                "https://script.google.com/macros/s/AKfycbx7rwGp8kC21GdfUcN40JZaqtfj6lnpnNiAue2N-bz6_w3N7NGFBhczHf-JW9pufV7Ueg/exec?tabla=DetallePlanilla"
            val respuesta = URL(url).readText()
            android.util.Log.d(
                "JSON_GOOGLE",
                respuesta
            )
            val json = JSONArray(respuesta)
                       val eventos =
                mutableMapOf<String, MutableMap<String, Int>>()
            for (i in 0 until json.length()) {
                val item = json.getJSONObject(i)
                val evento =
                    item.optString("nombreEvento")
                val producto =
                    item.optString("nombre")
                val cantidad =
                    item.optInt("cantidad", 0)
                if (evento.isNotEmpty() && producto.isNotEmpty()) {
                    val productosEvento =
                        eventos.getOrPut(evento) {
                            mutableMapOf()
                        }
                    productosEvento[producto] =
                        (productosEvento[producto] ?: 0) + cantidad
                }
            }
            eventos.map { (evento, productos) ->
                val mayorProducto =
                    productos.maxByOrNull {
                        it.value
                    }
                ReporteEvento(
                    nombreEvento = evento,
                    productoMasVendido =
                        mayorProducto?.key ?: "",
                    producto =
                        mayorProducto?.key ?: "",
                    cantidadTotal =
                        mayorProducto?.value ?: 0
                )
            }
                .sortedByDescending {
                    it.cantidadTotal
                }
        } catch (e: Exception) {
            android.util.Log.e(
                "ERROR_REPORTE",
                e.message ?: "Error desconocido",
                e
            )
            emptyList()
        }
    }
}
suspend fun obtenerVentasPorFecha(
    fechaDesde: String,
    fechaHasta: String
): List<ReportexFecha> {
    return withContext(Dispatchers.IO) {
        try {
            val url =
                "https://script.google.com/macros/s/AKfycbx7rwGp8kC21GdfUcN40JZaqtfj6lnpnNiAue2N-bz6_w3N7NGFBhczHf-JW9pufV7Ueg/exec?tabla=DetallePlanilla"
            val respuesta = URL(url).readText()
            val json = JSONArray(respuesta)
            val mapa = mutableMapOf<String, ReportexFecha>()
            for (i in 0 until json.length()) {
                val item = json.getJSONObject(i)
                val Uudd = item.optString("Uudd")
                val nombreEvento = item.optString("nombreEvento")
                val fecha = item.optString("fechaDesde")
                val totalVenta = item.optString("totalVenta").toIntOrNull() ?: 0
                if (!mapa.containsKey(Uudd)) {
                    mapa[Uudd] = ReportexFecha(
                        uudd = Uudd,
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
suspend fun obtenerProductosMasVendidosMes(
    fechaDesde: String,
    fechaHasta: String
): List<ProductoMasVendidoMes> {
    return withContext(Dispatchers.IO) {
        try {
            val url =
                "https://script.google.com/macros/s/AKfycbx7rwGp8kC21GdfUcN40JZaqtfj6lnpnNiAue2N-bz6_w3N7NGFBhczHf-JW9pufV7Ueg/exec?tabla=DetallePlanilla"
            val respuesta =
                URL(url).readText()
            val json =
                JSONArray(respuesta)
            val productos =
                mutableMapOf<String, Int>()
            for (i in 0 until json.length()) {
                val item =
                    json.getJSONObject(i)
                val nombre =
                    item.optString("nombre")
                val cantidad =
                    item.optString("cantidad")
                        .toIntOrNull()
                        ?: 0
                android.util.Log.d(
                    "PRODUCTO_VENTA",
                    "Nombre=$nombre Cantidad=$cantidad"
                )
                if (nombre.isNotEmpty()) {
                    productos[nombre] =
                        (productos[nombre] ?: 0) + cantidad
                }
            }
            productos.map {
                ProductoMasVendidoMes(
                    producto = it.key,
                    cantidadTotal = it.value
                )
            }
                .sortedByDescending {
                    it.cantidadTotal
                }
        } catch (e: Exception) {
            android.util.Log.e(
                "ERROR_PRODUCTOS_MES",
                e.toString()
            )
            emptyList()
        }
    }
}