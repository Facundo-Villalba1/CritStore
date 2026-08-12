package com.example.critstore

import android.content.*
import android.graphics.*
import android.graphics.pdf.PdfDocument
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.platform.*
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.*
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VerMateriales(
    materialDao: MaterialDao,
    volver: () -> Unit
) {
    val context = LocalContext.current
    val dim = obtenerDimensiones()
    var materiales by remember {
        mutableStateOf(
            emptyList<Materiales>()
        )
    }
    var marcas by remember {
        mutableStateOf(
            listOf("Todos")
        )
    }
    var marcaSeleccionada by remember {
        mutableStateOf("Todos")
    }
    var expandido by remember {
        mutableStateOf(false)
    }
    suspend fun cargarMateriales() {
        try {
            // Obtener materiales actualizados desde Google
            val materialesGoogle =
                obtenerMaterialesGoogle()
            // Guardar los materiales en Room
            materialesGoogle.forEach { material ->
                materialDao.insertarMaterial(
                    material
                )
            }
        } catch (e: Exception) {
            Toast.makeText(
                context,
                "No se pudieron actualizar los materiales desde Google",
                Toast.LENGTH_SHORT
            ).show()
        }
        // Cargar los materiales desde Room
        materiales =
            materialDao.obtenerMateriales()
        // Actualizar las marcas disponibles
        marcas =
            listOf("Todos") +
                    materiales
                        .map {
                            it.Marca.trim()
                        }
                        .filter {
                            it.isNotEmpty()
                        }
                        .distinct()
                        .sorted()
    }
    LaunchedEffect(Unit) {
        cargarMateriales()
    }
    val materialesFiltrados =
        materiales.filter { material ->
            material.Cantidad > 0 &&
                    (
                            marcaSeleccionada == "Todos" ||
                                    material.Marca.trim()
                                        .equals(
                                            marcaSeleccionada.trim(),
                                            ignoreCase = true
                                        )
                            )
        }
    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(
                    top = dim.paddingPantalla * 2,
                    start = dim.paddingPantalla,
                    end = dim.paddingPantalla,
                    bottom = dim.paddingPantalla
                )
    ) {
        Text(
            text = "Ver Materiales",
            fontSize = dim.titulo,
            style =
                MaterialTheme.typography.headlineMedium
        )
        Spacer(
            modifier =
                Modifier.height(
                    dim.espacio
                )
        )
        ExposedDropdownMenuBox(
            expanded = expandido,
            onExpandedChange = {
                expandido = !expandido
            }
        ) {
            OutlinedTextField(
                value = marcaSeleccionada,
                onValueChange = {},
                readOnly = true,
                label = {
                    Text(
                        text = "Filtrar por marca",
                        fontSize = dim.texto
                    )
                },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(
                        expanded = expandido
                    )
                },
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .menuAnchor()
            )
            ExposedDropdownMenu(
                expanded = expandido,
                onDismissRequest = {
                    expandido = false
                }
            ) {
                marcas.forEach { marca ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = marca,
                                fontSize = dim.texto
                            )
                        },
                        onClick = {
                            marcaSeleccionada =
                                marca
                            expandido = false
                        }
                    )
                }
            }
        }
        Spacer(
            modifier =
                Modifier.height(
                    dim.espacio
                )
        )
        Card(
            modifier =
                Modifier
                    .fillMaxWidth()
        ) {
            Row(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(
                            horizontal =
                                dim.espacio,
                            vertical =
                                dim.espacio
                        ),
                verticalAlignment =
                    Alignment.CenterVertically
            ) {
                Text(
                    text = "Color",
                    fontSize = dim.texto,
                    modifier =
                        Modifier.weight(
                            2f
                        )
                )
                Text(
                    text = "Cantidad",
                    fontSize = dim.texto,
                    modifier =
                        Modifier.weight(
                            1f
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
                    .weight(1f)
        ) {
            items(
                materialesFiltrados,
                key = {
                    it.uuid
                }
            ) { material ->
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
                                    horizontal =
                                        dim.espacio,
                                    vertical =
                                        dim.espacio
                                ),
                        verticalAlignment =
                            Alignment.CenterVertically
                    ) {
                        // COLOR
                        Text(
                            text =
                                material.Color,
                            modifier =
                                Modifier.weight(
                                    2f
                                ),
                            maxLines = 1,
                            overflow =
                                TextOverflow.Ellipsis,
                            fontSize =
                                dim.texto
                        )
                        // CANTIDAD
                        Text(
                            text =
                                material.Cantidad.toString(),
                            modifier =
                                Modifier.weight(
                                    1f
                                ),
                            fontSize =
                                dim.texto
                        )
                    }
                }
            }
        }
        Spacer(
            modifier =
                Modifier.height(
                    dim.espacio
                )
        )
        Button(
            onClick = {
                generarPDFMateriales(
                    context,
                    materialesFiltrados
                )
            },
            modifier =
                Modifier
                    .widthIn(
                        min = dim.alturaBotonInterno
                    )
                    .fillMaxWidth()
                    .align(
                        Alignment.CenterHorizontally
                    )
        ) {
            Text(
                text = "📄 PDF Materiales",
                fontSize = dim.texto
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
                        min = dim.alturaBotonInterno
                    )
                    .fillMaxWidth()
                    .align(
                        Alignment.CenterHorizontally
                    )
        ) {
            Text(
                text = "⬅ Volver",
                fontSize = dim.texto
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
fun generarPDFMateriales(
    context: Context,
    materiales: List<Materiales>
) {
    val documento =
        PdfDocument()
    val ancho = 595
    val alto = 842
    var numeroPagina = 1
    var pagina =
        documento.startPage(
            PdfDocument.PageInfo
                .Builder(
                    ancho,
                    alto,
                    numeroPagina
                )
                .create()
        )
    var canvas =
        pagina.canvas
    val paint =
        Paint()
    val margen =
        40f
    fun titulo() {
        paint.style =
            Paint.Style.FILL
        try {
            val bitmap =
                BitmapFactory.decodeResource(
                    context.resources,
                    R.drawable.logo_critstore
                )
            val logo =
                Bitmap.createScaledBitmap(
                    bitmap,
                    80,
                    80,
                    true
                )
            canvas.drawBitmap(
                logo,
                null,
                RectF(
                    480f,
                    20f,
                    550f,
                    90f
                ),
                paint
            )
        } catch (e: Exception) {
        }
        paint.textSize =
            22f
        paint.isFakeBoldText =
            true
        canvas.drawText(
            "Materiales CritStore",
            margen,
            45f,
            paint
        )
        paint.isFakeBoldText =
            false
        paint.textSize =
            11f
        val fecha =
            SimpleDateFormat(
                "dd-MM-yyyy HH:mm:ss",
                Locale.getDefault()
            ).format(
                Date()
            )
        canvas.drawText(
            "Generado: $fecha",
            margen,
            70f,
            paint
        )
    }
    fun cabecera(
        y: Float
    ): Float {
        paint.style =
            Paint.Style.STROKE
        canvas.drawRect(
            margen,
            y,
            555f,
            y + 30f,
            paint
        )
        canvas.drawLine(
            250f,
            y,
            250f,
            y + 30f,
            paint
        )
        canvas.drawLine(
            400f,
            y,
            400f,
            y + 30f,
            paint
        )
        paint.style =
            Paint.Style.FILL
        paint.textSize =
            12f
        paint.isFakeBoldText =
            true
        canvas.drawText(
            "Marca",
            60f,
            y + 20f,
            paint
        )
        canvas.drawText(
            "Color",
            280f,
            y + 20f,
            paint
        )
        canvas.drawText(
            "Cantidad",
            430f,
            y + 20f,
            paint
        )
        paint.isFakeBoldText =
            false
        return y + 30f
    }
    fun nuevaPagina() {
        documento.finishPage(
            pagina
        )
        numeroPagina++
        pagina =
            documento.startPage(
                PdfDocument.PageInfo
                    .Builder(
                        ancho,
                        alto,
                        numeroPagina
                    )
                    .create()
            )
        canvas =
            pagina.canvas
        titulo()
    }
    titulo()
    var y =
        cabecera(
            110f
        )
    materiales.forEach { material ->
        if (y > 760f) {
            nuevaPagina()
            y =
                cabecera(
                    110f
                )
        }
        paint.style =
            Paint.Style.STROKE
        canvas.drawRect(
            margen,
            y,
            555f,
            y + 25f,
            paint
        )
        canvas.drawLine(
            250f,
            y,
            250f,
            y + 25f,
            paint
        )
        canvas.drawLine(
            400f,
            y,
            400f,
            y + 25f,
            paint
        )
        paint.style =
            Paint.Style.FILL
        paint.textSize =
            10f
        val marca =
            if (material.Marca.length > 25)
                material.Marca.substring(
                    0,
                    25
                ) + "..."
            else
                material.Marca
        val color =
            if (material.Color.length > 20)
                material.Color.substring(
                    0,
                    20
                ) + "..."
            else
                material.Color
        canvas.drawText(
            marca,
            50f,
            y + 17f,
            paint
        )
        canvas.drawText(
            color,
            270f,
            y + 17f,
            paint
        )
        canvas.drawText(
            material.Cantidad.toString(),
            430f,
            y + 17f,
            paint
        )
        y += 25f
    }
    documento.finishPage(
        pagina
    )
    val fechaArchivo =
        SimpleDateFormat(
            "dd-MM-yyyy",
            Locale.getDefault()
        ).format(
            Date()
        )
    val nombreArchivo =
        "Materiales_CritStore_$fechaArchivo.pdf"
    val resolver =
        context.contentResolver
    val valores =
        ContentValues().apply {
            put(
                MediaStore.Downloads.DISPLAY_NAME,
                nombreArchivo
            )
            put(
                MediaStore.Downloads.MIME_TYPE,
                "application/pdf"
            )
            put(
                MediaStore.Downloads.RELATIVE_PATH,
                Environment.DIRECTORY_DOWNLOADS
            )
        }
    val uri =
        resolver.insert(
            MediaStore.Downloads.EXTERNAL_CONTENT_URI,
            valores
        )
    uri?.let {
        resolver
            .openOutputStream(it)
            ?.use { salida ->
                documento.writeTo(
                    salida
                )
            }
        Toast.makeText(
            context,
            "PDF de materiales guardado correctamente",
            Toast.LENGTH_LONG
        ).show()
    }
    documento.close()
}
