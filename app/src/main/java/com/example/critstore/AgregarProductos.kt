package com.example.critstore

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AgregarProductos(
    productoDao: ProductoDao,
    agregarProducto: (Producto) -> Unit,
    volver: () -> Unit
) {
    val configuracion = LocalConfiguration.current
    val anchoPantalla = configuracion.screenWidthDp
    val esTablet = anchoPantalla >= 600
    val paddingPantalla =
        if (esTablet) 32.dp else 16.dp
    val tamañoTitulo =
        if (esTablet) 32.sp else 24.sp
    val tamañoTexto =
        if (esTablet) 20.sp else 16.sp
    val anchoContenido =
        if (esTablet) 700.dp else 500.dp
    val altoBoton =
        if (esTablet) 60.dp else 50.dp
    var productos by remember {
        mutableStateOf(emptyList<Producto>())
    }
    var busqueda by remember {
        mutableStateOf("")
    }
    LaunchedEffect(Unit) {
        productos = productoDao.obtenerProductos()
    }
    val productosFiltrados =
        productos.filter {
            it.Cantidad > 0 &&
                    it.Nombre.contains(
                        busqueda,
                        ignoreCase = true
                    )
        }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingPantalla)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(max = anchoContenido)
                .align(Alignment.CenterHorizontally)
        ) {
            Text(
                text = "Agregar Productos",
                fontSize = tamañoTitulo,
                style = MaterialTheme.typography.headlineMedium
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = busqueda,
                onValueChange = {
                    busqueda = it
                },
                label = {
                    Text(
                        "Buscar producto",
                        fontSize = tamañoTexto
                    )
                },
                textStyle = LocalTextStyle.current.copy(
                    fontSize = tamañoTexto
                ),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
        LazyColumn(
            modifier = Modifier.weight(1f)
        ) {
            items(productosFiltrados) { producto ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 3.dp
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                if (esTablet) 20.dp else 12.dp
                            ),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = producto.Nombre,
                                fontSize = tamañoTexto,
                                style = MaterialTheme.typography.titleMedium
                            )
                            Spacer(
                                modifier = Modifier.height(4.dp)
                            )
                            Text(
                                text = "Stock: ${producto.Cantidad}",
                                fontSize = tamañoTexto
                            )
                            Text(
                                text = "Precio: $${producto.Precio}",
                                fontSize = tamañoTexto
                            )
                        }
                        Spacer(
                            modifier = Modifier.width(12.dp)
                        )
                        Button(
                            onClick = {
                                agregarProducto(producto)
                            },
                            modifier = Modifier
                                .height(altoBoton)
                        ) {
                            Text(
                                text = "Agregar",
                                fontSize = tamañoTexto
                            )
                        }
                    }
                }
            }
        }
        Spacer(
            modifier = Modifier.height(12.dp)
        )
        OutlinedButton(
            onClick = volver,
            modifier = Modifier
                .fillMaxWidth()
                .height(altoBoton)
        ) {
            Text(
                text = "⬅ Volver",
                fontSize = tamañoTexto
            )
        }
    }
}