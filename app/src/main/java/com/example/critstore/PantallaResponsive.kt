package com.example.critstore

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.res.*

@Composable
fun PantallaCelular(
    productos: () -> Unit,
    ventas: () -> Unit,
    Reportes: () -> Unit,
    Presupuesto:()-> Unit,
    Materiales: () -> Unit
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
        Image(
            painter = painterResource(
                id = R.drawable.logo_critstore
            ),
            contentDescription =
                "Logo CritStore",
            modifier =
                Modifier.size(
                    dim.logo
                )
        )
        Spacer( modifier = Modifier.height(dim.espacio) )
        Text(
            text = "CritStore",
            fontSize = dim.titulo,
            style =MaterialTheme.typography.headlineLarge
        )
        Spacer( modifier = Modifier.height(dim.espacio) )
        Button(
            onClick = productos,
            modifier = Modifier
                .widthIn(
                    dim.alturaBotonInterno
                )
        ){
            Text(
                "📦 Productos",
                fontSize = dim.texto
            )
        }
        Spacer( modifier = Modifier.height(dim.espacio) )
        Button(
            onClick = Materiales,
            modifier = Modifier
                .widthIn(
                    dim.alturaBotonInterno)
        ) {
            Text(
                text = "🧵 Materiales",
                fontSize = dim.texto
            )
        }
        Spacer( modifier = Modifier.height(dim.espacio) )
        Button(
            onClick = ventas,
            modifier = Modifier
                .widthIn(
                    dim.alturaBotonInterno)
        )


        {
            Text(
                "🛒 Ventas",
                fontSize =dim.texto
            )
        }
        Spacer( modifier = Modifier.height(dim.espacio) )
        Button(
            onClick =Reportes,
            modifier = Modifier
                .widthIn(
                    dim.alturaBotonInterno )
        ){
            Text(
                text = "📊  Reportes",
                fontSize = dim.texto
            )
            Spacer( modifier = Modifier.height(dim.espacio) )
        }
        Spacer( modifier = Modifier.height(dim.espacio) )
        Button(
            onClick = Presupuesto,
            modifier = Modifier
                .widthIn(
                    dim.alturaBotonInterno )
        ){
            Text(
                text = "🏷️  Presupuesto",
                fontSize = dim.texto
            )
            Spacer( modifier = Modifier.height(dim.espacio) )
        }
    }
}