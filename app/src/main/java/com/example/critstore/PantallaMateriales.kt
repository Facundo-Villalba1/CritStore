
package com.example.critstore

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun PantallaMateriales(
    volver: () -> Unit,
    verMateriales: () -> Unit,
    actualizarMateriales: () -> Unit
) {
    val dim =
        obtenerDimensiones()
    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(
                    dim.paddingPantalla
                ),
        horizontalAlignment =
            Alignment.CenterHorizontally,
        verticalArrangement =
            Arrangement.Center
    ) {
        Text(
            text = "Materiales",
            fontSize =
                dim.titulo,
            style =
                MaterialTheme
                    .typography
                    .headlineLarge
        )
        Spacer(
            modifier =
                Modifier.height(
                    dim.espacio
                )
        )
        Button(
            onClick =
                verMateriales,
            modifier = Modifier
                .widthIn(
                    dim.alturaBotonInterno)
        ) {
            Text(
                text =
                    "📋 Ver Materiales",
                fontSize =
                    dim.texto
            )
        }
        Spacer(
            modifier =
                Modifier.height(
                    dim.espacio
                )
        )
        Button(
            onClick =
                actualizarMateriales,
            modifier = Modifier
                .widthIn(
                    dim.alturaBotonInterno)
        ) {
            Text(
                text =
                    "🔄 Actualizar Materiales",
                fontSize =
                    dim.texto
            )
        }
        Spacer(
            modifier =
                Modifier.height(
                    dim.espacio
                )
        )
        Button(
            onClick =
                volver,
            modifier = Modifier
                .widthIn(
                    dim.alturaBotonInterno)
        ) {
            Text(
                text =
                    "⬅ Volver",
                fontSize =
                    dim.texto
            )
        }
        Spacer(
            modifier =
                Modifier.height(
                    dim.espacio
                )
        )
    }
}