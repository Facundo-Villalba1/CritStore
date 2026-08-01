package com.example.critstore

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun Reportes(
    volver: () -> Unit,
    mayorVentaEvento: () -> Unit,
    mayorVendidoFecha: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "📊 Reportes",
            fontSize = 28.sp
        )
        Spacer(
            modifier = Modifier.height(30.dp)
        )
        Button(
            onClick = mayorVentaEvento,
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
        ) {
            Text(
                text = "📊 Mayor venta por Evento",
                fontSize = 18.sp
            )
        }
        Spacer(
            modifier = Modifier.height(20.dp)
        )

        Button(
            onClick = mayorVendidoFecha,
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
        ) {
            Text(
                text = "📅 Vendido por fecha",
                fontSize = 18.sp
            )
        }
        Spacer(
            modifier = Modifier.height(30.dp)
        )
        OutlinedButton(
            onClick = volver,
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp)
        ) {
            Text(
                text = "⬅ Volver"
            )
        }
    }
}