package com.example.critstore

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ReporteVentas(
    planillaDao: PlanillaDao,
    verDetalle: (PlanillaVenta) -> Unit,
    volver: () -> Unit
) {
    val dim = obtenerDimensiones()
    var planillas by remember {
        mutableStateOf(emptyList<PlanillaVenta>())
    }
    LaunchedEffect(Unit) {
        planillas =
            planillaDao.obtenerPlanillas()
    }
  /*  var reportes  by remember {
        mutableStateOf<List<ReporteEvento>>(emptyList())
    }*/
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = dim.paddingPantalla * 2,
                start = dim.paddingPantalla,
                end = dim.paddingPantalla,
                bottom = dim.paddingPantalla
            )
    ) {
        Text(
            text = "Reporte de Ventas",
            fontSize =  dim.titulo,
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer( modifier = Modifier.height(dim.espacio) )
        LazyColumn(
            modifier = Modifier.weight(1f)
        ) {
            items(planillas) { planilla ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        top = 4.dp,
                        start = dim.paddingPantalla,
                        end = dim.paddingPantalla,
                        bottom = 4.dp
                        )
                ) {
                    Column(
                        modifier = Modifier.padding(dim.paddingPantalla)
                    ) {
                        Text(
                            text = planilla.NombreEvento,
                            fontSize = dim.texto
                        )
                        Text(
                            text =
                                "${planilla.FechaDesde} - ${planilla.FechaHasta}",
                                        fontSize = dim.texto
                        )
                        Text(
                            text =
                                "Total: $${planilla.totalVenta}",
                            fontSize = dim.texto
                        )
                        Button(
                            onClick = {
                                verDetalle(planilla)
                            }
                        )
                        {
                            Text( text = "📄 Ver detalle",
                                fontSize =  dim.texto)
                        }
                        Spacer( modifier = Modifier.height(dim.espacio) )
                    }
                }
            }
        }
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
