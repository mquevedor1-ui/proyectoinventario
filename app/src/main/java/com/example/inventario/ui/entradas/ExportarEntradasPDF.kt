package com.example.inventario.ui.entradas

import android.content.Context
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.widget.Toast
import androidx.core.content.FileProvider
import com.example.inventario.data.Entrada
import java.io.File
import java.io.FileOutputStream

fun exportarEntradasPDF(
    context: Context,
    entradas: List<Entrada>
) {
    val file = File(context.cacheDir, "entradas_${System.currentTimeMillis()}.pdf")
    val pdfDocument = PdfDocument()
    val paint = Paint()
    val titlePaint = Paint()

    val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create()
    val page = pdfDocument.startPage(pageInfo)
    val canvas: Canvas = page.canvas

    titlePaint.textSize = 18f
    titlePaint.isFakeBoldText = true
    canvas.drawText("Reporte de Entradas de Inventario", 40f, 50f, titlePaint)

    paint.textSize = 12f
    var yPosition = 100f

    // Encabezados
    paint.isFakeBoldText = true
    canvas.drawText("Fecha", 40f, yPosition, paint)
    canvas.drawText("Código", 120f, yPosition, paint)
    canvas.drawText("Descripción", 200f, yPosition, paint)
    canvas.drawText("Cant.", 400f, yPosition, paint)
    canvas.drawText("Proveedor", 460f, yPosition, paint)

    yPosition += 20f
    paint.isFakeBoldText = false
    canvas.drawLine(40f, yPosition - 10f, 550f, yPosition - 10f, paint)

    entradas.forEach { entrada ->
        if (yPosition > 800) {
            pdfDocument.finishPage(page)
            // Aquí se podría añadir lógica para nueva página si fuera necesario
            return@forEach 
        }
        canvas.drawText(entrada.fecha, 40f, yPosition, paint)
        canvas.drawText(entrada.codigo, 120f, yPosition, paint)
        canvas.drawText(entrada.descripcion.take(20), 200f, yPosition, paint)
        canvas.drawText(entrada.cantidad.toString(), 400f, yPosition, paint)
        canvas.drawText(entrada.proveedor.take(15), 460f, yPosition, paint)
        yPosition += 20f
    }

    pdfDocument.finishPage(page)

    try {
        val fos = FileOutputStream(file)
        pdfDocument.writeTo(fos)
        fos.flush()
        fos.close()
        pdfDocument.close()

        val uri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
        val intent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(uri, "application/pdf")
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(Intent.createChooser(intent, "Abrir Reporte de Entradas"))
    } catch (e: Exception) {
        e.printStackTrace()
        Toast.makeText(context, "Error al generar PDF: ${e.message}", Toast.LENGTH_LONG).show()
    }
}
