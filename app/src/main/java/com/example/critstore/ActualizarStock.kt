package com.example.critstore

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

@Composable
fun ActualizarStock(
    productoDao: ProductoDao,
    volver: () -> Unit
) {
    val configuracion = LocalConfiguration.current
    val esTablet =
        configuracion.screenWidthDp >= 600
    val padding =
        if (esTablet) 32.dp else 16.dp
    val tamañoTitulo =
        if (esTablet) 32.sp else 24.sp
    val tamañoTexto =
        if (esTablet) 20.sp else 16.sp
    var productos by remember {
        mutableStateOf(emptyList<Producto>())
    }
    var cantidades by rememberSaveable {
        mutableStateOf(
            mutableMapOf<String,String>()
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
        productos =
            productoDao.obtenerProductos()
        if(!cargado){
            productos.forEach { producto ->
                cantidades =
                    cantidades.toMutableMap().apply {
                        put(
                            producto.uuid,
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
            .padding(padding)
    ){
        Text(
            text = "Actualizar Stock",
            fontSize = tamañoTitulo,
            style =
                MaterialTheme.typography.headlineMedium
        )
        Spacer(
            Modifier.height(20.dp)
        )
        Card(
            modifier =
                Modifier.fillMaxWidth()
        ){
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                verticalAlignment =
                    Alignment.CenterVertically
            ){
                Text(
                    text = "Producto",
                    fontSize = tamañoTexto,
                    modifier =
                        Modifier.weight(1f)
                )
                Text(
                    text = "Stock",
                    fontSize = tamañoTexto,
                    modifier =
                        Modifier.width(80.dp),
                    textAlign =
                        TextAlign.Center
                )
                Spacer(
                    Modifier.width(50.dp)
                )
            }
        }
        Spacer(
            Modifier.height(8.dp)
        )
        LazyColumn(
            modifier =
                Modifier.weight(1f)
        ){
            items(productos){ producto ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ){
                    Row(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                        verticalAlignment =
                            Alignment.CenterVertically
                    ){
                        Text(
                            text = producto.Nombre,
                            fontSize = tamañoTexto,
                            maxLines = 1,
                            modifier =
                                Modifier.weight(1f)
                        )
                        Box(
                            modifier = Modifier
                                .width(80.dp)
                                .height(40.dp),
                            contentAlignment =
                                Alignment.Center
                        ){
                            BasicTextField(
                                value =
                                    cantidades[producto.uuid]
                                        ?: "",
                                onValueChange = { valor ->
                                    cantidades =
                                        cantidades
                                            .toMutableMap()
                                            .apply {
                                                put(
                                                    producto.uuid,
                                                    valor.filter {
                                                        it.isDigit()
                                                    }
                                                )
                                            }
                                },
                                singleLine = true,
                                textStyle =
                                    LocalTextStyle.current.copy(
                                        textAlign =
                                            TextAlign.Center,
                                        fontSize =
                                            16.sp
                                    ),
                                modifier =
                                    Modifier.fillMaxSize()
                            )
                        }
                        IconButton(
                            onClick = {
                                productoEliminar =
                                    producto
                            }
                        ){
                            Icon(
                                imageVector =
                                    Icons.Default.Delete,
                                contentDescription =
                                    "Eliminar"
                            )
                        }
                    }
                }
            }
        }
        Button(
            onClick = {
                scope.launch {
                    cantidades.forEach { (uuid, cantidad) ->
                        val nuevaCantidad =
                            cantidad.toIntOrNull() ?: 0
                        productoDao.actualizarCantidad(
                            uuid,
                            nuevaCantidad
                        )
                        actualizarStockGoogle(
                            uuid,
                            nuevaCantidad
                        )
                    }
                    productos =
                        productoDao.obtenerProductos()
                    Toast.makeText(
                        context,
                        "✅ Stock guardado y actualizado correctamente",
                        Toast.LENGTH_LONG
                    ).show()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp)
        ) {
            Text(
                text = "💾 Guardar Cambios",
                fontSize = 18.sp
            )
        }
        Spacer(
            Modifier.height(8.dp)
        )
        OutlinedButton(
            onClick = volver,
            modifier =
                Modifier.fillMaxWidth()
        ){
            Text(
                "⬅ Volver"
            )
        }
    }
    productoEliminar?.let { producto ->
        AlertDialog(
            onDismissRequest = {
                productoEliminar = null
            },
            title = {
                Text(
                    "Eliminar producto"
                )
            },
            text = {
                Text(
                    "¿Eliminar ${producto.Nombre}?"
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        scope.launch {
                            productoDao.eliminarProducto(
                                producto
                            )
                            eliminarProductoGoogle(
                                producto.uuid
                            )
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
                ){
                    Text("Eliminar")
                }
            },
            dismissButton = {
                Button(
                    onClick = {
                        productoEliminar = null
                    }
                ){
                    Text("Cancelar")
                }
            }
        )
    }
}