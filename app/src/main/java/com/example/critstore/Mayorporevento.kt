package com.example.critstore

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.text.font.*
import androidx.compose.ui.unit.*

@Composable
fun MayorVentaEvento(
    volver: () -> Unit
) {
    val dim = obtenerDimensiones()
    var reporte by remember {
        mutableStateOf<List<ReporteEvento>>(emptyList())
    }
    var cargando by remember {
        mutableStateOf(true)
    }
    LaunchedEffect(Unit) {
        try {
            reporte = obtenerMayorVentaEvento()
        } finally {
            cargando = false
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = dim.paddingPantalla * 2,
                     start = dim.paddingPantalla,
                     end = dim.paddingPantalla,
                     bottom = dim.paddingPantalla)
    ) {
        Spacer( modifier = Modifier.height(dim.espacio) )
        Text(
            text = "🏆 Mayor venta por Evento",
            fontSize = dim.titulo,
            fontWeight = FontWeight.Bold
        )
        Spacer(
            modifier = Modifier.height(dim.espacio)
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
                        text = "No hay datos recibidos de Google",
                        fontSize = dim.texto
                    )
                }
            }
            else -> {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(reporte) { item ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            elevation = CardDefaults.cardElevation(
                                defaultElevation = 6.dp
                            )
                        ) {
                            Column(
                                modifier = Modifier.padding(dim.paddingPantalla)
                            ) {
                                Text(
                                    text = "🎪 ${item.nombreEvento}",
                                    fontSize = dim.texto,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer( modifier = Modifier.height(dim.espacio) )
                                Text(
                                    text = "🛒 ${item.producto}",
                                    fontSize = dim.texto
                                )
                                Spacer( modifier = Modifier.height(dim.espacio) )
                                Text(
                                    text = "📦 Cantidad total: ${item.cantidadTotal}",
                                    fontSize = dim.texto
                                )
                            }
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
