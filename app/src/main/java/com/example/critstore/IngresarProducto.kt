package com.example.critstore

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext

@Composable
fun IngresarProducto(
    productoDao: ProductoDao,
    volver: () -> Unit
) {
    var nombre by remember { mutableStateOf("") }
    var cantidad by remember { mutableStateOf("") }
    var precio by remember { mutableStateOf("") }
    var tipo by remember { mutableStateOf("") }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(30.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Ingresar Producto",
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(
            modifier = Modifier.height(20.dp)
        )
        OutlinedTextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = {
                Text("Nombre")
            }
        )
        OutlinedTextField(
            value = cantidad,
            onValueChange = { cantidad = it },
            label = {
                Text("Cantidad")
            }
        )
        OutlinedTextField(
            value = precio,
            onValueChange = { precio = it },
            label = {
                Text("Precio")
            }
        )
        OutlinedTextField(
        value = tipo,
        onValueChange = { tipo = it },
        label = {
            Text("Tipo")
        }
        )
        Spacer(
            modifier = Modifier.height(20.dp)
        )
        Button(
            onClick = {
                val producto = Producto(
                    Tipo = tipo,
                    Nombre = nombre,
                    Cantidad = cantidad.toInt(),
                    Precio = precio.toInt()
                )
                scope.launch {
                    productoDao.insertarProducto(producto)
                    Toast.makeText(
                        context,
                        "Producto guardado correctamente",
                        Toast.LENGTH_SHORT
                    ).show()
                    // Limpiar campos
                    tipo = ""
                    nombre = ""
                    cantidad = ""
                    precio = ""
                }
            },
            modifier = Modifier.padding(top = 10.dp)
        ) {
            Text("💾 Guardar Producto")
        }
        Button(
            onClick = volver,
            modifier = Modifier.padding(top = 10.dp)
        ) {
            Text("⬅ Volver")
        }
    }
}