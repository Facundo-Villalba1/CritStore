package com.example.critstore

import android.content.*
import android.graphics.*
import android.graphics.pdf.PdfDocument
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.platform.*
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.*
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VerProductos(
    productoDao: ProductoDao,
    volver: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val dim = obtenerDimensiones()
        var productos by remember {
        mutableStateOf(
            emptyList<Producto>()
        )
    }
    var tipos by remember {
        mutableStateOf(
            listOf("Todos")
        )
    }
    var tipoSeleccionado by remember {
        mutableStateOf("Todos")
    }
    var expandido by remember {
        mutableStateOf(false)
    }
    var sincronizando by remember {
        mutableStateOf(false)
    }
    suspend fun cargarProductos() {
        productos =
            productoDao.obtenerProductos()
        tipos =
            listOf("Todos") +
                    productos
                        .map {
                            it.Tipo.trim()
                        }
                        .distinct()
    }
    suspend fun cargarDesdeGoogle() {
        try {
            val listaGoogle =
                obtenerProductosGoogle()
            listaGoogle.forEach { producto ->
                productoDao.insertarProducto(
                    producto
                )
            }
            cargarProductos()
        } catch (e: Exception) {
            Toast.makeText(
                context,
                "Error cargando Google: ${e.message}",
                Toast.LENGTH_LONG
            ).show()
        }
    }
    suspend fun sincronizarGoogle() {
        try {
            sincronizando = true
            val lista =
                productoDao.obtenerProductos()
            lista.forEach { producto ->
                sincronizarProducto(producto)
            }
            Toast.makeText(
                context,
                "Productos enviados a Google",
                Toast.LENGTH_LONG
            ).show()
        } catch (e: Exception) {
        } finally {
            sincronizando = true
        }
    }
    LaunchedEffect(Unit) {
       cargarProductos()
       scope.launch(Dispatchers.IO) {
            try {
                val listaGoogle =
                    obtenerProductosGoogle()
                listaGoogle.forEach { producto ->
                    productoDao.insertarProducto(
                        producto
                    )
                }
                withContext(Dispatchers.Main) {
                    cargarProductos()
                }
            } catch (e: Exception) {
                Log.e(
                    "GOOGLE",
                    "Error cargando productos",
                    e
                )
            }
        }
    }
   val productosFiltrados =
        productos.filter {
           it.Cantidad > 0 &&
                    (
                            tipoSeleccionado == "Todos" ||
                                    it.Tipo.trim() ==
                                    tipoSeleccionado.trim()
                            )
        }
    val anchoProducto = dim.pesoProducto
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
        Text(
            text = "Ver Productos",
            fontSize = dim.titulo,
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer( modifier = Modifier.height(dim.espacio) )
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
                    Text(
                        text = "Filtrar por tipo",
                        fontSize =  dim.texto,
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor()
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
                            Text(
                                tipo
                            )
                        },
                        onClick = {
                            tipoSeleccionado =
                                tipo
                            expandido = false
                        }
                    )
                }
            }
        }
        Spacer( modifier = Modifier.height(dim.espacio) )
        Card(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(dim.espacio)
            ) {
                Text(
                    text = "Producto",
                    fontSize = dim.texto,
                    modifier = Modifier.weight(3f)
                )
               Text(
                    text = "Stock",
                    fontSize = dim.texto,
                    modifier = Modifier.weight(1f)
                )
               Text(
                    text = "Precio",
                    fontSize = dim.texto,
                    modifier = Modifier.weight(1f)
                )
            }
        }
        Spacer( modifier = Modifier.height(dim.espacio) )
        LazyColumn(
            modifier = Modifier
                .weight(1f)
        ) {
            items(
                productosFiltrados
            ) { producto ->
                Card(
                    Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(dim.espacio)
                    ) {
                        Text(
                            text =
                                producto.Nombre,
                            modifier =
                                Modifier.weight(
                                    anchoProducto
                                ),
                            maxLines = 1,
                            overflow =
                                TextOverflow.Ellipsis,
                           fontSize =  dim.texto,
                        )
                        Text(
                            text =
                                producto.Cantidad
                                    .toString(),
                            modifier =
                                Modifier.weight(1f),
                            fontSize =  dim.texto,
                        )
                        Text(
                            text =
                                "$${producto.Precio}",
                            modifier =
                                Modifier.weight(1f),
                           fontSize =  dim.texto,
                        )
                    }
                    Spacer( modifier = Modifier.height(dim.espacio) )
                }
            }
        }
        Spacer( modifier = Modifier.height(dim.espacio) )
        Button(
            onClick = {
                Toast.makeText(
                    context,
                    "La sincronización continúa en segundo plano.",
                    Toast.LENGTH_SHORT
                ).show()
                GlobalScope.launch(Dispatchers.IO) {
                    try {
                        sincronizarGoogle()
                    } catch (e: Exception) {
                    }
                }
                volver()
            },
            modifier =Modifier
                .widthIn(dim.alturaBotonInterno)
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally)
        ) {
            Text(text ="☁ Guardar",
                fontSize =  dim.texto)
        }
        Spacer( modifier = Modifier.height(dim.espacio) )
        Button(
            onClick = {
                generarPDFStock(
                    context,
                    productosFiltrados
                )
            },
            modifier =Modifier
                .widthIn(dim.alturaBotonInterno)
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally)
        ) {
            Text(
                "📄 PDF Stock"
            )
        }
        Spacer( modifier = Modifier.height(dim.espacio) )
        OutlinedButton(
            onClick = volver,
            modifier =Modifier
                .widthIn(dim.alturaBotonInterno)
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally)
        ) {
            Text(text="⬅ Volver",
                fontSize = dim.texto
            )
        }
        Spacer( modifier = Modifier.height(dim.espacio) )
    }
}
fun generarPDFStock(
    context: Context,
    productos: List<Producto>
) {
    val documento =
        PdfDocument()
    val ancho = 595
    val alto = 842
    var numeroPagina = 1
    var pagina =
        documento.startPage(
            PdfDocument.PageInfo.Builder(
                ancho,
                alto,
                numeroPagina
            ).create()
        )
    var canvas = pagina.canvas
    val paint = Paint()
    val margen = 40f
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
        } catch(e: Exception){
        }
        paint.textSize = 22f
        paint.isFakeBoldText = true
        canvas.drawText(
            "Stock CritStore",
            margen,
            45f,
            paint
        )
        paint.isFakeBoldText = false
        paint.textSize = 11f
        val fecha =
            SimpleDateFormat(
                "dd-MM-yyyy HH:mm:ss",
                Locale.getDefault()
            ).format(Date())
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
            220f,
            y,
            220f,
            y + 30f,
            paint
        )
        canvas.drawLine(
            350f,
            y,
            350f,
            y + 30f,
            paint
        )
        canvas.drawLine(
            430f,
            y,
            430f,
            y + 30f,
            paint
        )
        paint.style =
            Paint.Style.FILL
        paint.textSize = 12f
        paint.isFakeBoldText = true
        canvas.drawText(
            "Producto",
            50f,
            y + 20,
            paint
        )
        canvas.drawText(
            "Tipo",
            240f,
            y + 20,
            paint
        )
        canvas.drawText(
            "Stock",
            370f,
            y + 20,
            paint
        )
        canvas.drawText(
            "Precio",
            470f,
            y + 20,
            paint
        )
        paint.isFakeBoldText = false
        return y + 30
    }
    fun nuevaPagina(){
        documento.finishPage(
            pagina
        )
        numeroPagina++
        pagina =
            documento.startPage(
                PdfDocument.PageInfo.Builder(
                    ancho,
                    alto,
                    numeroPagina
                ).create()
            )
        canvas =
            pagina.canvas
        titulo()
    }
    titulo()
    var y =
        cabecera(110f)
    productos.forEach { producto ->
        if(y > 760){
            nuevaPagina()
            y =
                cabecera(110f)
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
            220f,
            y,
            220f,
            y + 25f,
            paint
        )
        canvas.drawLine(
            350f,
            y,
            350f,
            y + 25f,
            paint
        )
        canvas.drawLine(
            430f,
            y,
            430f,
            y + 25f,
            paint
        )
        paint.style =
            Paint.Style.FILL
        paint.textSize = 10f
        val nombre =
            if(producto.Nombre.length > 25)
                producto.Nombre.substring(
                    0,
                    25
                ) + "..."
            else
                producto.Nombre
        canvas.drawText(
            nombre,
            50f,
            y + 17,
            paint
        )
        canvas.drawText(
            producto.Tipo,
            240f,
            y + 17,
            paint
        )
        canvas.drawText(
            producto.Cantidad.toString(),
            370f,
            y + 17,
            paint
        )
        canvas.drawText(
            "$${producto.Precio}",
            470f,
            y + 17,
            paint
        )
        y += 25
    }
    documento.finishPage(
        pagina
    )
    val fechaArchivo =
        SimpleDateFormat(
            "dd-MM-yyyy",
            Locale.getDefault()
        ).format(Date())
    val nombreArchivo =
        "Stock_CritStore_$fechaArchivo.pdf"
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
        resolver.openOutputStream(it)
            ?.use { salida ->
                documento.writeTo(
                    salida
                )
            }
        Toast.makeText(
            context,
            "PDF guardado correctamente",
            Toast.LENGTH_LONG
        ).show()
    }
    documento.close()
}