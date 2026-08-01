package com.example.critstore

import okhttp3.*
import org.json.JSONObject
import java.io.IOException
import okhttp3.MediaType.Companion.toMediaType

private const val URL_GOOGLE =
        "https://script.google.com/macros/s/AKfycbx7rwGp8kC21GdfUcN40JZaqtfj6lnpnNiAue2N-bz6_w3N7NGFBhczHf-JW9pufV7Ueg/exec?tabla=Producto"
fun enviarGoogle(
    json: JSONObject
) {
    val body = RequestBody.create(
        "application/json".toMediaType(),
        json.toString()
    )
    val request = Request.Builder()
        .url(URL_GOOGLE)
        .post(body)
        .build()
    val cliente = OkHttpClient()
    cliente.newCall(request)
        .enqueue(object : Callback {
            override fun onFailure(
                call: Call,
                e: IOException
            ) {
                println(
                    "ERROR GOOGLE: ${e.message}"
                )
            }
            override fun onResponse(
                call: Call,
                response: Response
            ) {
                println(
                    "GOOGLE: ${response.body?.string()}"
                )
            }
        })
}
fun sincronizarProducto(
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
fun sincronizarDetalle(
    detalle: DetallePlanilla,
    planilla: PlanillaVenta
) {
    val json = JSONObject()
    json.put(
        "tabla",
        "DetallePlanilla"
    )
    json.put(
        "idPlanilla",
        detalle.idPlanilla
    )
    json.put(
        "nombre",
        detalle.Nombre
    )
    json.put(
        "cantidad",
        detalle.Ventas
    )
    json.put(
        "total",
        detalle.Total
    )
    json.put(
        "nombreEvento",
        planilla.NombreEvento
    )
    json.put(
        "fechaDesde",
        planilla.FechaDesde
    )
    json.put(
        "fechaHasta",
        planilla.FechaHasta
    )
    json.put(
        "totalVenta",
        planilla.TotalVenta
    )
    enviarGoogle(json)
}