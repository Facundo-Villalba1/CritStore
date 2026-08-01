package com.example.critstore

import org.json.JSONArray
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.URL
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.MediaType.Companion.toMediaType
import org.json.JSONObject

suspend fun obtenerProductosGoogle(): List<Producto> {
    return withContext(Dispatchers.IO) {
        val lista = mutableListOf<Producto>()
        val url =
            "https://script.google.com/macros/s/AKfycbx7rwGp8kC21GdfUcN40JZaqtfj6lnpnNiAue2N-bz6_w3N7NGFBhczHf-JW9pufV7Ueg/exec?tabla=Producto"
        val resultado =
            URL(url).readText()
        val jsonArray =
            JSONArray(resultado)
        for (i in 0 until jsonArray.length()) {
            val item =
                jsonArray.getJSONObject(i)
            lista.add(
                Producto(
                    uuid = item.getString("uuid"),
                    Nombre = item.getString("nombre"),
                    Cantidad =
                        item.getString("cantidad")
                            .toIntOrNull()
                            ?: 0,
                    Precio =
                        item.getString("precio")
                            .toIntOrNull()
                            ?: 0,
                    Tipo = item.getString("tipo")
                )
            )
        }
        lista
    }
}
suspend fun actualizarStockGoogle(
    uuid: String,
    cantidad: Int
): Boolean {
    return withContext(Dispatchers.IO) {
        try {
            val json = JSONObject()
            json.put("accion", "actualizarStock")
            json.put("uuid", uuid)
            json.put("cantidad", cantidad)
            val body = RequestBody.create(
                "application/json".toMediaType(),
                json.toString()
            )
            val request = Request.Builder()
                .url(
                    "https://script.google.com/macros/s/AKfycbx7rwGp8kC21GdfUcN40JZaqtfj6lnpnNiAue2N-bz6_w3N7NGFBhczHf-JW9pufV7Ueg/exec"
                )
                .post(body)
                .build()
            val response =
                OkHttpClient()
                    .newCall(request)
                    .execute()
            val resultado =
                response.body?.string()
            println("RESPUESTA GOOGLE: $resultado")
            resultado?.contains("Stock actualizado") == true
        } catch (e: Exception) {
            println("ERROR GOOGLE: ${e.message}")
            false
        }
    }
}
suspend fun eliminarProductoGoogle(
    uuid: String
): Boolean {
    return withContext(Dispatchers.IO) {
        try {
            val json = JSONObject()
            json.put(
                "accion",
                "eliminarProducto"
            )
            json.put(
                "uuid",
                uuid
            )
            val body = RequestBody.create(
                "application/json".toMediaType(),
                json.toString()
            )
            val request =
                Request.Builder()
                    .url(
                        "https://script.google.com/macros/s/AKfycbx7rwGp8kC21GdfUcN40JZaqtfj6lnpnNiAue2N-bz6_w3N7NGFBhczHf-JW9pufV7Ueg/exec"
                    )
                    .post(body)
                    .build()
            val response =
                OkHttpClient()
                    .newCall(request)
                    .execute()
            val resultado =
                response.body?.string()
            println(
                "RESPUESTA ELIMINAR GOOGLE: $resultado"
            )
            resultado?.contains(
                "Producto eliminado"
            ) == true
        } catch (e: Exception) {
            println(
                "ERROR ELIMINAR GOOGLE: ${e.message}"
            )
            false
        }
    }
}
