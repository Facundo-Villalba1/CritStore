package com.example.critstore

import android.content.ContentValues
import android.content.Context
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import android.graphics.Bitmap
import android.graphics.BitmapFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VerProductos(
    productoDao: ProductoDao,
    volver: () -> Unit,
    generarPDF: (List<Producto>) -> Unit
) {
    var productos by remember {
        mutableStateOf(emptyList<Producto>())
    }
    var tipoSeleccionado by remember {
        mutableStateOf("Todos")
    }
    var tipos by remember {
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
        tipos = listOf("Todos") + productoDao.obtenerTipos()
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
        Spacer(
            modifier = Modifier.height(20.dp)
        )
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
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )
            ExposedDropdownMenu(
                expanded = expandido,
                onDismissRequest = {
                    expandido = false
                }
            ) {
                tipos.forEach { tipo ->
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
        Spacer(
            modifier = Modifier.height(20.dp)
        )
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
            ) {
                Text(
                    text = "Producto",
                    modifier = Modifier.weight(2f)
                )
                Text(
                    text = "Stock",
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = "Precio",
                    modifier = Modifier.weight(1f)
                )
            }
        }
        Spacer(
            modifier = Modifier.height(8.dp)
        )
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
            onClick = {
                generarPDF(productosFiltrados)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("📄 Generar PDF")
        }
        Spacer(
            modifier = Modifier.height(10.dp)
        )
        Button(
            onClick = volver,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("⬅ Volver")
        }
    }
}
fun generarPDFStock(
    context: Context,
    productos: List<Producto>
) {
    val documento = PdfDocument()
    val pagina = documento.startPage(
        PdfDocument.PageInfo.Builder(
            595,
            842,
            1
        ).create()
    )
    val canvas = pagina.canvas
    val paint = Paint()
    var y = 50f
    // TITULO
    paint.style = Paint.Style.FILL
    paint.textSize = 24f
    paint.isFakeBoldText = true
    canvas.drawText(
        "CritStore",
        40f,
        y,
        paint
    )
    y += 35
    paint.textSize = 18f
    canvas.drawText(
        "Listado de Stock",
        40f,
        y,
        paint
    )
    y += 35
    // FECHA GENERACION
    paint.textSize = 11f
    paint.isFakeBoldText = false
    val fecha = SimpleDateFormat(
        "dd/MM/yyyy HH:mm",
        Locale.getDefault()
    ).format(Date())
    canvas.drawText(
        "Generado: $fecha",
        40f,
        y,
        paint
    )
    y += 40
    // CONFIGURACION TABLA
    val inicioX = 40f
    val anchoProducto = 260f
    val anchoStock = 100f
    val anchoPrecio = 120f
    val altoFila = 30f
    val anchoTabla =
        anchoProducto + anchoStock + anchoPrecio
    // CABECERA TABLA
    paint.style = Paint.Style.STROKE
    paint.strokeWidth = 1f
    canvas.drawRect(
        inicioX,
        y,
        inicioX + anchoTabla,
        y + altoFila,
        paint
    )
    canvas.drawLine(
        inicioX + anchoProducto,
        y,
        inicioX + anchoProducto,
        y + altoFila,
        paint
    )
    canvas.drawLine(
        inicioX + anchoProducto + anchoStock,
        y,
        inicioX + anchoProducto + anchoStock,
        y + altoFila,
        paint
    )
    paint.style = Paint.Style.FILL
    paint.textSize = 13f
    paint.isFakeBoldText = true
    canvas.drawText(
        "Producto",
        inicioX + 10,
        y + 20,
        paint
    )
    canvas.drawText(
        "Stock",
        inicioX + anchoProducto + 25,
        y + 20,
        paint
    )
    canvas.drawText(
        "Precio",
        inicioX + anchoProducto + anchoStock + 25,
        y + 20,
        paint
    )
    y += altoFila
    // DATOS PRODUCTOS
    paint.textSize = 12f
    paint.isFakeBoldText = false
    productos.forEach { producto ->
        paint.style = Paint.Style.STROKE
        canvas.drawRect(
            inicioX,
            y,
            inicioX + anchoTabla,
            y + altoFila,
            paint
        )
        canvas.drawLine(
            inicioX + anchoProducto,
            y,
            inicioX + anchoProducto,
            y + altoFila,
            paint
        )
        canvas.drawLine(
            inicioX + anchoProducto + anchoStock,
            y,
            inicioX + anchoProducto + anchoStock,
            y + altoFila,
            paint
        )
        paint.style = Paint.Style.FILL
        canvas.drawText(
            producto.Nombre,
            inicioX + 10,
            y + 20,
            paint
        )
        canvas.drawText(
            producto.Cantidad.toString(),
            inicioX + anchoProducto + 35,
            y + 20,
            paint
        )
        canvas.drawText(
            "$${producto.Precio}",
            inicioX + anchoProducto + anchoStock + 25,
            y + 20,
            paint
        )
        y += altoFila
    }
    // LOGO PIE
    val logo = BitmapFactory.decodeResource(
        context.resources,
        R.drawable.logo_critstore
    )
    val logoReducido = Bitmap.createScaledBitmap(
        logo,
        90,
        90,
        true
    )
    val posicionX =
        (595 - logoReducido.width) / 2f
    canvas.drawBitmap(
        logoReducido,
        posicionX,
        700f,
        paint
    )
    documento.finishPage(pagina)
    // NOMBRE ARCHIVO
    val nombreArchivo =
        "Stock_CritStore.pdf"
    val valores = ContentValues().apply {
        put(
            MediaStore.MediaColumns.DISPLAY_NAME,
            nombreArchivo
        )
        put(
            MediaStore.MediaColumns.MIME_TYPE,
            "application/pdf"
        )
        put(
            MediaStore.MediaColumns.RELATIVE_PATH,
            Environment.DIRECTORY_DOWNLOADS
        )
    }
    try {
        val uri = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
            context.contentResolver.insert(
                MediaStore.Downloads.EXTERNAL_CONTENT_URI,
                valores
            )
        } else {
            null
        }
        uri?.let {
            context.contentResolver
                .openOutputStream(it)
                .use { salida ->
                    documento.writeTo(salida)
                }
        }
        Toast.makeText(
            context,
            "PDF guardado: $nombreArchivo",
            Toast.LENGTH_LONG
        ).show()
    } catch (e: Exception) {
        Toast.makeText(
            context,
            "Error generando PDF: ${e.message}",
            Toast.LENGTH_LONG
        ).show()
    }
    documento.close()
}