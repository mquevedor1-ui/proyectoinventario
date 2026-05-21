package com.example.inventario.ui.inventario
import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.widget.Toast
import androidx.core.content.FileProvider
import com.example.inventario.data.producto
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun exportarInventarioPDF(context: Context, productos: List<producto>, bodega: String) {
    val pdfDocument = PdfDocument()
    // Aumentamos el ancho a 1600 para que quepan todos los campos sin amontonarse
    val pageInfo = PdfDocument.PageInfo.Builder(1600, 2000, 1).create()
    val page = pdfDocument.startPage(pageInfo)
    val canvas = page.canvas

    val tituloPaint = Paint().apply { textSize = 35f; isFakeBoldText = true }
    val textoPaint = Paint().apply { textSize = 15f }
    val encabezadoPaint = Paint().apply { textSize = 15f; isFakeBoldText = true }
    val lineaPaint = Paint().apply { strokeWidth = 1f }

    canvas.drawText("Reporte General de Inventario", 50f, 60f, tituloPaint)
    val fechaActual = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date())
    canvas.drawText("Bodega: $bodega", 50f, 100f, textoPaint)
    canvas.drawText("Fecha: $fechaActual", 1300f, 100f, textoPaint)

    var y = 160f
    // Encabezados con distancias ajustadas
    canvas.drawText("Código", 40f, y, encabezadoPaint)
    canvas.drawText("Descripción", 160f, y, encabezadoPaint)
    canvas.drawText("Categoría", 380f, y, encabezadoPaint)
    canvas.drawText("Cant.", 550f, y, encabezadoPaint)
    canvas.drawText("Unidad", 630f, y, encabezadoPaint)
    canvas.drawText("Ubicación", 730f, y, encabezadoPaint)
    canvas.drawText("Proveedor", 880f, y, encabezadoPaint)
    canvas.drawText("Costo", 1050f, y, encabezadoPaint)
    canvas.drawText("Total", 1150f, y, encabezadoPaint)
    canvas.drawText("Fecha Ing.", 1270f, y, encabezadoPaint)
    canvas.drawText("Notas", 1400f, y, encabezadoPaint)

    y += 10f
    canvas.drawLine(40f, y, 1550f, y, lineaPaint)
    y += 35f

    var granTotal = 0.0
    productos.forEach { p ->
        val total = p.cantidad * p.costo
        granTotal += total

        canvas.drawText(p.codigo, 40f, y, textoPaint)
        canvas.drawText(p.descripcion.take(25), 160f, y, textoPaint)
        canvas.drawText(p.categoria.take(15), 380f, y, textoPaint)
        canvas.drawText(p.cantidad.toString(), 550f, y, textoPaint)
        canvas.drawText(p.unidad, 630f, y, textoPaint)
        canvas.drawText(p.ubicacion, 730f, y, textoPaint)
        canvas.drawText(p.proveedor.take(15), 880f, y, textoPaint)
        canvas.drawText("Q${String.format(Locale.US, "%.2f", p.costo)}", 1050f, y, textoPaint)
        canvas.drawText("Q${String.format(Locale.US, "%.2f", total)}", 1150f, y, textoPaint)
        canvas.drawText(p.fechaIngreso.take(10), 1270f, y, textoPaint)
        canvas.drawText(p.notas.take(20), 1400f, y, textoPaint)
        y += 30f
    }

    y += 20f
    canvas.drawLine(40f, y, 1550f, y, lineaPaint)
    y += 40f
    canvas.drawText("VALOR TOTAL DEL INVENTARIO: Q ${String.format(Locale.US, "%.2f", granTotal)}", 1000f, y, tituloPaint.apply { textSize = 25f })

    pdfDocument.finishPage(page)
    val file = File(context.cacheDir, "inventario_${System.currentTimeMillis()}.pdf")
    try {
        val os = FileOutputStream(file)
        pdfDocument.writeTo(os)
        os.close()
        pdfDocument.close()
        val uri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
        context.startActivity(Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(uri, "application/pdf")
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_ACTIVITY_NEW_TASK)
        })
    } catch (e: Exception) { e.printStackTrace() }
}
