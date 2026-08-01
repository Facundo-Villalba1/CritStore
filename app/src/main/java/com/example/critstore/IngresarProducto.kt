package com.example.critstore

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

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
    val context =
        LocalContext.current
    val scope =
        rememberCoroutineScope()
    val configuracion =
        LocalConfiguration.current
    val anchoPantalla =
        configuracion.screenWidthDp
    val esTablet =
        anchoPantalla >= 600
    val paddingPantalla =
        if(esTablet)
            48.dp
        else
            20.dp
    val anchoFormulario =
        if(esTablet)
            500.dp
        else
            400.dp
    val tamañoTitulo =
        if(esTablet)
            32.sp
        else
            24.sp
    val tamañoTexto =
        if(esTablet)
            18.sp
        else
            14.sp
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(
                rememberScrollState()
            )
            .padding(
                paddingPantalla
            ),
        horizontalAlignment =
            Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier
                .widthIn(
                    max = anchoFormulario
                )
                .fillMaxWidth()
        ){
            Text(
                text =
                    "Ingresar Producto",
                fontSize =
                    tamañoTitulo,
                style =
                    MaterialTheme.typography.headlineMedium
            )
            Spacer(
                Modifier.height(20.dp)
            )
            OutlinedTextField(
                value =
                    nombre,
                onValueChange = {
                    nombre = it
                },
                label = {
                    Text(
                        "Nombre",
                        fontSize = tamañoTexto
                    )
                },
                modifier =
                    Modifier.fillMaxWidth(),
                singleLine = true
            )
            Spacer(
                Modifier.height(12.dp)
            )
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
                        fontSize = tamañoTexto
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
            Spacer(
                Modifier.height(12.dp)
            )
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
                        fontSize = tamañoTexto
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
            Spacer(
                Modifier.height(12.dp)
            )
            OutlinedTextField(
                value =
                    tipo,
                onValueChange = {
                    tipo = it
                },
                label = {
                    Text(
                        "Tipo",
                        fontSize = tamañoTexto
                    )
                },
                modifier =
                    Modifier.fillMaxWidth(),
                singleLine = true
            )
            Spacer(
                Modifier.height(25.dp)
            )
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
                modifier = Modifier
                    .fillMaxWidth()
                    .height(
                        if(esTablet)
                            60.dp
                        else
                            50.dp
                    )
            ){
                Text(
                    "💾 Guardar Producto",
                    fontSize =
                        tamañoTexto
                )
            }
            Spacer(
                Modifier.height(12.dp)
            )
            OutlinedButton(
                onClick = volver,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(
                        if(esTablet)
                            60.dp
                        else
                            50.dp
                    )
            ){
                Text(
                    "⬅ Volver",
                    fontSize =
                        tamañoTexto
                )
            }
        }
    }
}