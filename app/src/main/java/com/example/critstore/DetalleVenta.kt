package com.example.critstore

import android.content.ContentValues
import android.content.Context
import android.graphics.Canvas
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat
import java.util.*
import android.graphics.Bitmap
import android.graphics.BitmapFactory

@Composable
fun DetalleVenta(
    planillaId: Int,
    planillaDao: PlanillaDao,
    planilla: PlanillaVenta,
    volver: () -> Unit
) {
    val context = LocalContext.current
    var detalles by remember {
        mutableStateOf(emptyList<DetallePlanilla>())
    }
    LaunchedEffect(planillaId) {
        detalles = planillaDao.obtenerDetallePlanilla(planillaId)
    }
    val totalGeneral = detalles.sumOf {
        it.total
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Detalle de Venta",
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(
            modifier = Modifier.height(15.dp)
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
                    "Producto",
                    modifier = Modifier.weight(2f)
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
        LazyColumn(
            modifier = Modifier.weight(1f)
        ) {
            items(detalles) { detalle ->
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
                            detalle.Nombre,
                            modifier = Modifier.weight(2f)
                        )
                        Text(
                            detalle.Ventas.toString(),
                            modifier = Modifier.weight(1f)
                        )
                        Text(
                            "$${detalle.total}",
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                "TOTAL VENTA: $${totalGeneral}",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(15.dp)
            )
        }
        Spacer(
            modifier = Modifier.height(10.dp)
        )
        Button(
            onClick = {
                generarPDF(
                    context,
                    detalles,
                    totalGeneral,
                    planilla.nombreEvento,
                    planilla.fechaDesde,
                    planilla.fechaHasta
                )
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("📄 Exportar PDF")
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
fun generarPDF(
    context: Context,
    detalles: List<DetallePlanilla>,
    total: Int,
    nombreEvento: String,
    fechaDesde: String,
    fechaHasta: String
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
        "Detalle de Venta",
        40f,
        y,
        paint
    )
    y += 35
    paint.textSize = 14f
    paint.isFakeBoldText = false
    canvas.drawText(
        "Evento: $nombreEvento",
        40f,
        y,
        paint
    )
    y += 25
    canvas.drawText(
        "Fecha Desde: $fechaDesde",
        40f,
        y,
        paint
    )
    y += 25
    canvas.drawText(
        "Fecha Hasta: $fechaHasta",
        40f,
        y,
        paint
    )
    y += 35
    val fechaGeneracion =
        SimpleDateFormat(
            "dd/MM/yyyy HH:mm",
            Locale.getDefault()
        ).format(Date())
    paint.textSize = 11f
    canvas.drawText(
        "Generado: $fechaGeneracion",
        40f,
        y,
        paint
    )
    y += 35
    // CABECERA TABLA
    paint.textSize = 14f
    paint.isFakeBoldText = true
    canvas.drawText(
        "Producto",
        50f,
        y,
        paint
    )
    canvas.drawText(
        "Cantidad",
        300f,
        y,
        paint
    )
    canvas.drawText(
        "Total",
        450f,
        y,
        paint
    )
    y += 10
    canvas.drawLine(
        40f,
        y,
        550f,
        y,
        paint
    )
    y += 25
    paint.textSize = 12f
    paint.isFakeBoldText = false
    detalles.forEach { detalle ->
        canvas.drawText(
            detalle.Nombre,
            50f,
            y,
            paint
        )
        canvas.drawText(
            detalle.Ventas.toString(),
            320f,
            y,
            paint
        )
        canvas.drawText(
            "$${detalle.total}",
            450f,
            y,
            paint
        )
        canvas.drawLine(
            40f,
            y + 8,
            550f,
            y + 8,
            paint
        )
        y += 30
    }
    y += 20
    paint.textSize = 18f
    paint.isFakeBoldText = true
    canvas.drawText(
        "TOTAL VENTA: $${total}",
        330f,
        y,
        paint
    )
    // LOGO PIE DE PAGINA
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
    // NOMBRE ARCHIVO PDF
    val nombreArchivo =
        "${nombreEvento}_${fechaDesde}_${fechaHasta}.pdf"
            .replace(
                "/",
                "-"
            )
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
        val uri = context.contentResolver.insert(
            MediaStore.Downloads.EXTERNAL_CONTENT_URI,
            valores
        )
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