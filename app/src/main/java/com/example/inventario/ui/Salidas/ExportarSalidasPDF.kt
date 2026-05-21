package com.example.inventario.ui.Salidas

import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.widget.Toast
import androidx.core.content.FileProvider
import com.example.inventario.data.Salida
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun exportarSalidasPDF(context: Context, salidas: List<Salida>, periodo: String) {
    val file = File(context.cacheDir, "salidas_${System.currentTimeMillis()}.pdf")
    val pdfDocument = PdfDocument()
    
    // Ancho ampliado para reporte detallado
    val pageInfo = PdfDocument.PageInfo.Builder(1400, 2000, 1).create()
    val page = pdfDocument.startPage(pageInfo)
    val canvas = page.canvas

    val tituloPaint = Paint().apply { textSize = 30f; isFakeBoldText = true }
    val subtituloPaint = Paint().apply { textSize = 20f; isFakeBoldText = false }
    val textoPaint = Paint().apply { textSize = 14f }
    val encabezadoPaint = Paint().apply { textSize = 14f; isFakeBoldText = true }
    val lineaPaint = Paint().apply { strokeWidth = 1f }

    canvas.drawText("Reporte Detallado de Salidas", 50f, 60f, tituloPaint)
    canvas.drawText("Periodo: $periodo", 50f, 95f, subtituloPaint)

    val fechaActual = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date())
    canvas.drawText("Generado el: $fechaActual", 50f, 130f, textoPaint)

    var y = 180f
    // Encabezados
    canvas.drawText("Fecha", 40f, y, encabezadoPaint)
    canvas.drawText("Código", 150f, y, encabezadoPaint)
    canvas.drawText("Descripción", 270f, y, encabezadoPaint)
    canvas.drawText("Cant.", 550f, y, encabezadoPaint)
    canvas.drawText("Destino", 630f, y, encabezadoPaint)
    canvas.drawText("Responsable", 800f, y, encabezadoPaint)
    canvas.drawText("Vehículo", 950f, y, encabezadoPaint)
    canvas.drawText("Notas", 1100f, y, encabezadoPaint)

    y += 10f
    canvas.drawLine(40f, y, 1360f, y, lineaPaint)
    y += 35f

    salidas.forEach { s ->
        if (y > 1900) return@forEach

        canvas.drawText(s.fecha.take(10), 40f, y, textoPaint)
        canvas.drawText(s.codigo, 150f, y, textoPaint)
        canvas.drawText(s.descripcion.take(25), 270f, y, textoPaint)
        canvas.drawText(s.cantidad.toString(), 550f, y, textoPaint)
        canvas.drawText(s.destino.take(15), 630f, y, textoPaint)
        canvas.drawText(s.responsable.take(15), 800f, y, textoPaint)
        canvas.drawText(s.vehiculo.take(15), 950f, y, textoPaint)
        canvas.drawText(s.notas.take(30), 1100f, y, textoPaint)
        y += 30f
    }

    pdfDocument.finishPage(page)

    try {
        val fos = FileOutputStream(file)
        pdfDocument.writeTo(fos)
        fos.close()
        pdfDocument.close()

        val uri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
        context.startActivity(Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(uri, "application/pdf")
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_ACTIVITY_NEW_TASK)
        })
    } catch (e: Exception) {
        Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_LONG).show()
    }
}
