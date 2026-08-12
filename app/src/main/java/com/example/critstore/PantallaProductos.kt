package com.example.critstore

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun PantallaProductos(
    volver: () -> Unit,
    ingresarProducto: () -> Unit,
    verProductos: () -> Unit,
    actualizarStock: () -> Unit
) {
    val dim = obtenerDimensiones()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(dim.paddingPantalla),
                horizontalAlignment =
            Alignment.CenterHorizontally,
        verticalArrangement =
            Arrangement.Center
    ) {
        Text(
            text = "Productos",
            fontSize = dim.titulo,
            style =
                MaterialTheme.typography.headlineLarge
        )
        Spacer(
            modifier = Modifier.height(
                dim.espacio
            )
        )
        Button(
            onClick = ingresarProducto,
            modifier = Modifier
                .widthIn(
                    dim.alturaBotonInterno
                )
        ) {
            Text(
                text = "📦 Ingresar Stock",
                fontSize = dim.texto
            )
        }
        Button(
            onClick = verProductos,
            modifier = Modifier
                .widthIn(
                    dim.alturaBotonInterno
                )
        ) {
            Text(
                text = "📋 Ver Productos",
                fontSize = dim.texto
            )
        }
        Button(
            onClick = actualizarStock,
            modifier = Modifier
                .widthIn(
                    dim.alturaBotonInterno
                )
        ) {
            Text(
                text = "🔄 Actualizar Stock",
                fontSize = dim.texto
            )
        }
        Spacer(
            modifier = Modifier.height(
                dim.espacio
            )
        )
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