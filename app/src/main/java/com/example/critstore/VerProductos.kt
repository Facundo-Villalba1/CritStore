package com.example.critstore

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material3.ExperimentalMaterial3Api

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VerProductos(
    productoDao: ProductoDao,
    volver: () -> Unit
) {
    var productos by remember {
        mutableStateOf(emptyList<Producto>())
    }
    var tipoSeleccionado by remember {
        mutableStateOf("Todos")
    }
    var tipo by remember {
        mutableStateOf(listOf("Todos"))
    }
    var expandido by remember {
        mutableStateOf(false)
    }
    val productosFiltrados =
        if (tipoSeleccionado == "Todos") {
            productos
        } else {
            productos.filter {
                it.Tipo.trim() == tipoSeleccionado.trim()
            }
        }
    LaunchedEffect(Unit) {
        productos = productoDao.obtenerProductos()
        tipo = listOf("Todos") + productoDao.obtenerTipos()
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Ver Productos",
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(modifier = Modifier.height(20.dp))
        ExposedDropdownMenuBox(
            expanded = expandido,
            onExpandedChange = {
                expandido = !expandido
            }
        ) {
            OutlinedTextField(
                value = tipoSeleccionado,
                onValueChange = {},
                readOnly = true,
                label = {
                    Text("Filtrar por tipo")
                },
                modifier = Modifier.menuAnchor().fillMaxWidth()
            )
            ExposedDropdownMenu(
                expanded = expandido,
                onDismissRequest = {
                    expandido = false
                }
            ) {
                tipo.forEach { tipo ->
                    DropdownMenuItem(
                        text = {
                            Text(tipo)
                        },
                        onClick = {
                            tipoSeleccionado = tipo
                            expandido = false
                        }
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(20.dp))
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
            ) {
                Text(
                    text = "Nombre",
                    modifier = Modifier.weight(2f)
                )
                Text(
                    text = "Cantidad",
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = "Precio",
                    modifier = Modifier.weight(1f)
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        LazyColumn(
            modifier = Modifier.weight(1f)
        ) {
            items(productosFiltrados) { producto ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp)
                    ) {
                        Text(
                            text = producto.Nombre,
                            modifier = Modifier.weight(2f)
                        )
                        Text(
                            text = producto.Cantidad.toString(),
                            modifier = Modifier.weight(1f)
                        )
                        Text(
                            text = "$${producto.Precio}",
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
        Button(
            onClick = volver,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("⬅ Volver")
        }
    }
}