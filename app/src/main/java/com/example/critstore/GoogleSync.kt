package com.example.critstore

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import org.json.JSONArray
import org.json.JSONObject
import java.net.*
import java.util.concurrent.TimeUnit

suspend fun obtenerProductosGoogle(): List<Producto> {
    return withContext(Dispatchers.IO) {
        val lista =
            mutableListOf<Producto>()
        try {
            val url =
                "https://script.google.com/macros/s/AKfycbx7rwGp8kC21GdfUcN40JZaqtfj6lnpnNiAue2N-bz6_w3N7NGFBhczHf-JW9pufV7Ueg/exec?tabla=Producto"
            val request =
                Request.Builder()
                    .url(url)
                    .get()
                    .build()
            val client =
                OkHttpClient.Builder()
                    .connectTimeout(
                        10,
                        TimeUnit.SECONDS
                    )
                    .readTimeout(
                        15,
                        TimeUnit.SECONDS
                    )
                    .build()
            val response =
                client
                    .newCall(request)
                    .execute()
            if (!response.isSuccessful) {
                Log.e(
                    "GOOGLE",
                    "Error HTTP: ${response.code}"
                )
                return@withContext emptyList()
            }
            val resultado =
                response.body
                    ?.string()
                    ?: return@withContext emptyList()
            Log.d(
                "GOOGLE",
                "Respuesta recibida: ${resultado.length} caracteres"
            )
            val jsonArray =
                JSONArray(resultado)
            for (
            i in 0 until jsonArray.length()
            ) {
                val item =
                    jsonArray.getJSONObject(i)
                val cantidad =
                    if (
                        item.has("cantidad") &&
                        !item.isNull("cantidad")
                    ) {
                        item
                            .getDouble("cantidad")
                            .toInt()
                    } else {
                        0
                    }
                val precio =
                    if (
                        item.has("precio") &&
                        !item.isNull("precio")
                    ) {
                        item
                            .getDouble("precio")
                            .toInt()
                    } else {
                        0
                    }
                lista.add(
                    Producto(
                        uuid =
                            item.optString(
                                "uuid",
                                ""
                            ),
                        Nombre =
                            item.optString(
                                "nombre",
                                ""
                            ),
                        Cantidad =
                            cantidad,
                        Precio =
                            precio,
                        Tipo =
                            item.optString(
                                "tipo",
                                ""
                            )
                    )
                )
            }
            lista
        } catch (e: Exception) {
            Log.e(
                "GOOGLE",
                "Error obteniendo productos",
                e
            )
            emptyList()
        }
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
//Para varios produictos
suspend fun actualizarStocksGooglee(
    productos: List<Producto>
): Boolean {
    return withContext(Dispatchers.IO) {
        try {
            val json = JSONObject()
            json.put(
                "accion",
                "actualizarStocks"
            )
            val array = JSONArray()
            productos.forEach { producto ->
                val item = JSONObject()
                item.put(
                    "uuid",
                    producto.uuid
                )
                item.put(
                    "cantidad",
                    producto.Cantidad
                )
                array.put(item)
            }
            json.put(
                "productos",
                array
            )
            val body =
                RequestBody.create(
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
            Log.d(
                "GOOGLE",
                "Respuesta: $resultado"
            )
            resultado?.contains(
                "Stock actualizado"
            ) == true
        } catch (e: Exception) {
            Log.e(
                "GOOGLE",
                "Error",
                e
            )
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
suspend fun obtenerMaterialesGoogle(): List<Materiales> {
    return withContext(Dispatchers.IO) {
        val lista =
            mutableListOf<Materiales>()
        try {
            val url =
                "https://script.google.com/macros/s/AKfycbx7rwGp8kC21GdfUcN40JZaqtfj6lnpnNiAue2N-bz6_w3N7NGFBhczHf-JW9pufV7Ueg/exec?tabla=Materiales"
            val resultado =
                URL(url).readText()
            val jsonArray =
                JSONArray(resultado)
            for (
            i in 0 until jsonArray.length()
            ) {
                val item =
                    jsonArray.getJSONObject(i)
                val cantidad =
                    if (
                        item.has("cantidad") &&
                        !item.isNull("cantidad")
                    ) {
                        item
                            .getDouble("cantidad")
                            .toInt()
                    } else {
                        0
                    }
                lista.add(
                    Materiales(
                        uuid =
                            item.optString(
                                "uuid",
                                ""
                            ),
                        Cantidad =
                            cantidad,
                        Color =
                            item.optString(
                                "color",
                                ""
                            ),
                        Marca =
                            item.optString(
                                "marca",
                                ""
                            )
                    )
                )
            }
            lista
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }
}
suspend fun actualizarMaterialesGoogle(
    materiales: List<Materiales>
): Boolean {
    return withContext(Dispatchers.IO) {
        try {
            val json = JSONObject()
            json.put(
                "accion",
                "actualizarMateriales"
            )
            val array = JSONArray()
            materiales.forEach { material ->
                val item = JSONObject()
                item.put(
                    "uuid",
                    material.uuid
                )
                item.put(
                    "cantidad",
                    material.Cantidad
                )
                item.put(
                    "color",
                    material.Color
                )
                item.put(
                    "marca",
                    material.Marca
                )
                array.put(item)
            }
            json.put(
                "materiales",
                array
            )
            val body =
                RequestBody.create(
                    "application/json".toMediaType(),
                    json.toString()
                )
            val request =
                Request.Builder()
                    .url(
                        " https://script.google.com/macros/s/AKfycbx7rwGp8kC21GdfUcN40JZaqtfj6lnpnNiAue2N-bz6_w3N7NGFBhczHf-JW9pufV7Ueg/exec?tabla=Materiales"
                    )
                    .post(body)
                    .build()
            val response =
                OkHttpClient()
                    .newCall(request)
                    .execute()
            val resultado =
                response.body?.string()
            Log.d(
                "GOOGLE_MATERIALES",
                "Respuesta actualizar: $resultado"
            )
            resultado?.contains(
                "Materiales actualizados"
            ) == true
        } catch (e: Exception) {
            Log.e(
                "GOOGLE_MATERIALES",
                "Error actualizando materiales",
                e
            )
            false
        }
    }
}
suspend fun eliminarMaterialGoogle(
    uuid: String
): Boolean {
    return withContext(Dispatchers.IO) {
        try {
            val json =
                JSONObject()
            json.put(
                "accion",
                "eliminarMaterial"
            )
            json.put(
                "uuid",
                uuid
            )
            val body =
                RequestBody.create(
                    "application/json".toMediaType(),
                    json.toString()
                )
            val request =
                Request.Builder()
                    .url(
                        " https://script.google.com/macros/s/AKfycbx7rwGp8kC21GdfUcN40JZaqtfj6lnpnNiAue2N-bz6_w3N7NGFBhczHf-JW9pufV7Ueg/exec?tabla=Materiales"
                    )
                    .post(body)
                    .build()
            val response =
                OkHttpClient()
                    .newCall(request)
                    .execute()
            val resultado =
                response.body?.string()
            Log.d(
                "GOOGLE_MATERIALES",
                "Respuesta eliminar: $resultado"
            )
            resultado?.contains(
                "Material eliminado"
            ) == true
        } catch (e: Exception) {
            Log.e(
                "GOOGLE_MATERIALES",
                "Error eliminando material",
                e
            )
            false
        }
    }
}
suspend fun sincronizarMaterialesGoogle(
    materiales: List<Materiales>
) {
    withContext(Dispatchers.IO) {
        materiales.forEach { material ->
            val json = JSONObject().apply {
                put(
                    "tabla",
                    "Materiales"
                )
                put(
                    "uuid",
                    material.uuid
                )
                put(
                    "cantidad",
                    material.Cantidad
                )
                put(
                    "color",
                    material.Color
                )
                put(
                    "marca",
                    material.Marca
                )
            }
            enviarGoogle(
                json
            )
        }
    }
}