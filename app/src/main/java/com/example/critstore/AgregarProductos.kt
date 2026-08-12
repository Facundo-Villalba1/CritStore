package com.example.critstore

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.unit.*

@Composable
fun AgregarProductos(
    productoDao: ProductoDao,
    productosYaAgregados: List<Producto>,
    agregarProducto: (Producto) -> Unit,
    volver: () -> Unit
) {
  val dim = obtenerDimensiones()
   var productos by remember {
        mutableStateOf(
            emptyList<Producto>()
        )
    }
   var tipoSeleccionado by remember {
        mutableStateOf("Todos")
    }
   var desplegarTipos by remember {
        mutableStateOf(false)
    }
    LaunchedEffect(Unit) {
       productos =
            productoDao.obtenerProductos()
    }
  val productosAgregados =
        productosYaAgregados
            .map {
                it.uuid
            }
            .toSet()
   val tipos =
        listOf("Todos") +
                productos
                    .map {
                        it.Tipo
                    }
                    .filter {
                        it.isNotBlank()
                    }
                    .distinct()
                    .sorted()
  val productosFiltrados =
        productos.filter { producto ->
          val tieneStock =
                producto.Cantidad > 0
           val noEstaAgregado =
                producto.uuid !in productosAgregados
           val coincideTipo =
                tipoSeleccionado == "Todos" ||
                        producto.Tipo == tipoSeleccionado
           tieneStock &&
                    noEstaAgregado &&
                    coincideTipo
        }
  Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                top = dim.paddingPantalla * 2,
               start = dim.paddingPantalla,
               end = dim.paddingPantalla,
               bottom = dim.paddingPantalla
            )
    ) {
       Column(
            modifier = Modifier
                .fillMaxWidth()
                .widthIn()
                .align(
                    Alignment.CenterHorizontally
                )
        ) {
          Text(
                text = "Agregar Productos",
                fontSize = dim.titulo,
                style =
                    MaterialTheme
                        .typography
                        .headlineMedium
            )
           Spacer(
                modifier = Modifier.height(
                    dim.espacio
                )
            )
          Box(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
               OutlinedButton(
                    onClick = {
                        desplegarTipos =
                            !desplegarTipos
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(
                            dim.alturaBotonInterno
                        )
                ) {
                   Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement =
                            Arrangement.SpaceBetween,
                        verticalAlignment =
                            Alignment.CenterVertically
                    ) {
                       Text(
                            text =
                                "Tipo: $tipoSeleccionado",
                            fontSize = dim.texto
                        )
                       Text(
                            text =
                                if (desplegarTipos) {
                                    "▲"
                                } else {
                                    "▼"
                                },
                            fontSize = dim.texto
                        )
                    }
                }
             DropdownMenu(
                    expanded = desplegarTipos,
                    onDismissRequest = {
                        desplegarTipos = false
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                   tipos.forEach { tipo ->
                       DropdownMenuItem(
                           text = {
                               Text(
                                    text = tipo,
                                    fontSize = dim.texto
                                )
                            },
                           onClick = {
                               tipoSeleccionado =
                                    tipo
                               desplegarTipos =
                                    false
                            }
                        )
                    }
                }
            }
           Spacer(
                modifier = Modifier.height(
                    dim.espacio
                )
            )
        }
       LazyColumn(
            modifier = Modifier.weight(1f)
        ) {
           items(
                productosFiltrados,
                key = {
                    it.uuid
                }
            ) { producto ->
               Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            vertical = 6.dp
                        ),
                   elevation =
                        CardDefaults
                            .cardElevation(
                                defaultElevation = 3.dp
                            )
                ) {
                   Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                dim.paddingPantalla
                            ),
                       verticalAlignment =
                            Alignment.CenterVertically,
                       horizontalArrangement =
                            Arrangement.spacedBy(
                                dim.espacio
                            )
                    ) {
                       Column(
                            modifier = Modifier.weight(1f)
                        ) {
                           Text(
                                text =
                                    producto.Nombre,
                               fontSize =
                                    dim.texto,
                               style =
                                    MaterialTheme
                                        .typography
                                        .titleMedium
                            )
                           Spacer(
                                modifier =
                                    Modifier.height(
                                        4.dp
                                    )
                            )
                           Text(
                                text =
                                    "Tipo: ${producto.Tipo}",
                               fontSize =
                                    dim.texto
                            )
                           Text(
                                text =
                                    "Stock: ${producto.Cantidad}",
                               fontSize =
                                    dim.texto
                            )
                           Text(
                                text =
                                    "Precio: $${producto.Precio}",
                               fontSize =
                                    dim.texto
                            )
                        }
                        Spacer(
                            modifier = Modifier.height(dim.espacio)
                        )
                        Button(
                            onClick = {
                               agregarProducto(
                                    producto
                                )
                            },
                           modifier = Modifier
                                .height(
                                    dim.alturaBotonInterno
                                )
                        ) {
                           Text(
                                text = "Agregar",
                                fontSize =
                                    dim.texto
                            )
                        }
                    }
                }
            }
        }
       Spacer(
            modifier = Modifier.height(
                dim.espacio
            )
        )
        Spacer(
            modifier = Modifier.height(dim.espacio)
        )
        OutlinedButton(
            onClick = volver,
            modifier =Modifier
                .widthIn(dim.alturaBotonInterno)
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally)
        ) {
            Text(
                text = "⬅ Volver",
                fontSize =  dim.texto
            )
            Spacer(
                modifier = Modifier.height(dim.espacio)
            )
   }
}
}