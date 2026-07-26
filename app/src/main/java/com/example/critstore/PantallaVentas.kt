package com.example.critstore

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@Composable
fun PantallaVentas(
    volver: () -> Unit,
    generarPlanilla: () -> Unit,
    reporteVentas: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(30.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Ventas",
            style = MaterialTheme.typography.headlineLarge
        )
        Button(
            onClick = generarPlanilla,
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth()
        ) {
            Text("📝 Generar Nueva Planilla")
        }
        Button(
            onClick = reporteVentas,
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth()
        ) {
            Text("📊 Reporte de Ventas")
        }
        Button(
            onClick = volver,
            modifier = Modifier.padding(10.dp)
        ) {
            Text("⬅ Volver")
        }
    }
}