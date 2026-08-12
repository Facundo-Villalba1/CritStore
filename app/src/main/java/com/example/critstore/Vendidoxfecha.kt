package com.example.critstore

import android.app.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.platform.*
import androidx.compose.ui.text.font.*
import androidx.compose.ui.unit.*
import kotlinx.coroutines.*
import java.text.*
import java.util.*

@Composable
fun VendidoPorFecha(
    volver: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val dim =obtenerDimensiones()
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
            .padding(    top = dim.paddingPantalla * 2,
                start = dim.paddingPantalla,
                end = dim.paddingPantalla,
                bottom = dim.paddingPantalla)
    ) {
        Text(
            text = "📅 Vendido por Mes",
            fontSize = dim.titulo,
            fontWeight = FontWeight.Bold
        )
        Spacer( modifier = Modifier.height(dim.espacio) )
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
                fontSize = dim.texto
            )
        }
        Spacer( modifier = Modifier.height(dim.espacio) )
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
                fontSize = dim.texto
            )
        }
        Spacer( modifier = Modifier.height(dim.espacio) )
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
                        Spacer( modifier = Modifier.height(dim.espacio) )
                        Text(
                            text = "Cargando datos...",
                            fontSize = dim.texto
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
                        fontSize = dim.texto
                    )
                }
            }
            else -> {
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
                                        fontSize = dim.texto,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer( modifier = Modifier.height(dim.espacio) )
                                    Text(
                                        text ="🧾 Total venta: ${item.totalVenta}",
                                        fontSize = dim.texto
                                    )
                                }
                            }
                        }
                    }
                    Spacer( modifier = Modifier.height(dim.espacio) )
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
                                fontSize = dim.texto,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer( modifier = Modifier.height(dim.espacio) )
                            Text(
                                text = "$ $totalMes",
                                fontSize = dim.titulo,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
        Spacer( modifier = Modifier.height(dim.espacio) )
        Button(
            onClick = volver,
            modifier =Modifier
                .widthIn(dim.alturaBotonInterno)
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally)
        ) {
            Text(
                text = "⬅ Volver",
                fontSize = dim.texto
            )
        }
        Spacer( modifier = Modifier.height(dim.espacio) )
    }
}
