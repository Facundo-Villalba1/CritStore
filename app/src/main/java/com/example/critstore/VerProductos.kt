package com.example.critstore

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import android.content.ContentValues
import android.content.Context
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.os.Environment
import android.provider.MediaStore
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.RectF

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VerProductos(
    productoDao: ProductoDao,
    volver: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val configuration = LocalConfiguration.current
    val esTablet =
        configuration.screenWidthDp >= 600
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
                sincronizarProducto(
                    producto
                )
            }
            Toast.makeText(
                context,
                "Productos enviados a Google",
                Toast.LENGTH_LONG
            ).show()
        } catch (e: Exception) {
            Toast.makeText(
                context,
                "Error enviando Google: ${e.message}",
                Toast.LENGTH_LONG
            ).show()
        } finally {
            sincronizando = false
        }
    }
    LaunchedEffect(Unit) {
        cargarDesdeGoogle()
    }
    val productosFiltrados =
        if (
            tipoSeleccionado == "Todos"
        ) {
            productos
        } else {
            productos.filter {
                it.Tipo.trim() ==
                        tipoSeleccionado.trim()
            }
        }
    val anchoProducto =
        if (esTablet)
            3f
        else
            2f
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                if (esTablet)
                    32.dp
                else
                    16.dp
            )
    ) {
        Text(
            text = "Ver Productos",
            style =
                if (esTablet)
                    MaterialTheme.typography.headlineLarge
                else
                    MaterialTheme.typography.headlineMedium
        )
        Spacer(
            modifier =
                Modifier.height(16.dp)
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
                    Text(
                        "Filtrar por tipo"
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
        Spacer(
            modifier = Modifier.height(16.dp)
        )
        Card(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        if(esTablet)
                            14.dp
                        else
                            10.dp
                    )
            ) {
                Text(
                    text = "Producto",
                    modifier =
                        Modifier.weight(
                            anchoProducto
                        ),
                    fontSize =
                        if(esTablet)
                            16.sp
                        else
                            13.sp
                )
                Text(
                    text = "Stock",
                    modifier =
                        Modifier.weight(1f),
                    fontSize =
                        if(esTablet)
                            16.sp
                        else
                            13.sp
                )
                Text(
                    text = "Precio",
                    modifier =
                        Modifier.weight(1f),
                    fontSize =
                        if(esTablet)
                            16.sp
                        else
                            13.sp
                )
            }
        }
        Spacer(
            modifier = Modifier.height(6.dp)
        )
        LazyColumn(
            modifier = Modifier
                .weight(1f)
        ) {
            items(
                productosFiltrados
            ) { producto ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            vertical = 4.dp
                        )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                if(esTablet)
                                    14.dp
                                else
                                    10.dp
                            )
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
                            fontSize =
                                if(esTablet)
                                    16.sp
                                else
                                    13.sp
                        )
                        Text(
                            text =
                                producto.Cantidad
                                    .toString(),
                            modifier =
                                Modifier.weight(1f),
                            fontSize =
                                if(esTablet)
                                    16.sp
                                else
                                    13.sp
                        )
                        Text(
                            text =
                                "$${producto.Precio}",
                            modifier =
                                Modifier.weight(1f),
                            fontSize =
                                if(esTablet)
                                    16.sp
                                else
                                    13.sp
                        )
                    }
                }
            }
        }
        Spacer(
            modifier = Modifier.height(12.dp)
        )
        Button(
            onClick = {
                scope.launch {
                    sincronizarGoogle()
                }
            },
            enabled = !sincronizando,
            modifier =
                if(esTablet)
                    Modifier
                        .width(350.dp)
                        .align(
                            androidx.compose.ui.Alignment.CenterHorizontally
                        )
                else
                    Modifier
                        .fillMaxWidth()
        ) {
            Text(
                text =
                    if(sincronizando)
                        "☁ Enviando..."
                    else
                        "☁ Guardar en Google"
            )
        }
        Spacer(
            modifier =
                Modifier.height(10.dp)
        )
        Button(
            onClick = {
                generarPDFStock(
                    context,
                    productosFiltrados
                )
            },
            modifier =
                if(esTablet)
                    Modifier
                        .width(350.dp)
                        .align(
                            androidx.compose.ui.Alignment.CenterHorizontally
                        )
                else
                    Modifier
                        .fillMaxWidth()
        ) {
            Text(
                "📄 Generar PDF Stock"
            )
        }
        Spacer(
            modifier =
                Modifier.height(10.dp)
        )
        OutlinedButton(
            onClick = volver,
            modifier =
                if(esTablet)
                    Modifier
                        .width(350.dp)
                        .align(
                            androidx.compose.ui.Alignment.CenterHorizontally
                        )
                else
                    Modifier
                        .fillMaxWidth()
        ) {
            Text(
                "⬅ Volver"
            )
        }
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