package com.example.critstore

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun PantallaProductos(
    volver: () -> Unit,
    ingresarProducto: () -> Unit,
    verProductos: () -> Unit,
    actualizarStock: () -> Unit
) {
    val configuracion = LocalConfiguration.current
    val anchoPantalla =
        configuracion.screenWidthDp
    val esTablet =
        anchoPantalla >= 600
    val paddingPantalla =
        if (esTablet)
            60.dp
        else
            30.dp
    val tamañoTitulo =
        if (esTablet)
            36.sp
        else
            28.sp
    val tamañoTexto =
        if (esTablet)
            22.sp
        else
            16.sp
    val anchoBotones =
        if (esTablet)
            450.dp
        else
            320.dp
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingPantalla),
        horizontalAlignment =
            Alignment.CenterHorizontally,
        verticalArrangement =
            Arrangement.Center
    ) {
        Text(
            text = "Productos",
            fontSize = tamañoTitulo,
            style =
                MaterialTheme.typography.headlineLarge
        )
        Spacer(
            modifier =
                Modifier.height(
                    if(esTablet)
                        40.dp
                    else
                        25.dp
                )
        )
        Button(
            onClick = ingresarProducto,
            modifier = Modifier
                .widthIn(
                    max = anchoBotones
                )
                .fillMaxWidth()
                .height(
                    if(esTablet)
                        65.dp
                    else
                        55.dp
                )
                .padding(vertical = 5.dp)
        ) {
            Text(
                text = "📦 Ingresar Stock",
                fontSize = tamañoTexto
            )
        }
        Button(
            onClick = verProductos,
            modifier = Modifier
                .widthIn(
                    max = anchoBotones
                )
                .fillMaxWidth()
                .height(
                    if(esTablet)
                        65.dp
                    else
                        55.dp
                )
                .padding(vertical = 5.dp)
        ) {
            Text(
                text = "📋 Ver Productos",
                fontSize = tamañoTexto
            )
        }
        Button(
            onClick = actualizarStock,
            modifier = Modifier
                .widthIn(
                    max = anchoBotones
                )
                .fillMaxWidth()
                .height(
                    if(esTablet)
                        65.dp
                    else
                        55.dp
                )
                .padding(vertical = 5.dp)
        ) {
            Text(
                text = "🔄 Actualizar Stock",
                fontSize = tamañoTexto
            )
        }
        Spacer(
            modifier =
                Modifier.height(
                    if(esTablet)
                        20.dp
                    else
                        10.dp
                )
        )
        Button(
            onClick = volver,
            modifier = Modifier
                .widthIn(
                    max = anchoBotones
                )
                .fillMaxWidth()
                .height(
                    if(esTablet)
                        65.dp
                    else
                        55.dp
                )
        ) {
            Text(
                text = "⬅ Volver",
                fontSize = tamañoTexto
            )
        }
    }
}