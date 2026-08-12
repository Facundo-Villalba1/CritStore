package com.example.critstore

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.*

@Composable
fun EditarPlanilla(
    planilla: PlanillaVenta,
    planillaDao: PlanillaDao,
    productoDao: ProductoDao,
    volver: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val dim = obtenerDimensiones()
    var mostrarAgregarProductos by remember {
        mutableStateOf(false)
    }
    var productosVenta by remember {
        mutableStateOf(emptyList<Producto>())
    }
    var productosDisponibles by remember {
        mutableStateOf(emptyList<Producto>())
    }
    var detallesGuardados by remember {
        mutableStateOf(emptyList<DetallePlanilla>())
    }
    var cantidadesVenta by remember {
        mutableStateOf(
            mutableMapOf<String, String>()
        )
    }
    var guardando by remember {
        mutableStateOf(false)
    }
    LaunchedEffect(planilla.id) {
        detallesGuardados =
            planillaDao.obtenerDetallePlanilla(planilla.id)
        productosDisponibles =
            productoDao.obtenerProductos()
        productosVenta =
            productosDisponibles.filter { producto ->
                detallesGuardados.any {
                    it.Nombre.trim().equals(
                        producto.Nombre.trim(),
                        ignoreCase = true
                    )
                }
            }
        cantidadesVenta =
            productosVenta.associate { producto ->
                val detalle =
                    detallesGuardados.find {
                        it.Nombre.trim().equals(
                            producto.Nombre.trim(),
                            ignoreCase = true
                        )
                    }
                producto.uuid to
                        (
                                detalle?.Ventas?.toString()
                                    ?: "0"
                                )
            }.toMutableMap()
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
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                top = dim.paddingPantalla * 2,
                start = dim.paddingPantalla,
                end = dim.paddingPantalla,
                bottom = dim.paddingPantalla
            )
    ) {
        Text(
            text = "Editar Planilla",
            fontSize = dim.titulo,
            style =
                MaterialTheme.typography.headlineMedium
        )
        Spacer(
            modifier = Modifier.height(dim.espacio)
        )
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier =
                    Modifier.padding(16.dp)
            ) {
                Text(
                    text =
                        "Evento: ${planilla.NombreEvento}",
                    fontSize = dim.texto
                )
                Spacer(
                    modifier =
                        Modifier.height(dim.espacio)
                )
                Text(
                    text =
                        "Fecha Desde: ${planilla.FechaDesde}",
                    fontSize = dim.texto
                )
                Spacer(
                    modifier =
                        Modifier.height(dim.espacio)
                )
                Text(
                    text =
                        "Fecha Hasta: ${planilla.FechaHasta}",
                    fontSize = dim.texto
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
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(dim.alturaBotonInterno)
        ) {
            Text(
                text = "Agregar Productos",
                fontSize = dim.texto
            )
        }
        Spacer(
            modifier = Modifier.height(dim.espacio)
        )
        // Separación sin Card ni barra gris
        Spacer(
            modifier = Modifier.height(dim.espacio)
        )
        LazyColumn(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .weight(1f),
            verticalArrangement =
                Arrangement.spacedBy(8.dp)
        ) {
            items(productosVenta) { producto ->
                val cantidadVendida =
                    cantidadesVenta[producto.uuid]
                        ?.toIntOrNull()
                        ?: 0
                val totalProducto =
                    producto.Precio *
                            cantidadVendida
                val stockNuevo =
                    producto.Cantidad
                Card(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(vertical = 5.dp),
                    elevation =
                        CardDefaults.cardElevation(
                            defaultElevation = 3.dp
                        )
                ) {
                    Column(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .padding(
                                    dim.paddingPantalla
                                )
                    ) {
                        Row(
                            modifier =
                                Modifier.fillMaxWidth(),
                            verticalAlignment =
                                Alignment.CenterVertically
                        ) {
                            Text(
                                text = producto.Nombre,
                                modifier =
                                    Modifier.weight(1f),
                                fontSize = dim.texto,
                                maxLines = 1,
                                overflow =
                                    TextOverflow.Ellipsis
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
                                    imageVector =
                                        Icons.Default.Delete,
                                    contentDescription =
                                        "Eliminar producto"
                                )
                            }
                        }
                        Spacer(
                            modifier =
                                Modifier.height(dim.espacio)
                        )
                        Row(
                            modifier =
                                Modifier.fillMaxWidth(),
                            horizontalArrangement =
                                Arrangement.SpaceBetween
                        ) {
                            Text(
                                text =
                                    "Stock: ${producto.Cantidad}",
                                fontSize = dim.texto
                            )
                            Text(
                                text =
                                    "Precio: $${producto.Precio}",
                                fontSize = dim.texto
                            )
                        }
                        Spacer(
                            modifier =
                                Modifier.height(dim.espacio)
                        )
                        Row(
                            modifier =
                                Modifier.fillMaxWidth(),
                            verticalAlignment =
                                Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Cantidad:",
                                fontSize = dim.texto
                            )
                            Spacer(
                                modifier =
                                    Modifier.width(dim.espacio)
                            )
                            Row(
                                verticalAlignment =
                                    Alignment.CenterVertically,
                                horizontalArrangement =
                                    Arrangement.spacedBy(2.dp)
                            ) {
                                IconButton(
                                    onClick = {
                                        val actual =
                                            cantidadesVenta[producto.uuid]
                                                ?.toIntOrNull()
                                                ?: 0
                                        if (actual > 0) {
                                            cantidadesVenta =
                                                cantidadesVenta
                                                    .toMutableMap()
                                                    .apply {
                                                        put(
                                                            producto.uuid,
                                                            (
                                                                    actual - 1
                                                                    ).toString()
                                                        )
                                                    }
                                        }
                                    }
                                ) {
                                    Icon(
                                        imageVector =
                                            Icons.Default.Remove,
                                        contentDescription =
                                            "Restar"
                                    )
                                }
                                Text(
                                    text = cantidadVendida.toString(),
                                    modifier = Modifier.width(25.dp),
                                    fontSize = dim.texto,
                                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                                )
                                IconButton(
                                    onClick = {
                                        val actual =
                                            cantidadesVenta[producto.uuid]
                                                ?.toIntOrNull()
                                                ?: 0
                                        cantidadesVenta =
                                            cantidadesVenta
                                                .toMutableMap()
                                                .apply {
                                                    put(
                                                        producto.uuid,
                                                        (
                                                                actual + 1
                                                                ).toString()
                                                    )
                                                }
                                    }
                                ) {
                                    Icon(
                                        imageVector =
                                            Icons.Default.Add,
                                        contentDescription =
                                            "Sumar"
                                    )
                                }
                            }
                        }
                        Spacer(
                            modifier =
                                Modifier.height(dim.espacio)
                        )
                        Text(
                            text =
                                "Stock nuevo: $stockNuevo",
                            fontSize = dim.texto
                        )
                        Text(
                            text =
                                "Total: $$totalProducto",
                            fontSize = dim.texto,
                            style =
                                MaterialTheme.typography.titleMedium
                        )
                    }
                }
            }
        }
        val totalVentaNueva =
            productosVenta.sumOf { producto ->
                val cantidad =
                    cantidadesVenta[producto.uuid]
                        ?.toIntOrNull()
                        ?: 0
                producto.Precio * cantidad
            }
        Text(
            text =
                "TOTAL VENTA: $$totalVentaNueva",
            fontSize = dim.titulo,
            style =
                MaterialTheme.typography.headlineSmall
        )
        Spacer(
            modifier =
                Modifier.height(dim.espacio)
        )
        Button(
            onClick = {
                if (guardando)
                    return@Button
                scope.launch {
                    try {
                        guardando = true
                        var totalFinal = 0
                        val productosModificados =
                            mutableSetOf<String>()
                        val productosBD =
                            productoDao.obtenerProductos()
                        val detallesAnteriores =
                            planillaDao.obtenerDetallePlanilla(
                                planilla.id
                            )
                        val stockIncorrecto =
                            productosVenta.any { producto ->
                                val cantidadNueva =
                                    cantidadesVenta[producto.uuid]
                                        ?.toIntOrNull()
                                        ?: 0
                                val cantidadAnterior =
                                    detallesAnteriores.find {
                                        it.Nombre.trim()
                                            .equals(
                                                producto.Nombre.trim(),
                                                ignoreCase = true
                                            )
                                    }?.Ventas ?: 0
                                val stockDisponible =
                                    producto.Cantidad +
                                            cantidadAnterior
                                stockDisponible < cantidadNueva
                            }
                        if (stockIncorrecto) {
                            Toast.makeText(
                                context,
                                "❌ No hay stock suficiente para guardar la planilla",
                                Toast.LENGTH_LONG
                            ).show()
                            guardando = false
                            return@launch
                        }
                        detallesAnteriores.forEach { detalle ->
                            val productoAnterior =
                                productosBD.find {
                                    it.Nombre.trim()
                                        .equals(
                                            detalle.Nombre.trim(),
                                            ignoreCase = true
                                        )
                                }
                            if (productoAnterior != null) {
                                val nuevoStock =
                                    productoAnterior.Cantidad +
                                            detalle.Ventas
                                productoDao.actualizarCantidad(
                                    productoAnterior.uuid,
                                    nuevoStock
                                )
                                productosModificados.add(
                                    productoAnterior.uuid
                                )
                            }
                        }
                        planillaDao.eliminarDetallesPlanilla(
                            planilla.id
                        )
                        val productosActualizados =
                            productoDao.obtenerProductos()
                                .associateBy {
                                    it.uuid
                                }
                        productosVenta.forEach { producto ->
                            val cantidadNueva =
                                cantidadesVenta[producto.uuid]
                                    ?.toIntOrNull()
                                    ?: 0
                            if (cantidadNueva > 0) {
                                val totalProducto =
                                    producto.Precio *
                                            cantidadNueva
                                totalFinal += totalProducto
                                planillaDao.insertarDetalle(
                                    DetallePlanilla(
                                        idPlanilla =
                                            planilla.id,
                                        Uudd =
                                            planilla.Uudd,
                                        Nombre =
                                            producto.Nombre.trim(),
                                        Precio =
                                            producto.Precio,
                                        Ventas =
                                            cantidadNueva,
                                        Total =
                                            totalProducto
                                    )
                                )
                                val productoBD =
                                    productosActualizados[producto.uuid]
                                if (productoBD != null) {
                                    val nuevoStock =
                                        productoBD.Cantidad -
                                                cantidadNueva
                                    productoDao.actualizarCantidad(
                                        productoBD.uuid,
                                        nuevoStock
                                    )
                                    productosModificados.add(
                                        productoBD.uuid
                                    )
                                }
                            }
                        }
                        planillaDao.actualizarPlanilla(
                            planilla.copy(
                                totalVenta = totalFinal
                            )
                        )
                        val productosParaSincronizar =
                            productoDao.obtenerProductos()
                                .filter {
                                    productosModificados.contains(
                                        it.uuid
                                    )
                                }
                        Toast.makeText(
                            context,
                            "✅ Planilla guardada correctamente",
                            Toast.LENGTH_LONG
                        ).show()
                        volver()
                        CoroutineScope(
                            Dispatchers.IO
                        ).launch {
                            try {
                                actualizarStocksGooglee(
                                    productosParaSincronizar
                                        .distinctBy {
                                            it.uuid
                                        }
                                )
                            } catch (e: Exception) {
                            }
                        }
                    } catch (e: Exception) {
                        Toast.makeText(
                            context,
                            "Error al guardar: ${e.message}",
                            Toast.LENGTH_LONG
                        ).show()
                    } finally {
                        guardando = false
                    }
                }
            },
            modifier =Modifier
                .widthIn(dim.alturaBotonInterno)
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally),
            enabled =
                !guardando
        ) {
            Text(
                text =
                    if (guardando)
                        "Guardando..."
                    else
                        "💾 Guardar Cambios",
                fontSize = dim.texto
            )
        }
        Spacer(
            modifier =
                Modifier.height(dim.espacio)
        )
        OutlinedButton(
            onClick = volver,
            modifier =Modifier
                .widthIn(dim.alturaBotonInterno)
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally)
        ) {
            Text(
                text =
                    "⬅ Volver",
                fontSize =
                    dim.texto
            )
        }
        Spacer(
            modifier =
                Modifier.height(dim.espacio)
        )
    }
}