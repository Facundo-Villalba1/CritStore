package com.example.critstore

import android.widget.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.platform.*
import androidx.compose.ui.text.input.*
import kotlinx.coroutines.*

@Composable
fun IngresarProducto(
    productoDao: ProductoDao,
    volver: () -> Unit
) {
    var nombre by remember {
        mutableStateOf("")
    }
    var cantidad by remember {
        mutableStateOf("")
    }
    var precio by remember {
        mutableStateOf("")
    }
    var tipo by remember {
        mutableStateOf("")
    }
    val context =LocalContext.current
    val scope =rememberCoroutineScope()
    val dim = obtenerDimensiones()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(
                rememberScrollState()
            )
            .padding(
                top = dim.paddingPantalla * 2,
                start = dim.paddingPantalla,
                end = dim.paddingPantalla,
                bottom = dim.paddingPantalla
            ),
        horizontalAlignment =
            Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier
                .widthIn(
                  dim.espacio
                )
                .fillMaxWidth()
        ){
            Text(
                text ="Ingresar Producto",
                fontSize = dim.titulo,
                style = MaterialTheme.typography.headlineMedium
            )
            Spacer( modifier = Modifier.height(dim.espacio) )
            OutlinedTextField(
                value =
                    nombre,
                onValueChange = {
                    nombre = it
                },
                label = {
                    Text(
                        "Nombre",
                        fontSize = dim.texto
                    )
                },
                modifier =
                    Modifier.fillMaxWidth(),
                singleLine = true
            )
            Spacer( modifier = Modifier.height(dim.espacio) )
            OutlinedTextField(
                value =
                    cantidad,
                onValueChange = {
                    cantidad =
                        it.filter { caracter ->
                            caracter.isDigit()
                        }
                },
                label = {
                    Text(
                        "Cantidad",
                        fontSize = dim.texto
                    )
                },
                keyboardOptions =
                    KeyboardOptions(
                        keyboardType =
                            KeyboardType.Number
                    ),
                modifier =
                    Modifier.fillMaxWidth(),
                singleLine = true
            )
            Spacer( modifier = Modifier.height(dim.espacio) )
            OutlinedTextField(
                value =
                    precio,
                onValueChange = {
                    precio =
                        it.filter { caracter ->
                            caracter.isDigit()
                        }
                },
                label = {
                    Text(
                        "Precio",
                        fontSize = dim.texto
                    )
                },
                keyboardOptions =
                    KeyboardOptions(
                        keyboardType =
                            KeyboardType.Number
                    ),
                modifier =
                    Modifier.fillMaxWidth(),
                singleLine = true
            )
            Spacer( modifier = Modifier.height(dim.espacio) )
            OutlinedTextField(
                value =
                    tipo,
                onValueChange = {
                    tipo = it
                },
                label = {
                    Text(
                        "Tipo",
                        fontSize = dim.texto
                    )
                },
                modifier =
                    Modifier.fillMaxWidth(),
                singleLine = true
            )
            Spacer( modifier = Modifier.height(dim.espacio) )
            Button(
                onClick = {
                    if(
                        nombre.isBlank() ||
                        cantidad.isBlank() ||
                        precio.isBlank() ||
                        tipo.isBlank()
                    ){
                        Toast.makeText(
                            context,
                            "Complete todos los campos",
                            Toast.LENGTH_SHORT
                        ).show()
                        return@Button
                    }
                    val producto =
                        Producto(
                            uuid =
                                java.util.UUID
                                    .randomUUID()
                                    .toString(),
                            Tipo =
                                tipo.trim(),
                            Nombre =
                                nombre.trim(),
                            Cantidad =
                                cantidad.toInt(),
                            Precio =
                                precio.toInt()
                        )
                    scope.launch {
                        productoDao.insertarProducto(
                            producto
                        )
                        Toast.makeText(
                            context,
                            "Producto guardado",
                            Toast.LENGTH_SHORT
                        ).show()
                        nombre = ""
                        cantidad = ""
                        precio = ""
                        tipo = ""
                    }
                },
                modifier =Modifier
                    .widthIn(dim.alturaBotonInterno)
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally)
            ){
                Text(
                    "💾 Guardar Producto",
                    fontSize =
                        dim.texto
                )
            }
            Spacer( modifier = Modifier.height(dim.espacio) )
            OutlinedButton(
                onClick = volver,
                modifier =Modifier
                    .widthIn(dim.alturaBotonInterno)
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally)
            ){
                Text(
                    "⬅ Volver",
                    fontSize =
                        dim.texto
                )
            }
 Spacer(modifier = Modifier.height(dim.alturaBotonInterno))
        }
    }
}