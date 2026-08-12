package com.example.critstore

import android.app.*
import android.widget.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.platform.*
import androidx.compose.ui.text.style.*
import androidx.compose.ui.unit.*
import kotlinx.coroutines.*
import java.util.*

@Composable
fun GenerarPlanilla(
    productoDao: ProductoDao,
    planillaDao: PlanillaDao,
    volver: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val dim = obtenerDimensiones()
    var nombreEvento by remember {
        mutableStateOf(TempPlanilla.nombreEvento)
    }
    var fechaDesde by remember {
        mutableStateOf(TempPlanilla.fechaDesde)
    }
    var fechaHasta by remember {
        mutableStateOf(TempPlanilla.fechaHasta)
    }
    var productosDisponibles by remember {
        mutableStateOf(emptyList<Producto>())
    }
    var productosVenta by remember {
        mutableStateOf(emptyList<Producto>())
    }
    var cantidadesVenta by remember {
        mutableStateOf(TempPlanilla.cantidadesVenta)
    }
    var mostrarAgregarProductos by remember {
        mutableStateOf(false)
    }
    LaunchedEffect(Unit) {
        productosDisponibles =
            productoDao.obtenerProductos()
    }
    fun seleccionarFecha(
        cambiarFecha: (String) -> Unit
    ) {
        val calendario = Calendar.getInstance()
        DatePickerDialog(
            context,
            { _, año, mes, dia ->
                cambiarFecha(
                    "$dia/${mes + 1}/$año"
                )
            },
            calendario.get(Calendar.YEAR),
            calendario.get(Calendar.MONTH),
            calendario.get(Calendar.DAY_OF_MONTH)
        ).show()
    }
     if (mostrarAgregarProductos) {
       AgregarProductos(
            productoDao = productoDao,
           productosYaAgregados = productosVenta,
           agregarProducto = { producto ->
               if (!productosVenta.any {
                        it.uuid == producto.uuid
                    }
                ) {
                   productosVenta =
                        productosVenta + producto
                   cantidadesVenta =
                        cantidadesVenta
                            .toMutableMap()
                            .apply {
                                put(
                                    producto.uuid,
                                    "1"
                                )
                            }
                }
               mostrarAgregarProductos = false
            },
           volver = {
                mostrarAgregarProductos = false
            }
        )
       return
    }
    val totalVenta =
        productosVenta.sumOf { producto ->
            val cantidad =
                cantidadesVenta[producto.uuid]
                    ?.toIntOrNull() ?: 0
            producto.Precio * cantidad
        }
    val hayStockNegativo =
        productosVenta.any { producto ->
            val cantidad =
                cantidadesVenta[producto.uuid]
                    ?.toIntOrNull() ?: 0
            producto.Cantidad - cantidad < 0
        }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = dim.paddingPantalla * 2,
                start = dim.paddingPantalla,
                end = dim.paddingPantalla,
                bottom = dim.paddingPantalla)
    ) {
        Text(
            text = "Generar Nueva Planilla",
            fontSize = dim.titulo,
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(
            modifier = Modifier.height(dim.espacio)
        )
        OutlinedTextField(
            value = nombreEvento,
            onValueChange = {
                nombreEvento = it
                TempPlanilla.nombreEvento = it
            },
            label = {
                Text(
                    "Nombre del evento",
                    fontSize = dim.texto
                )
            },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        Spacer(
            modifier = Modifier.height(dim.espacio)
        )
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Button(
                onClick = {
                    seleccionarFecha {
                        fechaDesde = it
                        TempPlanilla.fechaDesde = it
                    }
                },
                modifier =
                    if (dim.esTablet)
                        Modifier
                            .weight(1f)
                            .height(dim.alturaBotonInterno)
                    else
                        Modifier
                            .fillMaxWidth()
                            .height(dim.alturaBotonInterno)
            ) {
                Text(
                    if (fechaDesde.isEmpty())
                        "📅 Desde"
                    else
                        fechaDesde,
                    fontSize =  dim.texto
                )
            }
            Button(
                onClick = {
                    seleccionarFecha {
                        fechaHasta = it
                        TempPlanilla.fechaHasta = it
                    }
                },
                modifier =
                    if (dim.esTablet)
                        Modifier
                            .weight(1f)
                            .height(dim.alturaBotonInterno)
                    else
                        Modifier
                            .fillMaxWidth()
                            .height(dim.alturaBotonInterno)
            ) {
                Text(
                    if (fechaHasta.isEmpty())
                        "📅 Hasta"
                    else
                        fechaHasta,
                    fontSize =  dim.texto
                )
            }
        }
        Spacer(
            modifier = Modifier.height(dim.espacio)
        )
        Button(
            onClick = {
                mostrarAgregarProductos = true
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(dim.alturaBotonInterno)
        ) {
            Text(
                "Agregar Productos",
                fontSize =  dim.texto
            )
        }
        Spacer(
            modifier = Modifier.height(dim.espacio)
        )
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(productosVenta) { producto ->
                val cantidadVendida =
                    cantidadesVenta[producto.uuid]
                        ?.toIntOrNull() ?: 0
                val stockNuevo =
                    producto.Cantidad - cantidadVendida
                val totalProducto =
                    producto.Precio * cantidadVendida
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 5.dp),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 6.dp
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                dim.paddingPantalla
                            )
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = producto.Nombre,
                                modifier = Modifier.weight(1f),
                                fontSize = dim.texto,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            IconButton(
                                onClick = {
                                    productosVenta =
                                        productosVenta.filter {
                                            it.uuid != producto.uuid
                                        }
                                    cantidadesVenta =
                                        cantidadesVenta
                                            .toMutableMap()
                                            .apply {
                                                remove(
                                                    producto.uuid
                                                )
                                            }
                                },
                                modifier = Modifier.size(
                                    dim.alturaBotonInterno
                                )
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Eliminar producto"
                                )
                            }
                        }
                        Spacer(
                            modifier = Modifier.height(dim.espacio)
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Stock: ${producto.Cantidad}",
                                fontSize =  dim.texto
                            )
                            Text(
                                text = "Precio: $${producto.Precio}",
                                fontSize =  dim.texto
                            )
                        }
                        Spacer(
                            modifier = Modifier.height(dim.espacio)
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Cantidad:",
                                fontSize =  dim.texto
                            )
                            Spacer(
                                modifier = Modifier.height(dim.espacio)
                            )
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(2.dp)
                            ) {
                                IconButton(
                                    onClick = {
                                        if (cantidadVendida > 0) {
                                            cantidadesVenta =
                                                cantidadesVenta
                                                    .toMutableMap()
                                                    .apply {
                                                        put(
                                                            producto.uuid,
                                                            (cantidadVendida - 1)
                                                                .toString()
                                                        )
                                                    }
                                        }
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Remove,
                                        contentDescription = "Restar"
                                    )
                                }
                                Text(
                                    text = cantidadVendida.toString(),
                                    modifier = Modifier.width(25.dp),
                                    fontSize =  dim.texto,
                                    textAlign = TextAlign.Center
                                )
                                IconButton(
                                    onClick = {
                                        cantidadesVenta =
                                            cantidadesVenta
                                                .toMutableMap()
                                                .apply {
                                                    put(
                                                        producto.uuid,
                                                        (cantidadVendida + 1)
                                                            .toString()
                                                    )
                                                }
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Add,
                                        contentDescription = "Sumar"
                                    )
                                }
                            }
                        }
                        Spacer(
                            modifier = Modifier.height(dim.espacio)
                        )
                        Text(
                            text = "Nuevo stock: $stockNuevo",
                            fontSize =  dim.texto
                        )
                        Text(
                            text = "Total venta: $$totalProducto",
                            fontSize =  dim.texto,
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }
            }
        }
        Spacer(
            modifier = Modifier.height(dim.espacio)
        )
            Text(
                text = "Total Venta: $$totalVenta",
                fontSize =  dim.titulo,
                style = MaterialTheme.typography.headlineSmall
            )
        Spacer(
            modifier = Modifier.height(dim.espacio)
        )
        Button(
            onClick = {
                if (nombreEvento.trim().isEmpty()) {
                    Toast.makeText(
                        context,
                        "Ingrese nombre del evento",
                        Toast.LENGTH_LONG
                    ).show()
                    return@Button
                }
                if (fechaDesde.trim().isEmpty()) {
                    Toast.makeText(
                        context,
                        "Seleccione fecha desde",
                        Toast.LENGTH_LONG
                    ).show()
                    return@Button
                }
                if (fechaHasta.trim().isEmpty()) {
                    Toast.makeText(
                        context,
                        "Seleccione fecha hasta",
                        Toast.LENGTH_LONG
                    ).show()
                    return@Button
                }
                if (productosVenta.isEmpty()) {
                    Toast.makeText(
                        context,
                        "Agregue productos a la venta",
                        Toast.LENGTH_LONG
                    ).show()
                    return@Button
                }
                val hayCantidadValida =
                    productosVenta.any { producto ->
                        val cantidad =
                            cantidadesVenta[producto.uuid]
                                ?.toIntOrNull() ?: 0
                        cantidad > 0
                    }
                if (!hayCantidadValida) {
                    Toast.makeText(
                        context,
                        "Ingrese cantidades de venta",
                        Toast.LENGTH_LONG
                    ).show()
                    return@Button
                }
                if (hayStockNegativo) {
                    Toast.makeText(
                        context,
                        "No hay suficiente stock",
                        Toast.LENGTH_LONG
                    ).show()
                    return@Button
                }
                scope.launch {
                    try {
                        val productosParaSincronizar =
                            mutableListOf<Producto>()
                        val planilla = PlanillaVenta(
                            NombreEvento = nombreEvento.trim(),
                            FechaDesde = fechaDesde,
                            FechaHasta = fechaHasta,
                            totalVenta = totalVenta
                        )
                        val idPlanilla =
                            planillaDao.insertarPlanilla(planilla)
                        productosVenta.forEach { producto ->
                            val cantidad =
                                cantidadesVenta[producto.uuid]
                                    ?.toIntOrNull() ?: 0
                            if (cantidad > 0) {
                                val detalle =
                                    DetallePlanilla(
                                        idPlanilla = idPlanilla.toInt(),
                                        Uudd = planilla.Uudd,
                                        Nombre =
                                            producto.Nombre,
                                        Precio =
                                            producto.Precio,
                                        Ventas =
                                            cantidad,
                                        Total = producto.Precio * cantidad
                                    )
                                planillaDao.insertarDetalle(
                                    detalle
                                )
                                productoDao.descontarStock(
                                    producto.uuid,
                                    cantidad
                                )
                                // Obtener stock real actualizado desde Room
                                val productoActual =
                                    productoDao.obtenerProductos()
                                        .find {
                                            it.uuid == producto.uuid
                                        }
                                if (productoActual != null) {
                                    productosParaSincronizar.add(
                                        productoActual
                                    )
                                }
                            }
                        }
                        // Sincronizar todos juntos
                        val resultado =
                            actualizarStocksGooglee(
                                productosParaSincronizar
                                    .distinctBy {
                                        it.uuid
                                    }
                            )
                        TempPlanilla.limpiar()
                        productosVenta =
                            emptyList()
                        cantidadesVenta =
                            mutableMapOf()
                        productosDisponibles =
                            productoDao.obtenerProductos()
                        nombreEvento = ""
                        fechaDesde = ""
                        fechaHasta = ""
                        Toast.makeText(
                            context,
                            "✅ Planilla guardada correctamente",
                            Toast.LENGTH_LONG
                        ).show()
                        volver()
                    } catch (e: Exception) {
                        Toast.makeText(
                            context,
                            "Error al guardar: ${e.message}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            },
            modifier =Modifier
                .widthIn(dim.alturaBotonInterno)
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally),
            enabled = !hayStockNegativo
        ) {
            Text(
                text = "💾 Guardar",
                fontSize =  dim.texto
            )
        }
        Spacer(
            modifier = Modifier.height(dim.espacio)
        )
            OutlinedButton(
                onClick = volver,
                modifier =Modifier
                    .widthIn(dim.alturaBotonInterno)
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally)
            ) {
                Text(
                    text = "⬅ Volver",
                    fontSize =  dim.texto
                )
                Spacer(
                    modifier = Modifier.height(dim.espacio)
                )
            }
        }
}
