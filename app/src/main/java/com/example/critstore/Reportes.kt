package com.example.critstore

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.*


@Composable
fun Reportes(
    volver: () -> Unit,
    mayorVentaEvento: () -> Unit,
    mayorVendidoFecha: () -> Unit,
    productoMasVendidoMes: () -> Unit
) {
    val dim = obtenerDimensiones()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(dim.paddingPantalla),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "📊 Reportes",
            fontSize = dim.titulo,
            style= MaterialTheme.typography.headlineLarge
        )
        Spacer( modifier = Modifier.height(dim.espacio) )
        Button(
            onClick = mayorVentaEvento,
            modifier = Modifier
                .widthIn(
                    dim.alturaBotonInterno)
        ) {
            Text(
                text = "📊 Mayor venta por Evento",
                fontSize = dim.texto
            )
        }
        Spacer( modifier = Modifier.height(dim.espacio) )
        Button(
            onClick = mayorVendidoFecha,
            modifier = Modifier
                .widthIn(
                    dim.alturaBotonInterno)
        ) {
            Text(
                text = "📅 Vendido por fecha",
                fontSize = dim.texto
            )
        }
        Spacer( modifier = Modifier.height(dim.espacio) )
        Button(
            onClick = productoMasVendidoMes,
            modifier = Modifier
                .widthIn(
                    dim.alturaBotonInterno)
        ) {
            Text(
                text = "📅 Producto por Mes",
                fontSize = dim.texto
            )
        }
        Spacer( modifier = Modifier.height(dim.espacio) )
        OutlinedButton(
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