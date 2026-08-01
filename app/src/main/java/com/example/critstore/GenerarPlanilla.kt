package com.example.critstore

import android.app.DatePickerDialog
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import java.util.Calendar

@Composable
fun GenerarPlanilla(
    productoDao: ProductoDao,
    planillaDao: PlanillaDao,
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
        if(esTablet)
            32.dp
        else
            16.dp
    val tamañoTitulo =
        if(esTablet)
            32.sp
        else
            24.sp
    val tamañoTexto =
        if(esTablet)
            18.sp
        else
            14.sp
    val alturaBoton =
        if(esTablet)
            60.dp
        else
            50.dp
    var nombreEvento by remember {
        mutableStateOf(
            TempPlanilla.nombreEvento
        )
    }
    var fechaDesde by remember {
        mutableStateOf(
            TempPlanilla.fechaDesde
        )
    }
    var fechaHasta by remember {
        mutableStateOf(
            TempPlanilla.fechaHasta
        )
    }
    var productosDisponibles by remember {
        mutableStateOf(
            emptyList<Producto>()
        )
    }
    var productosVenta by remember {
        mutableStateOf(
            emptyList<Producto>()
        )
    }
    var cantidadesVenta by remember {
        mutableStateOf(
            TempPlanilla.cantidadesVenta
        )
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
        val calendario =
            Calendar.getInstance()
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
    if(mostrarAgregarProductos){
        AgregarProductos(
            productoDao = productoDao,
            agregarProducto = { producto ->
                if(!productosVenta.contains(producto)){
                    productosVenta =
                        productosVenta + producto
                    cantidadesVenta =
                        cantidadesVenta
                            .toMutableMap()
                            .apply {
                                put(
                                    producto.uuid,
                                    ""
                                )
                            }
                }
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
                    ?.toIntOrNull()
                    ?: 0
            producto.Precio * cantidad
        }
    val hayStockNegativo =
        productosVenta.any { producto ->
            val cantidad =
                cantidadesVenta[producto.uuid]
                    ?.toIntOrNull()
                    ?: 0
            producto.Cantidad - cantidad < 0
        }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                paddingPantalla
            )
    ){
        Text(
            text = "Generar Nueva Planilla",
            fontSize = tamañoTitulo,
            style =
                MaterialTheme.typography.headlineMedium
        )
        Spacer(
            modifier =
                Modifier.height(15.dp)
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
                    fontSize = tamañoTexto
                )
            },
            modifier =
                Modifier.fillMaxWidth(),
            singleLine = true
        )
        Spacer(
            modifier =
                Modifier.height(12.dp)
        )
        FlowRow(
            modifier =
                Modifier.fillMaxWidth(),
            horizontalArrangement =
                Arrangement.spacedBy(10.dp),
            verticalArrangement =
                Arrangement.spacedBy(10.dp)
        ){
            Button(
                onClick = {
                    seleccionarFecha {
                        fechaDesde = it
                        TempPlanilla.fechaDesde = it
                    }
                },
                modifier =
                    if(esTablet)
                        Modifier
                            .weight(1f)
                            .height(alturaBoton)
                    else
                        Modifier
                            .fillMaxWidth()
                            .height(alturaBoton)
            ){
                Text(
                    if(fechaDesde.isEmpty())
                        "📅 Desde"
                    else
                        fechaDesde,
                    fontSize = tamañoTexto
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
                    if(esTablet)
                        Modifier
                            .weight(1f)
                            .height(alturaBoton)
                    else
                        Modifier
                            .fillMaxWidth()
                            .height(alturaBoton)
            ){
                Text(
                    if(fechaHasta.isEmpty())
                        "📅 Hasta"
                    else
                        fechaHasta,
                    fontSize = tamañoTexto
                )
            }
        }
        Spacer(
            modifier =
                Modifier.height(12.dp)
        )
        Button(
            onClick = {
                mostrarAgregarProductos = true
            },
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(
                        alturaBoton
                    )
        ){
            Text(
                "Agregar Productos",
                fontSize = tamañoTexto
            )
        }
        Spacer(
            modifier =
                Modifier.height(12.dp)
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
                val stockNuevo =
                    producto.Cantidad -
                            cantidadVendida
                val totalProducto =
                    producto.Precio *
                            cantidadVendida
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
                            modifier =
                                Modifier.fillMaxWidth(),
                            verticalAlignment =
                                Alignment.CenterVertically
                        ) {
                            Text(
                                text =
                                    producto.Nombre,
                                fontSize =
                                    tamañoTexto,
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
                            modifier =
                                Modifier.height(10.dp)
                        )
                        FlowRow(
                            modifier =
                                Modifier.fillMaxWidth(),
                            horizontalArrangement =
                                Arrangement.SpaceBetween
                        ){
                            Text(
                                text =
                                    "Stock: ${producto.Cantidad}",
                                fontSize =
                                    tamañoTexto
                            )
                            Text(
                                text =
                                    "Precio: $${producto.Precio}",
                                fontSize =
                                    tamañoTexto
                            )
                        }
                        Spacer(
                            modifier =
                                Modifier.height(10.dp)
                        )
                        Row(
                            modifier =
                                Modifier.fillMaxWidth(),
                            verticalAlignment =
                                Alignment.CenterVertically
                        ){
                            Text(
                                text =
                                    "Venta:",
                                fontSize =
                                    tamañoTexto
                            )
                            Spacer(
                                modifier =
                                    Modifier.width(10.dp)
                            )
                            OutlinedTextField(
                                value =
                                    cantidadesVenta[producto.uuid]
                                        ?: "",
                                onValueChange = {
                                    cantidadesVenta =
                                        cantidadesVenta
                                            .toMutableMap()
                                            .apply {
                                                put(
                                                    producto.uuid,
                                                    it.filter {
                                                            caracter ->
                                                        caracter.isDigit()
                                                    }
                                                )
                                            }
                                },
                                singleLine = true,
                                modifier = Modifier
                                    .width(
                                        if(esTablet)
                                            120.dp
                                        else
                                            90.dp
                                    )
                                    .height(
                                        if(esTablet)
                                            60.dp
                                        else
                                            50.dp
                                    )
                            )
                        }
                        Spacer(
                            modifier =
                                Modifier.height(10.dp)
                        )
                        Text(
                            text =
                                "Nuevo stock: $stockNuevo",
                            fontSize =
                                tamañoTexto
                        )
                        Text(
                            text =
                                "Total venta: $$totalProducto",
                            fontSize =
                                tamañoTexto,
                            style =
                                MaterialTheme.typography.titleMedium
                        )
                    }
                }
            }
        }
        Spacer(
            modifier =
                Modifier.height(12.dp)
        )
        Text(
            text =
                "TOTAL VENTA: $$totalVenta",
            fontSize =
                tamañoTitulo,
            style =
                MaterialTheme.typography.headlineSmall
        )
        Spacer(
            modifier =
                Modifier.height(15.dp)
        )
        Button(
            onClick = {
                if(nombreEvento.trim().isEmpty()) {
                    Toast.makeText(
                        context,
                        "Ingrese nombre del evento",
                        Toast.LENGTH_LONG
                    ).show()
                    return@Button
                }
                if(fechaDesde.trim().isEmpty()) {
                    Toast.makeText(
                        context,
                        "Seleccione fecha desde",
                        Toast.LENGTH_LONG
                    ).show()
                    return@Button
                }
                if(fechaHasta.trim().isEmpty()) {
                    Toast.makeText(
                        context,
                        "Seleccione fecha hasta",
                        Toast.LENGTH_LONG
                    ).show()
                    return@Button
                }
                if(productosVenta.isEmpty()) {
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
                                ?.toIntOrNull()
                                ?: 0
                        cantidad > 0
                    }
                if(!hayCantidadValida) {
                    Toast.makeText(
                        context,
                        "Ingrese cantidades de venta",
                        Toast.LENGTH_LONG
                    ).show()
                    return@Button
                }
                if(hayStockNegativo) {
                    Toast.makeText(
                        context,
                        "No hay suficiente stock",
                        Toast.LENGTH_LONG
                    ).show()
                    return@Button
                }
                scope.launch {
                    val planilla =
                        PlanillaVenta(
                            NombreEvento = nombreEvento.trim(),
                            FechaDesde = fechaDesde,
                            FechaHasta = fechaHasta,
                            TotalVenta = totalVenta
                        )
                    val idPlanilla =
                        planillaDao.insertarPlanilla(
                            planilla
                        )
                    productosVenta.forEach { producto ->
                        val cantidad =
                            cantidadesVenta[producto.uuid]
                                ?.toIntOrNull()
                                ?: 0
                        if(cantidad > 0) {
                            val detalle =
                                DetallePlanilla(
                                    idPlanilla =
                                        idPlanilla.toInt(),
                                    Nombre =
                                        producto.Nombre,
                                    Precio =
                                        producto.Precio,
                                    Ventas =
                                        cantidad,
                                    Total =
                                        producto.Precio * cantidad
                                )
                            planillaDao.insertarDetalle(
                                detalle
                            )
                            productoDao.descontarStock(
                                producto.uuid,
                                cantidad
                            )
                            val stockNuevo =
                                producto.Cantidad - cantidad
                            actualizarStockGoogle(
                                producto.uuid,
                                stockNuevo
                            )
                        }
                    }
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
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(
                    alturaBoton
                ),
            enabled = !hayStockNegativo
        ) {
            Text(
                "💾 Guardar Planilla",
                fontSize = tamañoTexto
            )
        }
        Spacer(
            modifier =
                Modifier.height(10.dp)
        )
        Button(
            onClick = volver,
            modifier = Modifier
                .fillMaxWidth()
                .height(
                    alturaBoton
                )
        ){
            Text(
                "⬅ Volver",
                fontSize =
                    tamañoTexto
            )
        }
    }
}