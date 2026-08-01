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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.RectF


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
    val context = LocalContext.current
    var detalles by remember {
        mutableStateOf<List<DetallePlanilla>>(emptyList())
    }
    LaunchedEffect(planillaId) {
        detalles = planillaDao.obtenerDetallePlanilla(
            planillaId
        )
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Detalle de Venta",
            style = MaterialTheme.typography.headlineSmall
        )
        Spacer(
            modifier = Modifier.height(10.dp)
        )
        Text(
            text = "Evento: ${planilla.NombreEvento}",
            style = MaterialTheme.typography.titleMedium
        )
        Text(
            text = "Fecha: ${planilla.FechaDesde}",
        )
        Spacer(
            modifier = Modifier.height(20.dp)
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
                        modifier = Modifier.weight(2f)
                    )
                    Text(
                        "Precio",
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        "Cant.",
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        "Total",
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
                        modifier = Modifier.weight(2f)
                    )
                    Text(
                        "$${detalle.Precio}",
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        detalle.Ventas.toString(),
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        "$${detalle.Total}",
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
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier
                .align(Alignment.End)
                .padding(8.dp)
        )
        Spacer(
            modifier = Modifier.height(10.dp)
        )
        Button(
            onClick = {
                editarPlanilla(planilla)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("✏️ Modificar Venta")
        }
        Button(
            onClick = {
                scope.launch {
                    detalles.forEach { detalle ->
                        sincronizarDetalle(
                            detalle,
                            planilla
                        )
                    }
                    Toast.makeText(
                        context,
                        "✅ Venta sincronizada correctamente",
                        Toast.LENGTH_LONG
                    ).show()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                "🔄 Sincronizar Venta"
            )
        }
        Spacer(
            modifier = Modifier.height(10.dp)
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
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                "📄 Generar PDF"
            )
        }
        Button(
            onClick = {
                volver()
            },
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                "Volver"
            )
        }
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