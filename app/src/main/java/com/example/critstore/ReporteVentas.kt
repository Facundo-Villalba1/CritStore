package com.example.critstore

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp


@Composable
fun ReporteVentas(
    planillaDao: PlanillaDao,
    verDetalle: (PlanillaVenta) -> Unit,
    volver: () -> Unit
) {
    val context = LocalContext.current
    var planillas by remember {
        mutableStateOf(emptyList<PlanillaVenta>())
    }
    LaunchedEffect(Unit) {
        planillas =
            planillaDao.obtenerPlanillas()
    }
    var reportes  by remember {
        mutableStateOf<List<ReporteEvento>>(emptyList())
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Reporte de Ventas",
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(
            modifier = Modifier.height(15.dp)
        )
        LazyColumn(
            modifier = Modifier.weight(1f)
        ) {
            items(planillas) { planilla ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(5.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(15.dp)
                    ) {
                        Text(
                            text = planilla.NombreEvento
                        )
                        Text(
                            text =
                                "${planilla.FechaDesde} - ${planilla.FechaHasta}"
                        )
                        Text(
                            text =
                                "Total: $${planilla.TotalVenta}"
                        )
                        Button(
                            onClick = {
                                verDetalle(planilla)
                            }
                        )
                        {
                            Text("📄 Ver detalle")
                        }
                    }
                }
            }
        }
       Button(
            onClick = volver,
            modifier = Modifier.fillMaxWidth()
        ) {
           Text("⬅ Volver")
       }
   } // cierre correcto del Column
}

