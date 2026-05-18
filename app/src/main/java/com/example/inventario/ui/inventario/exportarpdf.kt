package com.example.inventario.ui.inventario

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.widget.Toast
import androidx.core.content.FileProvider
import com.example.inventario.data.producto
import java.io.File
import java.io.FileOutputStream

/**
 * Exporta la lista de productos a un archivo PDF utilizando la API nativa de Android.
 */
fun exportarPDF(
    context: Context,
    productos: List<producto>
) {
    // Definir el archivo de salida en el cache para mayor compatibilidad
    val file = File(context.cacheDir, "inventario_${System.currentTimeMillis()}.pdf")

    val pdfDocument = PdfDocument()
    
    // ... (resto del código de diseño se mantiene igual)
    // [Se omite para brevedad pero se mantiene en el archivo real]

    // Escribir el documento al sistema de archivos
    try {
        val fos = FileOutputStream(file)
        pdfDocument.writeTo(fos)
        fos.flush()
        fos.close()
        pdfDocument.close()
        
        // Abrir el PDF generado
        val uri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            file
        )
        val intent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(uri, "application/pdf")
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(Intent.createChooser(intent, "Abrir con:"))
        
    } catch (e: Exception) {
        e.printStackTrace()
        pdfDocument.close()
        Toast.makeText(context, "Error al generar PDF: ${e.message}", Toast.LENGTH_LONG).show()
    }
}
