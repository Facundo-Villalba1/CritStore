package com.example.critstore

import android.widget.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material.icons.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.*
import androidx.compose.ui.*
import androidx.compose.ui.platform.*
import androidx.compose.ui.text.style.*
import androidx.compose.ui.unit.*
import kotlinx.coroutines.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActualizarStock(
    productoDao: ProductoDao,
    volver: () -> Unit
) {
    val dim =obtenerDimensiones()
    var productos by remember {
        mutableStateOf(emptyList<Producto>())
    }
    var cantidades by rememberSaveable {
        mutableStateOf(
            mutableMapOf<String, String>()
        )
    }
        var tipos by remember {
        mutableStateOf(listOf<String>())
    }
    var tipoSeleccionado by remember {
        mutableStateOf("Todos")
    }
    var menuTipo by remember {
        mutableStateOf(false)
    }
    var cargado by remember {
        mutableStateOf(false)
    }
    var productoEliminar by remember {
        mutableStateOf<Producto?>(null)
    }
    val scope =
        rememberCoroutineScope()
    val context =
        LocalContext.current
    LaunchedEffect(Unit) {
        productos =
            productoDao.obtenerProductos()
        tipos =
            listOf("Todos") +
                    productoDao.obtenerTipos()
        if (!cargado) {
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
    val productosFiltrados =
        if (tipoSeleccionado == "Todos")
            productos
        else
            productos.filter {
                it.Tipo == tipoSeleccionado
            }
    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(   top = dim.paddingPantalla * 2,
                    start = dim.paddingPantalla,
                    end = dim.paddingPantalla,
                    bottom = dim.paddingPantalla)
    ) {
        Text(
            text = "Actualizar Stock",
            fontSize =dim.titulo,
            style =
                MaterialTheme.typography.headlineMedium
        )
        Spacer( modifier = Modifier.height(dim.espacio) )
        ExposedDropdownMenuBox(
            expanded = menuTipo,
            onExpandedChange = {
                menuTipo = !menuTipo
            }
        ) {
            OutlinedTextField(
                value =
                    tipoSeleccionado,
                onValueChange = {},
                readOnly = true,
                label = {
                    Text(
                        "Filtrar por tipo"
                    )
                },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(
                        expanded = menuTipo
                    )
                },
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .menuAnchor()
            )
            ExposedDropdownMenu(
                expanded = menuTipo,
                onDismissRequest = {
                    menuTipo = false
                },
                modifier =
                    Modifier.fillMaxWidth()
            ) {
                tipos.forEach { tipo ->
                    DropdownMenuItem(
                        text = {
                            Text(tipo)
                        },
                        onClick = {
                            tipoSeleccionado =
                                tipo
                            menuTipo = false
                        }
                    )
                }
            }
        }
        Spacer( modifier = Modifier.height(dim.espacio) )
        Card(
            modifier =
                Modifier.fillMaxWidth()
        ) {
            Row(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                verticalAlignment =
                    Alignment.CenterVertically
            ) {
                Text(
                    "Producto",
                    fontSize = dim.texto,
                    modifier =
                        Modifier.weight(1f)
                )
                Text(
                    "Stock",
                    fontSize = dim.texto,
                    modifier =
                        Modifier.width(160.dp),
                    textAlign =
                        TextAlign.Center
                )
            }
        }
        Spacer( modifier = Modifier.height(dim.espacio) )
        LazyColumn(
            modifier =
                Modifier.weight(1f)
        ) {
            items(productosFiltrados) { producto ->
                Card(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                ) {
                    Row(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                        verticalAlignment =
                            Alignment.CenterVertically
                    ) {
                        Column(
                            modifier =
                                Modifier.weight(1f)
                        ) {
                            Text(
                                producto.Nombre,
                                fontSize = dim.texto
                            )
                            Text(
                                producto.Tipo,
                                fontSize =
                                    12.sp
                            )
                        }
                        IconButton(
                            onClick = {
                                val actual =
                                    cantidades[producto.uuid]
                                        ?.toIntOrNull()
                                        ?: 0
                                if (actual > 0) {
                                    cantidades =
                                        cantidades.toMutableMap().apply {
                                            put(
                                                producto.uuid,
                                                (actual - 1)
                                                    .toString()
                                            )
                                        }
                                }
                            }
                        ) {
                            Icon(
                                Icons.Default.Remove,
                                contentDescription =
                                    "Restar"
                            )
                        }
                        Text(
                            cantidades[producto.uuid]
                                ?: "0",
                            fontSize =
                                18.sp,
                            modifier =
                                Modifier.width(50.dp),
                            textAlign =
                                TextAlign.Center
                        )
                        IconButton(
                            onClick = {
                                val actual =
                                    cantidades[producto.uuid]
                                        ?.toIntOrNull()
                                        ?: 0
                                cantidades =
                                    cantidades.toMutableMap().apply {
                                        put(
                                            producto.uuid,
                                            (actual + 1)
                                                .toString()
                                        )
                                    }
                            }
                        ) {
                            Icon(
                                Icons.Default.Add,
                                contentDescription =
                                    "Sumar"
                            )
                        }
                        IconButton(
                            onClick = {
                                productoEliminar =
                                    producto
                            }
                        ) {
                            Icon(
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
                            cantidad.toIntOrNull()
                                ?: 0
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
                        "✅ Stock guardado correctamente",
                        Toast.LENGTH_LONG
                    ).show()
                }
            },
            modifier =Modifier
                .widthIn(dim.alturaBotonInterno)
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally)
        ) {
            Text(
                "💾 Actualizar",
                fontSize =dim.texto
            )
        }
        Spacer( modifier = Modifier.height(dim.espacio) )
        OutlinedButton(
            onClick = volver,
            modifier =Modifier
                .widthIn(dim.alturaBotonInterno)
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally)
        ) {
            Text(
                "⬅ Volver",
                fontSize = dim.texto
            )
        }
        Spacer( modifier = Modifier.height(dim.espacio) )
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
                ) {
                    Text("Eliminar")
                }
                Spacer( modifier = Modifier.height(dim.espacio) )
            },
            dismissButton = {
                Button(
                    onClick = {
                        productoEliminar = null
                    }
                ) {
                    Text("Cancelar")
                }
                Spacer( modifier = Modifier.height(dim.espacio) )
            }
        )
    }
}