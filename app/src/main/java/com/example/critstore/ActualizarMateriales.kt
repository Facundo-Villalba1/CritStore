package com.example.critstore

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@Composable
fun ActualizarMateriales(
    materialDao: MaterialDao,
    volver: () -> Unit
) {
    val dim = obtenerDimensiones()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var materiales by remember {
        mutableStateOf(
            emptyList<Materiales>()
        )
    }
    var cantidades by rememberSaveable {
        mutableStateOf(
            mutableMapOf<String, String>()
        )
    }
    var materialEliminar by remember {
        mutableStateOf<Materiales?>(null)
    }
    var cargado by remember {
        mutableStateOf(false)
    }
    var guardando by remember {
        mutableStateOf(false)
    }
    LaunchedEffect(Unit) {
        materiales =
            materialDao.obtenerMateriales()
        if (!cargado) {
            materiales.forEach { material ->
                cantidades =
                    cantidades
                        .toMutableMap()
                        .apply {
                            put(
                                material.uuid,
                                material.Cantidad.toString()
                            )
                        }
            }
            cargado = true
        }
    }
    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(
                    top =
                        dim.paddingPantalla * 2,
                    start =
                        dim.paddingPantalla,
                    end =
                        dim.paddingPantalla,
                    bottom =
                        dim.paddingPantalla
                )
    ) {
        Text(
            text = "Actualizar Materiales",
            fontSize = dim.titulo,
            style =
                MaterialTheme
                    .typography
                    .headlineMedium
        )
        Spacer(
            modifier =
                Modifier.height(
                    dim.espacio
                )
        )
        Card(
            modifier =
                Modifier.fillMaxWidth()
        ) {
            Row(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(
                            dim.espacio
                        ),
                verticalAlignment =
                    Alignment.CenterVertically
            ) {
                Text(
                    text = "Marca",
                    fontSize = dim.texto,
                    modifier =
                        Modifier.weight(2f)
                )
                Text(
                    text = "Color",
                    fontSize = dim.texto,
                    modifier =
                        Modifier.weight(2f)
                )
                Text(
                    text = "Cantidad",
                    fontSize = dim.texto,
                    modifier =
                        Modifier.weight(2f),
                    textAlign =
                        TextAlign.Center
                )
                Spacer(
                    modifier =
                        Modifier.width(
                            48.dp
                        )
                )
            }
        }
        Spacer(
            modifier =
                Modifier.height(
                    dim.espacio
                )
        )
        LazyColumn(
            modifier =
                Modifier
                    .weight(1f),
            verticalArrangement =
                Arrangement.spacedBy(
                    8.dp
                )
        ) {
            items(
                items = materiales,
                key = {
                    it.uuid
                }
            ) { material ->
                val cantidad =
                    cantidades[
                        material.uuid
                    ]
                        ?.toIntOrNull()
                        ?: 0
                Card(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(
                                vertical = 4.dp
                            )
                ) {
                    Row(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .padding(
                                    dim.espacio
                                ),
                        verticalAlignment =
                            Alignment.CenterVertically
                    ) {
                        Text(
                            text =
                                material.Marca,
                            fontSize =
                                dim.texto,
                            modifier =
                                Modifier.weight(
                                    2f
                                ),
                            maxLines = 1
                        )
                        Text(
                            text =
                                material.Color,
                            fontSize =
                                dim.texto,
                            modifier =
                                Modifier.weight(
                                    2f
                                ),
                            maxLines = 1
                        )
                        IconButton(
                            onClick = {
                                val actual =
                                    cantidades[
                                        material.uuid
                                    ]
                                        ?.toIntOrNull()
                                        ?: 0
                                if (actual > 0) {
                                    cantidades =
                                        cantidades
                                            .toMutableMap()
                                            .apply {
                                                put(
                                                    material.uuid,
                                                    (
                                                            actual - 1
                                                            ).toString()
                                                )
                                            }
                                }
                            }
                        ) {
                            Icon(
                                imageVector =
                                    Icons.Default.Remove,
                                contentDescription =
                                    "Restar"
                            )
                        }
                        Text(
                            text =
                                cantidad.toString(),
                            fontSize =
                                dim.texto,
                            modifier =
                                Modifier.width(
                                    40.dp
                                ),
                            textAlign =
                                TextAlign.Center
                        )
                        IconButton(
                            onClick = {
                                val actual =
                                    cantidades[
                                        material.uuid
                                    ]
                                        ?.toIntOrNull()
                                        ?: 0
                                cantidades =
                                    cantidades
                                        .toMutableMap()
                                        .apply {
                                            put(
                                                material.uuid,
                                                (
                                                        actual + 1
                                                        ).toString()
                                            )
                                        }
                            }
                        ) {
                            Icon(
                                imageVector =
                                    Icons.Default.Add,
                                contentDescription =
                                    "Sumar"
                            )
                        }
                        IconButton(
                            onClick = {
                                materialEliminar =
                                    material
                            }
                        ) {
                            Icon(
                                imageVector =
                                    Icons.Default.Delete,
                                contentDescription =
                                    "Eliminar material"
                            )
                        }
                    }
                }
            }
        }
        Button(
            onClick = {
                if (guardando) {
                    return@Button
                }
                scope.launch {
                    guardando = true
                    try {
                        val materialesActuales =
                            materialDao
                                .obtenerMateriales()
                        materialesActuales.forEach { material ->
                            val nuevaCantidad =
                                cantidades[
                                    material.uuid
                                ]
                                    ?.toIntOrNull()
                                    ?: 0
                            materialDao
                                .actualizarCantidad(
                                    uuid =
                                        material.uuid,
                                    cantidad =
                                        nuevaCantidad
                                )
                            val materialActualizado =
                                material.copy(
                                    Cantidad =
                                        nuevaCantidad
                                )
                            actualizarMaterialesGoogle(
                                listOf(
                                    materialActualizado
                                )
                            )
                        }
                        materiales =
                            materialDao
                                .obtenerMateriales()
                        Toast.makeText(
                            context,
                            "✅ Materiales guardados correctamente",
                            Toast.LENGTH_LONG
                        ).show()
                    } catch (e: Exception) {
                        Toast.makeText(
                            context,
                            "Error al guardar: ${e.message}",
                            Toast.LENGTH_LONG
                        ).show()
                    } finally {
                        guardando = false
                    }
                }
            },
            enabled =
                !guardando,
            modifier =
                Modifier
                    .widthIn(
                        min =
                            dim.alturaBotonInterno
                    )
                    .fillMaxWidth()
                    .align(
                        Alignment.CenterHorizontally
                    )
        ) {
            Text(
                text =
                    if (guardando)
                        "Guardando..."
                    else
                        "💾 Acualizar",
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
        OutlinedButton(
            onClick = volver,
            modifier =
                Modifier
                    .widthIn(
                        min =
                            dim.alturaBotonInterno
                    )
                    .fillMaxWidth()
                    .align(
                        Alignment.CenterHorizontally
                    )
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
    materialEliminar?.let { material ->
        AlertDialog(
            onDismissRequest = {
                materialEliminar =
                    null
            },
            title = {
                Text(
                    text =
                        "Eliminar material"
                )
            },
            text = {
                Text(
                    text =
                        "¿Eliminar ${material.Marca} - ${material.Color}?"
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (guardando) {
                            return@Button
                        }
                        scope.launch {
                            guardando = true
                            try {
                                materialDao
                                    .eliminarMaterial(
                                        material
                                    )
                                val eliminadoGoogle =
                                    eliminarMaterialGoogle(
                                        material.uuid
                                    )
                                materiales =
                                    materialDao
                                        .obtenerMateriales()
                                cantidades =
                                    cantidades
                                        .toMutableMap()
                                        .apply {
                                            remove(
                                                material.uuid
                                            )
                                        }
                                materialEliminar =
                                    null
                                if (eliminadoGoogle) {
                                    Toast.makeText(
                                        context,
                                        "✅ Material eliminado",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                } else {
                                    Toast.makeText(
                                        context,
                                        "⚠ Material eliminado de Room, pero no de Google",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            } catch (e: Exception) {
                                Toast.makeText(
                                    context,
                                    "Error al eliminar: ${e.message}",
                                    Toast.LENGTH_LONG
                                ).show()
                            } finally {
                                guardando = false
                            }
                        }
                    }
                ) {
                    Text(
                        text =
                            "Eliminar"
                    )
                }
            },
            dismissButton = {
                Button(
                    onClick = {
                        materialEliminar =
                            null
                    }
                ) {
                    Text(
                        text =
                            "Cancelar"
                    )
                }
            }
        )
    }
}
