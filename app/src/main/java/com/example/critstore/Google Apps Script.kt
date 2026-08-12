package com.example.critstore

import android.util.Log
import kotlinx.coroutines.*
import okhttp3.*
import org.json.JSONObject
import okhttp3.MediaType.Companion.toMediaType

private const val URL_GOOGLE =
        "https://script.google.com/macros/s/AKfycbx7rwGp8kC21GdfUcN40JZaqtfj6lnpnNiAue2N-bz6_w3N7NGFBhczHf-JW9pufV7Ueg/exec?tabla=Producto"
suspend fun enviarGoogle(
    json: JSONObject
): Boolean {
    return withContext(Dispatchers.IO) {
        try {
            val body =
                RequestBody.create(
                    "application/json".toMediaType(),
                    json.toString()
                )
            val request =
                Request.Builder()
                    .url(URL_GOOGLE)
                    .post(body)
                    .build()
            val clienteGoogle =
                OkHttpClient()
            val response =
                clienteGoogle
                    .newCall(request)
                    .execute()
            response.close()
            true
        } catch (e: Exception) {
            Log.e(
                "GOOGLE_ERROR",
                e.message ?: "error"
            )
            false
        }
    }
}
suspend fun sincronizarProducto(
    producto: Producto
) {
    val json = JSONObject()
    json.put(
        "tabla",
        "Producto"
    )
    json.put(
        "uuid",
        producto.uuid
    )
    json.put(
        "nombre",
        producto.Nombre
    )
    json.put(
        "cantidad",
        producto.Cantidad
    )
    json.put(
        "precio",
        producto.Precio
    )
    json.put(
        "tipo",
        producto.Tipo
    )
    enviarGoogle(json)
}
suspend fun sincronizarDetalle(
    detalle: DetallePlanilla,
    planilla: PlanillaVenta )
{
    val json = JSONObject()
    json.put("tabla", "DetallePlanilla")
    json.put("uudd", detalle.Uudd)
    json.put("nombre", detalle.Nombre)
    json.put("cantidad", detalle.Ventas)
    json.put("total", detalle.Total)
    json.put("nombreEvento", planilla.NombreEvento)
    json.put("fechaDesde", planilla.FechaDesde)
    json.put("fechaHasta", planilla.FechaHasta)
    json.put("totalVenta", planilla.totalVenta)
    Log.d("JSON", json.toString())
    val resultado = enviarGoogle(json)
    Log.d("GOOGLE", "Resultado = $resultado")
}
suspend fun eliminarDetallesPlanilla(uudd: String) {
    val json = JSONObject()
    json.put(
        "accion",
        "eliminarDetallePlanilla"
    )
    json.put(
        "uudd",
        uudd
    )
    val resultado = enviarGoogle(json)
    Log.d(
        "ELIMINAR_GOOGLE",
        "Resultado = $resultado"
    )
}