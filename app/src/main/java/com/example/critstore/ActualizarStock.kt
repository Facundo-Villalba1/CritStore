package com.example.critstore
import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.saveable.rememberSaveable
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
@Composable
fun ActualizarStock(
    productoDao: ProductoDao,
    volver: () -> Unit
) {
    var productos by remember {
        mutableStateOf(emptyList<Producto>())
    }
    var cantidades by rememberSaveable {
        mutableStateOf(
            mutableMapOf<Int, String>()
        )
    }
    var cargado by remember {
        mutableStateOf(false)
    }
    var productoEliminar by remember {
        mutableStateOf<Producto?>(null)
    }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        productos = productoDao.obtenerProductos()
        if (!cargado) {
            productos.forEach { producto ->
                cantidades =
                    cantidades.toMutableMap().apply {
                        put(
                            producto.id,
                            producto.Cantidad.toString()
                        )
                    }
            }
            cargado = true
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Actualizar Stock",
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(
            modifier = Modifier.height(20.dp)
        )
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
            ) {
                Text(
                    "Producto",
                    modifier = Modifier.weight(2f)
                )
                Text(
                    "Stock",
                    modifier = Modifier.weight(1f)
                )
                Text("")
            }
        }
        Spacer(
            modifier = Modifier.height(8.dp)
        )
        LazyColumn(
            modifier = Modifier.weight(1f)
        ) {
            items(productos) { producto ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                        verticalAlignment =
                            androidx.compose.ui.Alignment.CenterVertically
                    ) {
                        Text(
                            producto.Nombre,
                            modifier = Modifier.weight(2f)
                        )
                        OutlinedTextField(
                            value =
                                cantidades[producto.id] ?: "",
                            onValueChange = { nuevoValor ->
                                cantidades =
                                    cantidades.toMutableMap().apply {
                                        put(
                                            producto.id,
                                            nuevoValor
                                        )
                                    }
                            },
                            modifier = Modifier
                                .width(70.dp)
                                .height(45.dp),
                            singleLine = true
                        )
                        IconButton(
                            onClick = {
                                productoEliminar = producto
                            }
                        ) {
                            Icon(
                                imageVector =
                                    Icons.Default.Delete,
                                contentDescription =
                                    "Eliminar producto"
                            )
                        }
                    }
                }
            }
        }
        Button(
            onClick = {
                scope.launch {
                    cantidades.forEach { (id, cantidad) ->
                        if (cantidad.isNotEmpty()) {
                            productoDao.actualizarCantidad(
                                id,
                                cantidad.toInt()
                            )
                        }
                    }
                    productos =
                        productoDao.obtenerProductos()
                    Toast.makeText(
                        context,
                        "Stock actualizado correctamente",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("💾 Guardar Cambios")
        }
        Spacer(
            modifier = Modifier.height(10.dp)
        )
        Button(
            onClick = volver,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("⬅ Volver")
        }
    }
    productoEliminar?.let { producto ->
        AlertDialog(
            onDismissRequest = {
                productoEliminar = null
            },
            title = {
                Text("Eliminar producto")
            },
            text = {
                Text(
                    "¿Desea eliminar ${producto.Nombre}?"
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        scope.launch {
                            productoDao.eliminarProducto(producto)
                            productos =
                                productoDao.obtenerProductos()
                            productoEliminar = null
                            Toast.makeText(
                                context,
                                "Producto eliminado",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                ) {
                    Text("Eliminar")
                }
            },
            dismissButton = {
                Button(
                    onClick = {
                        productoEliminar = null
                    }
                ) {
                    Text("Cancelar")
                }
            }
        )
    }
}
