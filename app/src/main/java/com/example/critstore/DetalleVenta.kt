package com.example.critstore

import android.content.*
import android.graphics.*
import android.graphics.pdf.*
import android.os.*
import android.provider.*
import android.util.*
import android.widget.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.platform.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import kotlinx.coroutines.*
import java.text.*
import java.util.*

@Composable
fun DetalleVenta(
    planillaId: Int,
    planillaDao: PlanillaDao,
    planilla: PlanillaVenta,
    volver: () -> Unit,
    editarPlanilla: (PlanillaVenta) -> Unit
)
 {
    val scope = rememberCoroutineScope()
     val dim = obtenerDimensiones()
    val context = LocalContext.current
    var detalles by remember {
        mutableStateOf<List<DetallePlanilla>>(emptyList())
    }
     var sincronizando by remember {
         mutableStateOf(false)
     }
    LaunchedEffect(planillaId) {
        detalles = planillaDao.obtenerDetallePlanilla(
            planilla.id
        )
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = dim.paddingPantalla * 2,
                start = dim.paddingPantalla,
                end = dim.paddingPantalla,
                bottom = dim.paddingPantalla)
    ) {
        Text(
            text = "Detalle de Venta",
            fontSize = dim.titulo,
            fontWeight = FontWeight.Bold)
        Spacer(
            Modifier.height(dim.espacio)
        )
        Text(
            text = "Evento: ${planilla.NombreEvento}",
            fontSize = dim.texto,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Fecha: ${planilla.FechaDesde}",
            fontSize = dim.texto
        )
        Spacer(
            Modifier.height(dim.espacio)
        )
        LazyColumn(
            modifier = Modifier
                .weight(1f)
        ) {
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    Text(
                        "Producto",
                        fontSize = dim.texto,
                        modifier = Modifier.weight(2f)
                    )
                    Text(
                        "Precio",
                        fontSize = dim.texto,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        "Cant.",
                        fontSize = dim.texto,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        "Total",
                        fontSize = dim.texto,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
            items(detalles) { detalle ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp)
                ) {
                    Text(
                        detalle.Nombre,
                        fontSize = dim.texto,
                        modifier = Modifier.weight(2f)
                    )
                    Text(
                        "$${detalle.Precio}",
                        fontSize = dim.texto,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        detalle.Ventas.toString(),
                        fontSize = dim.texto,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        "$${detalle.Total}",
                        fontSize = dim.texto,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
        val totalVenta =
            detalles.sumOf {
                it.Total
            }
        Text(
            text = "TOTAL: $$totalVenta",
            fontSize = dim.titulo,
            fontWeight = FontWeight.Bold
        )
        Spacer(
            Modifier.height(dim.espacio)
        )
        Button(
            onClick = {
                editarPlanilla(planilla)
            },
            modifier =Modifier
                .widthIn(dim.alturaBotonInterno)
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally)
        ) {
            Text("✏️ Modificar Venta"
            ,fontSize = dim.texto)
        }
        Spacer(
            modifier = Modifier.height(dim.espacio)
        )
       /* Button(
            onClick = {
                scope.launch {
                    val planillaActualizada =
                        planillaDao.obtenerPlanillaPorId(planilla.id)
                    Log.d(
                        "TOTAL_DETALLES",
                        "Cantidad=${detalles.size}"
                    )
                    eliminarDetallesPlanilla(planilla.Uudd)
                    detalles.forEach { detalle ->
                        Log.d(
                            "ANTES_SYNC",
                            "Producto=${detalle.Nombre} UUID=${detalle.Uudd}"
                        )
                            sincronizarDetalle(
                            detalle,
                            planillaActualizada
                        )
                    }
                    Toast.makeText(
                        context,
                        "✅ Venta sincronizada correctamente",
                        Toast.LENGTH_LONG
                    ).show()
                }
            },
            modifier =Modifier
                .widthIn(dim.alturaBotonInterno)
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally)
        ) {
            Text(
                "🔄 Sincronizar Venta",
            )
        }*/
        Button(
            onClick = {

                if (sincronizando) {
                    return@Button
                }

                scope.launch {

                    sincronizando = true

                    try {

                        val planillaActualizada =
                            planillaDao.obtenerPlanillaPorId(
                                planilla.id
                            )

                        Log.d(
                            "TOTAL_DETALLES",
                            "Cantidad=${detalles.size}"
                        )

                        eliminarDetallesPlanilla(
                            planilla.Uudd
                        )

                        detalles.forEach { detalle ->

                            Log.d(
                                "ANTES_SYNC",
                                "Producto=${detalle.Nombre} UUID=${detalle.Uudd}"
                            )

                            sincronizarDetalle(
                                detalle,
                                planillaActualizada
                            )
                        }

                        Toast.makeText(
                            context,
                            "✅ Venta sincronizada correctamente",
                            Toast.LENGTH_LONG
                        ).show()

                    } catch (e: Exception) {

                        Log.e(
                            "SYNC_VENTA",
                            "Error sincronizando venta",
                            e
                        )

                        Toast.makeText(
                            context,
                            "❌ Error al sincronizar: ${e.message}",
                            Toast.LENGTH_LONG
                        ).show()

                    } finally {

                        sincronizando = false
                    }
                }
            },

            enabled = !sincronizando,

            modifier = Modifier
                .widthIn(
                    min = dim.alturaBotonInterno
                )
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally),

            colors = ButtonDefaults.buttonColors(
                containerColor =
                    if (sincronizando)
                        MaterialTheme.colorScheme.surfaceVariant
                    else
                        MaterialTheme.colorScheme.primary,

                contentColor =
                    if (sincronizando)
                        MaterialTheme.colorScheme.onSurfaceVariant
                    else
                        MaterialTheme.colorScheme.onPrimary,

                disabledContainerColor =
                    MaterialTheme.colorScheme.surfaceVariant,

                disabledContentColor =
                    MaterialTheme.colorScheme.onSurfaceVariant
            )
        ) {

            Text(
                text =
                    if (sincronizando)
                        "⏳ Sincronizando..."
                    else
                        "☁ Sincronizar Venta",

                fontSize = dim.texto
            )
        }
        Spacer(
            Modifier.height(dim.espacio)
        )
        Button(
            onClick = {
                val totalVenta =
                    detalles.sumOf {
                        it.Total
                    }
                generarPDF(
                    context = context,
                    detalles = detalles,
                    total = totalVenta,
                    nombreEvento = planilla.NombreEvento,
                    fechaDesde = planilla.FechaDesde,
                    fechaHasta = planilla.FechaHasta
                )
            },
            modifier =Modifier
                .widthIn(dim.alturaBotonInterno)
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally)
        ) {
            Text(
                "📄 Generar PDF",
                fontSize = dim.texto
            )
        }
        Spacer(
            modifier = Modifier.height(dim.espacio)
        )
        Button(
            onClick = {
                volver()
            },
            modifier =Modifier
                .widthIn(dim.alturaBotonInterno)
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally)
        ) {
            Text(
                "Volver",  fontSize = dim.texto
            )
        }
        Spacer(
            modifier = Modifier.height(dim.espacio)
        )
    }
}
fun generarPDF(
    context: Context,
    detalles: List<DetallePlanilla>,
    total: Int,
    nombreEvento: String,
    fechaDesde: String,
    fechaHasta: String
) {
    val documento = PdfDocument()
    val ancho = 595
    val alto = 842
    var paginaNumero = 1
    var pagina =
        documento.startPage(
            PdfDocument.PageInfo.Builder(
                ancho,
                alto,
                paginaNumero
            ).create()
        )
    var canvas = pagina.canvas
    val paint = Paint()
    fun dibujarEncabezado() {
        paint.style = Paint.Style.FILL
        try {
            val bitmap =
                BitmapFactory.decodeResource(
                    context.resources,
                    R.drawable.logo_critstore
                )
            val logo =
                Bitmap.createScaledBitmap(
                    bitmap,
                    70,
                    70,
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
        paint.textSize = 24f
        paint.isFakeBoldText = true
        canvas.drawText(
            "CritStore",
            40f,
            50f,
            paint
        )
        paint.textSize = 16f
        canvas.drawText(
            "Detalle de Venta",
            40f,
            80f,
            paint
        )
        paint.textSize = 12f
        paint.isFakeBoldText = false
        canvas.drawText(
            "Evento: $nombreEvento",
            40f,
            115f,
            paint
        )
        canvas.drawText(
            "Desde: $fechaDesde",
            40f,
            135f,
            paint
        )
        canvas.drawText(
            "Hasta: $fechaHasta",
            40f,
            155f,
            paint
        )
    }
    fun dibujarTabla(
        yInicial: Float
    ): Float {
        var y = yInicial
        val xInicio = 40f
        val xFin = 555f
        val columnaProducto = 50f
        val columnaCantidad = 360f
        val columnaTotal = 470f
        paint.style = Paint.Style.STROKE
        canvas.drawRect(
            xInicio,
            y,
            xFin,
            y + 30,
            paint
        )
        canvas.drawLine(
            340f,
            y,
            340f,
            y + 30,
            paint
        )
        canvas.drawLine(
            440f,
            y,
            440f,
            y + 30,
            paint
        )
        paint.style = Paint.Style.FILL
        paint.textSize = 12f
        paint.isFakeBoldText = true
        canvas.drawText(
            "Producto",
            columnaProducto,
            y + 20,
            paint
        )
        canvas.drawText(
            "Cantidad",
            columnaCantidad,
            y + 20,
            paint
        )
        canvas.drawText(
            "Total",
            columnaTotal,
            y + 20,
            paint
        )
        y += 30
        paint.isFakeBoldText = false
        detalles.forEach { detalle ->
            if(y > 760){
                documento.finishPage(
                    pagina
                )
                paginaNumero++
                pagina =
                    documento.startPage(
                        PdfDocument.PageInfo.Builder(
                            ancho,
                            alto,
                            paginaNumero
                        ).create()
                    )
                canvas =
                    pagina.canvas
                dibujarEncabezado()
                y = 190f
            }
            paint.style =
                Paint.Style.STROKE
            canvas.drawRect(
                xInicio,
                y,
                xFin,
                y + 25,
                paint
            )
            canvas.drawLine(
                340f,
                y,
                340f,
                y + 25,
                paint
            )
            canvas.drawLine(
                440f,
                y,
                440f,
                y + 25,
                paint
            )
            paint.style =
                Paint.Style.FILL
            paint.textSize = 11f
            val nombre =
                if(detalle.Nombre.length > 35)
                    detalle.Nombre.substring(
                        0,
                        35
                    ) + "..."
                else
                    detalle.Nombre
            canvas.drawText(
                nombre,
                columnaProducto,
                y + 17,
                paint
            )
            canvas.drawText(
                detalle.Ventas.toString(),
                columnaCantidad,
                y + 17,
                paint
            )
            canvas.drawText(
                "$${detalle.Total}",
                columnaTotal,
                y + 17,
                paint
            )
            y += 25
        }
        return y
    }
    dibujarEncabezado()
    val yFinal =
        dibujarTabla(
            190f
        )
    paint.textSize = 18f
    paint.isFakeBoldText = true
    canvas.drawText(
        "TOTAL VENTA: $$total",
        40f,
        yFinal + 40,
        paint
    )
    documento.finishPage(
        pagina
    )
    val fecha =
        SimpleDateFormat(
            "dd-MM-yyyy",
            Locale.getDefault()
        ).format(Date())
    val eventoLimpio =
        nombreEvento
            .replace(
                "[^a-zA-Z0-9 ]".toRegex(),
                ""
            )
            .replace(
                " ",
                "_"
            )
    val nombreArchivo =
        "Venta_${eventoLimpio}_${fecha}.pdf"
    val valores =
        ContentValues().apply {
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
        val uri =
            context.contentResolver.insert(
                MediaStore.Downloads.EXTERNAL_CONTENT_URI,
                valores
            )
        uri?.let {
            context.contentResolver
                .openOutputStream(it)
                ?.use { salida ->
                    documento.writeTo(
                        salida
                    )
                }
        }
        Toast.makeText(
            context,
            "PDF guardado: $nombreArchivo",
            Toast.LENGTH_LONG
        ).show()
    } catch(e: Exception){
        Toast.makeText(
            context,
            "Error PDF: ${e.message}",
            Toast.LENGTH_LONG
        ).show()
    }
    documento.close()
}