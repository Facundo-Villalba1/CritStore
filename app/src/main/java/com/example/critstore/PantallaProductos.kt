package com.example.critstore

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun PantallaProductos(
    volver: () -> Unit,
    ingresarProducto: () -> Unit,
    verProductos: () -> Unit,
    actualizarStock: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(30.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Productos",
            style = MaterialTheme.typography.headlineLarge
        )
        Button(
            onClick = ingresarProducto,
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth()
        ) {
            Text("📦 Ingresar Stock")
        }
        Button(
            onClick = verProductos,
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth()
        ) {
            Text("📋 Ver Productos")
        }
        Button(
            onClick = actualizarStock,
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth()
        ) {
            Text("🔄 Actualizar Stock")
        }
        Button(
            onClick = volver,
            modifier = Modifier.padding(10.dp)
        ) {
            Text("⬅ Volver")
        }
    }
}