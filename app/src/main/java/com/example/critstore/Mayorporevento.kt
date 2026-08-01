package com.example.critstore

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun MayorVentaEvento(
    volver: () -> Unit
) {
    val configuration = LocalConfiguration.current
    val esTablet = configuration.screenWidthDp >= 600
    val padding = if (esTablet) 30.dp else 16.dp
    val espacio = if (esTablet) 20.dp else 12.dp
    val titulo = if (esTablet) 30.sp else 24.sp
    val texto = if (esTablet) 20.sp else 16.sp
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
            .padding(padding)
    ) {
        Spacer(
            modifier = Modifier.height(5.dp)
        )
        Text(
            text = "🏆 Mayor venta por Evento",
            fontSize = titulo,
            fontWeight = FontWeight.Bold
        )
        Spacer(
            modifier = Modifier.height(espacio)
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
                            modifier = Modifier.height(16.dp)
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
                        text = "No hay datos recibidos de Google",
                        fontSize = texto
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
                                modifier = Modifier.padding(padding)
                            ) {
                                Text(
                                    text = "🎪 ${item.nombreEvento}",
                                    fontSize = texto,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(
                                    modifier = Modifier.height(8.dp)
                                )
                                Text(
                                    text = "🛒 ${item.producto}",
                                    fontSize = texto
                                )
                                                          Spacer(
                                    modifier = Modifier.height(8.dp)
                                )
                                Text(
                                    text = "📦 Cantidad total: ${item.cantidadTotal}",
                                    fontSize = texto
                                )
                            }
                        }
                    }
                }
            }
        }
        Spacer(
            modifier = Modifier.height(5.dp)
        )
        Button(
            onClick = volver,
            modifier = Modifier
                .fillMaxWidth()
                .height(if (esTablet) 60.dp else 52.dp)
        ) {
            Text(
                text = "⬅ Volver",
                fontSize = texto
            )
        }
    }
}
