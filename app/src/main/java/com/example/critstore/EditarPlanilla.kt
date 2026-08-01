package com.example.critstore

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

@Composable
fun EditarPlanilla(
    planilla: PlanillaVenta,
    planillaDao: PlanillaDao,
    productoDao: ProductoDao,
    volver: () -> Unit
) {
    val context =
        LocalContext.current
    val scope =
        rememberCoroutineScope()
    val configuracion =
        LocalConfiguration.current
    val anchoPantalla =
        configuracion.screenWidthDp
    val esTablet =
        anchoPantalla >= 600
    val paddingPantalla =
        if(esTablet) 32.dp else 16.dp
    val tamañoTitulo =
        if(esTablet) 32.sp else 24.sp
    val tamañoTexto =
        if(esTablet) 18.sp else 14.sp
    val alturaBoton =
        if(esTablet) 60.dp else 50.dp
    var mostrarAgregarProductos by remember {
        mutableStateOf(false)
    }
    var productosVenta by remember {
        mutableStateOf(
            emptyList<Producto>()
        )
    }
    var productosDisponibles by remember {
        mutableStateOf(
            emptyList<Producto>()
        )
    }
    var detallesGuardados by remember {
        mutableStateOf(
            emptyList<DetallePlanilla>()
        )
    }
    var cantidadesVenta by remember {
        mutableStateOf(
            mutableMapOf<String,String>()
        )
    }
    LaunchedEffect(planilla.id) {
        detallesGuardados =
            planillaDao.obtenerDetallePlanilla(
                planilla.id
            )
        productosDisponibles =
            productoDao.obtenerProductos()
        productosVenta =
            productosDisponibles.filter { producto ->
                detallesGuardados.any {
                    it.Nombre.trim()
                        .equals(
                            producto.Nombre.trim(),
                            ignoreCase = true
                        )
                }
            }
        cantidadesVenta =
            productosVenta.associate { producto ->
                val detalle =
                    detallesGuardados.find {
                        it.Nombre.trim()
                            .equals(
                                producto.Nombre.trim(),
                                ignoreCase = true
                            )
                    }
                producto.uuid to
                        (
                                detalle?.Ventas
                                    ?.toString()
                                    ?: "0"
                                )
            }.toMutableMap()
    }
    if (mostrarAgregarProductos) {
        AgregarProductos(
            productoDao = productoDao,
            agregarProducto = { producto ->
                val existe =
                    productosVenta.any {
                        it.uuid == producto.uuid
                    }
                if(!existe) {
                    productosVenta =
                        productosVenta + producto
                    cantidadesVenta =
                        cantidadesVenta
                            .toMutableMap()
                            .apply {
                                put(
                                    producto.uuid,
                                    "0"
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
                paddingPantalla
            )
    ) {
        Text(
            text = "Editar Planilla",
            fontSize = tamañoTitulo,
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(
            modifier = Modifier.height(12.dp)
        )
        Text(
            text =
                "Evento: ${planilla.NombreEvento}",
            fontSize = tamañoTexto
        )
        Text(
            text =
                "Fecha Desde: ${planilla.FechaDesde}",
            fontSize = tamañoTexto
        )
        Text(
            text =
                "Fecha Hasta: ${planilla.FechaHasta}",
            fontSize = tamañoTexto
        )
        Spacer(
            modifier = Modifier.height(15.dp)
        )
        Button(
            onClick = {
                mostrarAgregarProductos = true
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(
                    alturaBoton
                )
        ) {
            Text(
                "Agregar Productos",
                fontSize = tamañoTexto
            )
        }
        Spacer(
            modifier = Modifier.height(15.dp)
        )
        Card(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Producto",
                    modifier = Modifier.weight(2f),
                    fontSize = tamañoTexto
                )
                Text(
                    text = "Cantidad",
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                    fontSize = tamañoTexto
                )
                Text(
                    text = "Total",
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                    fontSize = tamañoTexto
                )
            }
        }
        Spacer(
            modifier = Modifier.height(8.dp)
        )
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
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
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            vertical = 5.dp
                        ),
                    elevation =
                        CardDefaults.cardElevation(
                            defaultElevation = 3.dp
                        )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                if(esTablet)
                                    20.dp
                                else
                                    12.dp
                            )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            verticalAlignment =
                                Alignment.CenterVertically
                        ) {
                            Text(
                                text = producto.Nombre,
                                fontSize = tamañoTexto,
                                maxLines = 1,
                                overflow =
                                    TextOverflow.Ellipsis,
                                modifier =
                                    Modifier.weight(1f)
                            )
                            Button(
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
                                    if(esTablet)
                                        55.dp
                                    else
                                        45.dp
                                ),
                                contentPadding =
                                    PaddingValues(0.dp)
                            ) {
                                Text(
                                    "❌"
                                )
                            }
                        }
                        Spacer(
                            modifier = Modifier.height(10.dp)
                        )
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalArrangement =
                                Arrangement.SpaceBetween
                        ) {
                            Text(
                                text =
                                    "Stock: ${producto.Cantidad}",
                                fontSize = tamañoTexto
                            )
                            Text(
                                text =
                                    "Precio: $${producto.Precio}",
                                fontSize = tamañoTexto
                            )
                        }
                        Spacer(
                            modifier = Modifier.height(10.dp)
                        )
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            verticalAlignment =
                                Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Cantidad:",
                                fontSize = tamañoTexto
                            )
                            Spacer(
                                modifier = Modifier.width(10.dp)
                            )
                            OutlinedTextField(
                                value =
                                    cantidadesVenta[producto.uuid]
                                        ?: "0",
                                onValueChange = { valor ->
                                    cantidadesVenta =
                                        cantidadesVenta
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
                                modifier = Modifier
                                    .width(
                                        if(esTablet)
                                            150.dp
                                        else
                                            110.dp
                                    )
                                    .height(
                                        if(esTablet)
                                            65.dp
                                        else
                                            55.dp
                                    ),
                                singleLine = true,
                                textStyle =
                                    LocalTextStyle.current.copy(
                                        textAlign =
                                            TextAlign.Center,
                                        fontSize = tamañoTexto
                                    )
                            )
                        }
                        Spacer(
                            modifier = Modifier.height(8.dp)
                        )
                        Text(
                            text =
                                "Stock nuevo: $stockNuevo",
                            fontSize = tamañoTexto
                        )
                        Text(
                            text =
                                "Total: $$totalProducto",
                            fontSize = tamañoTexto,
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
                producto.Precio *
                        cantidad
            }
        Text(
            text =
                "TOTAL VENTA: $$totalVentaNueva",
            fontSize = tamañoTitulo,
            style =
                MaterialTheme.typography.headlineSmall
        )
        Spacer(
            modifier = Modifier.height(15.dp)
        )
        Button(
            onClick = {
                scope.launch {
                    var totalFinal = 0
                    val detallesAnteriores =
                        planillaDao.obtenerDetallePlanilla(
                            planilla.id
                        )
                    detallesAnteriores.forEach { detalle ->
                        val productoAnterior =
                            productoDao.obtenerProductos()
                                .find {
                                    it.Nombre.trim()
                                        .equals(
                                            detalle.Nombre.trim(),
                                            ignoreCase = true
                                        )
                                }
                        if (productoAnterior != null) {
                            val stockDevuelto =
                                productoAnterior.Cantidad + detalle.Ventas
                            productoDao.actualizarCantidad(
                                productoAnterior.uuid,
                                stockDevuelto
                            )
                            actualizarStockGoogle(
                                productoAnterior.uuid,
                                stockDevuelto
                            )
                        }
                    }
                    planillaDao.eliminarDetallesPlanilla(
                        planilla.id
                    )
                    productosVenta.forEach { producto ->
                        val cantidadNueva =
                            cantidadesVenta[producto.uuid]
                                ?.toIntOrNull()
                                ?: 0
                        val totalProducto =
                            producto.Precio * cantidadNueva
                        totalFinal += totalProducto
                        val detalleNuevo =
                            DetallePlanilla(
                                idPlanilla = planilla.id,
                                Nombre = producto.Nombre.trim(),
                                Precio = producto.Precio,
                                Ventas = cantidadNueva,
                                Total = totalProducto
                            )
                        planillaDao.insertarDetalle(
                            detalleNuevo
                        )
                        val productoActual =
                            productoDao.obtenerProductos()
                                .find {
                                    it.uuid == producto.uuid
                                }
                        if (productoActual != null) {
                            val stockFinal =
                                productoActual.Cantidad - cantidadNueva
                            productoDao.actualizarCantidad(
                                producto.uuid,
                                stockFinal
                            )
                            actualizarStockGoogle(
                                producto.uuid,
                                stockFinal
                            )
                        }
                    }
                    planillaDao.actualizarPlanilla(
                        planilla.copy(
                            TotalVenta = totalFinal
                        )
                    )
                    Toast.makeText(
                        context,
                        "✅ Planilla actualizada correctamente",
                        Toast.LENGTH_LONG
                    ).show()
                    volver()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(
                    alturaBoton
                )
        ) {
            Text(
                "💾 Guardar Cambios",
                fontSize = tamañoTexto
            )
        }
        Spacer(
            modifier = Modifier.height(10.dp)
        )
        Button(
            onClick = volver,
            modifier = Modifier
                .fillMaxWidth()
                .height(
                    alturaBoton
                )
        ) {
            Text(
                "⬅ Volver",
                fontSize = tamañoTexto
            )
        }
    }
}