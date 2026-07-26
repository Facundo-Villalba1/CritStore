package com.example.critstore

import android.app.DatePickerDialog
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import java.util.Calendar

@Composable
fun GenerarPlanilla(
    productoDao: ProductoDao,
    planillaDao: PlanillaDao,
    volver: () -> Unit
) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
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
    var productos by remember {
        mutableStateOf(
            emptyList<Producto>()
        )
    }
    var cantidadesVenta by remember {
        mutableStateOf(
            TempPlanilla.cantidadesVenta
        )
    }
    LaunchedEffect(Unit) {
        productos =
            productoDao.obtenerProductos()
        productos.forEach { producto ->
            if (!cantidadesVenta.containsKey(producto.id)) {
                cantidadesVenta =
                    cantidadesVenta.toMutableMap().apply {
                        put(
                            producto.id,
                            ""
                        )
                    }
            }
        }
    }
    fun seleccionarFecha(
        cambiar: (String) -> Unit
    ) {
        val calendario =
            Calendar.getInstance()
        DatePickerDialog(
            context,
            { _, año, mes, dia ->
                cambiar(
                    "$dia/${mes + 1}/$año"
                )
            },
            calendario.get(Calendar.YEAR),
            calendario.get(Calendar.MONTH),
            calendario.get(Calendar.DAY_OF_MONTH)
        ).show()
    }
    val totalVenta =
        productos.sumOf { producto ->
            val cantidad =
                cantidadesVenta[producto.id]
                    ?.toIntOrNull()
                    ?: 0
            producto.Precio * cantidad
        }
    val hayStockNegativo =
        productos.any { producto ->
            val cantidad =
                cantidadesVenta[producto.id]
                    ?.toIntOrNull()
                    ?: 0
            producto.Cantidad - cantidad < 0
        }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Generar Nueva Planilla",
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(
            modifier = Modifier.height(10.dp)
        )
        OutlinedTextField(
            value = nombreEvento,
            onValueChange = {
                nombreEvento = it
                TempPlanilla.nombreEvento = it
            },
            label = {
                Text("Nombre del evento")
            },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(
            modifier = Modifier.height(8.dp)
        )
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(
                onClick = {
                    seleccionarFecha {
                        fechaDesde = it
                        TempPlanilla.fechaDesde = it
                    }
                },
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    if(fechaDesde.isEmpty())
                        "Desde"
                    else
                        fechaDesde
                )
            }
            Spacer(
                modifier = Modifier.width(10.dp)
            )
            Button(
                onClick = {
                    seleccionarFecha {
                        fechaHasta = it
                        TempPlanilla.fechaHasta = it
                    }
                },
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    if(fechaHasta.isEmpty())
                        "Hasta"
                    else
                        fechaHasta
                )
            }
        }
        Spacer(
            modifier = Modifier.height(15.dp)
        )
        // TABLA DE PRODUCTOS
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Producto",
                    modifier = Modifier.weight(2.5f)
                )
                Text(
                    text = "Stock",
                    modifier = Modifier.width(50.dp),
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "Precio",
                    modifier = Modifier.width(70.dp),
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "Ventas",
                    modifier = Modifier.width(45.dp),
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "Nuevo",
                    modifier = Modifier.width(55.dp),
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "Total",
                    modifier = Modifier.width(70.dp),
                    textAlign = TextAlign.Center
                )
            }
        }
        Spacer(
            modifier = Modifier.height(5.dp)
        )
        LazyColumn(
            modifier = Modifier.weight(1f)
        ) {
            items(productos) { producto ->
                val cantidadVendida =
                    cantidadesVenta[producto.id]
                        ?.toIntOrNull()
                        ?: 0
                val stockNuevo =
                    producto.Cantidad - cantidadVendida
                val totalProducto =
                    producto.Precio * cantidadVendida
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 3.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = producto.Nombre,
                            modifier = Modifier.weight(2.5f)
                        )
                        Text(
                            text = producto.Cantidad.toString(),
                            modifier = Modifier.width(50.dp),
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = "$${producto.Precio}",
                            modifier = Modifier.width(70.dp),
                            textAlign = TextAlign.Center
                        )
                        OutlinedTextField(
                            value = cantidadesVenta[producto.id] ?: "",
                            onValueChange = {
                                cantidadesVenta =
                                    cantidadesVenta
                                        .toMutableMap()
                                        .apply {
                                            put(
                                                producto.id,
                                                it
                                            )
                                        }
                                TempPlanilla.cantidadesVenta =
                                    cantidadesVenta
                            },
                            modifier = Modifier
                                .width(50.dp)
                                .height(54.dp),
                            singleLine = true,
                            textStyle = LocalTextStyle.current.copy(
                                textAlign = TextAlign.Center
                            )
                        )
                        Text(
                            text = stockNuevo.toString(),
                            modifier = Modifier.width(55.dp),
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = "$$totalProducto",
                            modifier = Modifier.width(70.dp),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
        Spacer(
            modifier = Modifier.height(10.dp)
        )
        Text(
            text = "TOTAL VENTA: $$totalVenta",
            style = MaterialTheme.typography.headlineSmall
        )
        Spacer(
            modifier = Modifier.height(10.dp)
        )
        Button(
            onClick = {
                if (!hayStockNegativo) {
                    scope.launch {
                        val idPlanilla =
                            planillaDao.insertarPlanilla(
                                PlanillaVenta(
                                    nombreEvento = nombreEvento,
                                    fechaDesde = fechaDesde,
                                    fechaHasta = fechaHasta,
                                    totalVenta = totalVenta
                                )
                            )
                        productos.forEach { producto ->
                            val cantidad =
                                cantidadesVenta[producto.id]
                                    ?.toIntOrNull()
                                    ?: 0
                            if (cantidad > 0) {
                                planillaDao.insertarDetalle(
                                    DetallePlanilla(
                                        idPlanilla = idPlanilla.toInt(),
                                        Nombre = producto.Nombre,
                                        precio = producto.Precio,
                                        Ventas = cantidad,
                                        total = producto.Precio * cantidad
                                    )
                                )
                                productoDao.descontarStock(
                                    producto.id,
                                    cantidad
                                )
                            }
                        }
                        TempPlanilla.limpiar()
                        productos =
                            productoDao.obtenerProductos()
                    }
                }
            },
            enabled = !hayStockNegativo,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("💾 Guardar Planilla")
        }
        Spacer(
            modifier = Modifier.height(8.dp)
        )
        Button(
            onClick = volver,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("⬅ Volver")
        }
    }
}