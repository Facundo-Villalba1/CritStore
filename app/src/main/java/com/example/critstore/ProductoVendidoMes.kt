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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductoMasVendidoMes(
    volver: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val dim =obtenerDimensiones()
       var reporte by remember {
        mutableStateOf<List<ProductoMasVendidoMes>>(emptyList())
    }
    var cargando by remember {
        mutableStateOf(false)
    }
    var fechaDesde by remember {
        mutableStateOf("")
    }
    var fechaHasta by remember {
        mutableStateOf("")
    }
    var mesSeleccionado by remember {
        mutableStateOf("")
    }
    fun seleccionarMes() {
        val calendario =Calendar.getInstance()
        DatePickerDialog( context,
            { _, anio, mes, _ ->
                val mesReal =
                    mes + 1
                val primerDia =  Calendar.getInstance()
                primerDia.set(
                    anio,
                    mes,
                    1
                )
                val ultimoDia =  Calendar.getInstance()
                ultimoDia.set(
                    anio,
                    mes,
                    ultimoDia.getActualMaximum(
                        Calendar.DAY_OF_MONTH
                    )
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
            .padding( top = dim.paddingPantalla * 2,
                start = dim.paddingPantalla,
                end = dim.paddingPantalla,
                bottom = dim.paddingPantalla)
    ) {
        Text(
            text =
                "📦 Productos más vendidos del mes",
            fontSize = dim.titulo,
            fontWeight = FontWeight.Bold
        )
        Spacer( modifier = Modifier.height(dim.espacio) )
        Button(
            onClick = {
                seleccionarMes()
            },
            modifier =
                Modifier.fillMaxWidth()
        ) {
            Text(
                text =
                    if(
                        mesSeleccionado.isEmpty()
                    )
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
                    reporte = obtenerProductosMasVendidosMes(
                        fechaDesde,
                        fechaHasta
                    )
                    cargando = false
                }
            },
            enabled = mesSeleccionado.isNotEmpty() && !cargando,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "🔍 Buscar",
                fontSize = dim.texto
            )
        }
        Spacer(modifier = Modifier.height(dim.espacio))
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            when {
                cargando -> {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator()
                        Spacer(modifier = Modifier.height(dim.espacio))
                        Text(
                            text = "Cargando datos...",
                            fontSize = dim.texto
                        )
                    }
                }
                reporte.isEmpty() -> {
                    Text(
                        text = if (mesSeleccionado.isEmpty())
                            "Seleccione un mes"
                        else
                            "No hay productos vendidos",
                        fontSize = dim.texto,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(dim.espacio)
                    ) {
                        items(reporte) { producto ->
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                elevation = CardDefaults.cardElevation(
                                    defaultElevation = 4.dp
                                )
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(dim.paddingPantalla),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "📦 ${producto.producto}",
                                        fontSize = dim.texto,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.weight(1f)
                                    )
                                    Spacer(modifier = Modifier.width(dim.espacio))
                                    Text(
                                        text = "Cantidad: ${producto.cantidadTotal}",
                                        fontSize = dim.texto
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(dim.espacio))
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
        Spacer(modifier = Modifier.height(dim.espacio))
    }
}
