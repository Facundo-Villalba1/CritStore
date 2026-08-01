package com.example.critstore

import android.app.DatePickerDialog
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@Composable
fun VendidoPorFecha(
    volver: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val configuration = LocalConfiguration.current
    val esTablet = configuration.screenWidthDp >= 600
    val padding = if (esTablet) 28.dp else 16.dp
    val titulo = if (esTablet) 30.sp else 24.sp
    val texto = if (esTablet) 20.sp else 16.sp
    val alturaBoton = if (esTablet) 60.dp else 52.dp
    var mesSeleccionado by remember {
        mutableStateOf("")
    }
    var fechaDesde by remember {
        mutableStateOf("")
    }
    var fechaHasta by remember {
        mutableStateOf("")
    }
    var cargando by remember {
        mutableStateOf(false)
    }
    var reporte by remember {
        mutableStateOf<List<ReportexFecha>>(emptyList())
    }
    fun seleccionarMes() {
        val calendario = Calendar.getInstance()
        DatePickerDialog(
            context,
            { _, anio, mes, _ ->
                val mesReal = mes + 1
                val primerDia = Calendar.getInstance()
                primerDia.set(anio, mes, 1)
                val ultimoDia = Calendar.getInstance()
                ultimoDia.set(
                    anio,
                    mes,
                    ultimoDia.getActualMaximum(Calendar.DAY_OF_MONTH)
                )
                val formato =
                    SimpleDateFormat(
                        "dd/MM/yyyy",
                        Locale.getDefault()
                    )
                fechaDesde = formato.format(primerDia.time)
                fechaHasta = formato.format(ultimoDia.time)
                mesSeleccionado =
                    "%02d/%04d".format(
                        mesReal,
                        anio
                    )
            },
            calendario.get(Calendar.YEAR),
            calendario.get(Calendar.MONTH),
            calendario.get(Calendar.DAY_OF_MONTH)
        ).show()
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
    ) {
        Text(
            text = "📅 Vendido por Mes",
            fontSize = titulo,
            fontWeight = FontWeight.Bold
        )
        Spacer(
            modifier = Modifier.height(20.dp)
        )
        Button(
            onClick = {
                seleccionarMes()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text =
                    if (mesSeleccionado.isEmpty())
                        "📅 Seleccionar mes"
                    else
                        "📅 $mesSeleccionado",
                fontSize = texto
            )
        }
        Spacer(
            modifier = Modifier.height(12.dp)
        )
        Button(
            onClick = {
                scope.launch {
                    cargando = true
                    reporte =
                        obtenerVentasPorFecha(
                            "",
                            ""
                        )
                    cargando = false
                }
            },
            enabled = mesSeleccionado.isNotEmpty(),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "🔍 Buscar",
                fontSize = texto
            )
        }
        Spacer(
            modifier = Modifier.height(20.dp)
        )
        when {
            cargando -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator()
                        Spacer(
                            modifier = Modifier.height(12.dp)
                        )
                        Text(
                            text = "Cargando datos...",
                            fontSize = texto
                        )
                    }
                }
            }
            reporte.isEmpty() -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text =
                            if (mesSeleccionado.isEmpty())
                                "Seleccione un mes"
                            else
                                "No hay ventas para ese mes",
                        fontSize = texto
                    )
                }
            }
            else -> {
                // Si totalVenta viene repetido por cada producto de la misma planilla,
                // evita duplicarlo tomando solo valores únicos.
                val totalMes =
                    reporte.sumOf { it.totalVenta }
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    LazyColumn(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(reporte) { item ->
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                elevation = CardDefaults.cardElevation(
                                    defaultElevation = 4.dp
                                )
                            ) {
                                Column(
                                    modifier = Modifier.padding(16.dp)
                                ) {
                                    Text(
                                        text = "🎪 ${item.nombreEvento}",
                                        fontSize = texto,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(
                                        modifier = Modifier.height(6.dp)
                                    )
                                    Text(
                                        text ="🧾 Total venta: ${item.totalVenta}",
                                        fontSize = texto
                                    )
                                }
                            }
                        }
                    }
                    Spacer(
                        modifier = Modifier.height(12.dp)
                    )
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 8.dp
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(
                                text = "💰 Total vendido",
                                fontSize = texto,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(
                                modifier = Modifier.height(8.dp)
                            )
                            Text(
                                text = "$ $totalMes",
                                fontSize = if (esTablet) 28.sp else 24.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
        Spacer(
            modifier = Modifier.height(8.dp)
        )
        Button(
            onClick = volver,
            modifier = Modifier
                .fillMaxWidth()
                .height(alturaBoton)
        ) {
            Text(
                text = "⬅ Volver",
                fontSize = texto
            )
        }
    }
}
