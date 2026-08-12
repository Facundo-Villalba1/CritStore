package com.example.critstore
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*

@Composable
fun PantallaVentas(
    volver: () -> Unit,
    generarPlanilla: () -> Unit,
    reporteVentas: () -> Unit
) {
    val dim = obtenerDimensiones()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = dim.paddingPantalla * 2,
                start = dim.paddingPantalla,
                end = dim.paddingPantalla,
                bottom = dim.paddingPantalla),
        horizontalAlignment =
            Alignment.CenterHorizontally,
        verticalArrangement =
            Arrangement.Center
    ) {
        Text(
            text = "Ventas",
            fontSize = dim.titulo,
            style = MaterialTheme.typography.headlineLarge
        )
        Spacer( modifier = Modifier.height(dim.espacio) )
        Button(
            onClick = generarPlanilla,
            modifier = Modifier
                .widthIn(
                dim.alturaBotonInterno
                )
        ) {
            Text(
                text = "📝 Generar Nueva Planilla",
                fontSize = dim.texto
            )
            Spacer( modifier = Modifier.height(dim.espacio) )
        }
        Button(
            onClick = reporteVentas,
            modifier = Modifier
                .widthIn(
               dim.alturaBoton
                )
        ) {
            Text(
                text = "📊 Reporte de Ventas",
                fontSize = dim.texto
            )
        }
        Spacer( modifier = Modifier.height(dim.espacio) )
        Button(
            onClick = volver,
            modifier = Modifier
                .widthIn(
                    dim.alturaBotonInterno
                )
        ) {
            Text(
                text = "⬅ Volver",
                fontSize = dim.texto
            )
        }
        Spacer( modifier = Modifier.height(dim.espacio) )
    }
}