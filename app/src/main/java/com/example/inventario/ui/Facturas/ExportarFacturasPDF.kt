package com.example.inventario.ui.Facturas

import android.content.Context
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.widget.Toast
import androidx.core.content.FileProvider
import com.example.inventario.data.Factura
import java.io.File
import java.io.FileOutputStream

fun exportarFacturasPDF(
    context: Context,
    facturas: List<Factura>
) {
    val file = File(context.cacheDir, "facturas_${System.currentTimeMillis()}.pdf")
    val pdfDocument = PdfDocument()
    val paint = Paint()
    val titlePaint = Paint()

    val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create()
    val page = pdfDocument.startPage(pageInfo)
    val canvas: Canvas = page.canvas

    titlePaint.textSize = 18f
    titlePaint.isFakeBoldText = true
    canvas.drawText("Reporte de Facturas", 40f, 50f, titlePaint)

    paint.textSize = 12f
    var yPosition = 100f

    // Encabezados
    paint.isFakeBoldText = true
    canvas.drawText("N° Factura", 40f, yPosition, paint)
    canvas.drawText("Fecha", 140f, yPosition, paint)
    canvas.drawText("Proveedor", 240f, yPosition, paint)
    canvas.drawText("Total", 480f, yPosition, paint)

    yPosition += 20f
    paint.isFakeBoldText = false
    canvas.drawLine(40f, yPosition - 10f, 550f, yPosition - 10f, paint)

    facturas.forEach { factura ->
        if (yPosition > 800) {
            pdfDocument.finishPage(page)
            return@forEach
        }
        canvas.drawText(factura.numeroFactura, 40f, yPosition, paint)
        canvas.drawText(factura.fecha, 140f, yPosition, paint)
        canvas.drawText(factura.proveedor.take(25), 240f, yPosition, paint)
        canvas.drawText("$ ${factura.total}", 480f, yPosition, paint)
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
        context.startActivity(Intent.createChooser(intent, "Abrir Reporte de Facturas"))
    } catch (e: Exception) {
        e.printStackTrace()
        Toast.makeText(context, "Error al generar PDF: ${e.message}", Toast.LENGTH_LONG).show()
    }
}
