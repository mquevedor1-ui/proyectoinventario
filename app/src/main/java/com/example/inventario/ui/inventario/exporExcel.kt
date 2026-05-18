package com.example.inventario.ui.inventario

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.core.content.FileProvider
import com.example.inventario.data.producto
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.File
import java.io.FileOutputStream

fun exportarExcel(
    context: Context,
    productos: List<producto>
) {
    // workbook
    val workbook = XSSFWorkbook()

    // hoja
    val sheet = workbook.createSheet("Inventario")

    // encabezados
    val headers = listOf(
        "Código",
        "Descripción",
        "Categoría",
        "Cantidad",
        "Unidad",
        "Ubicación",
        "Proveedor",
        "Costo",
        "Stock Mínimo",
        "Fecha",
        "Notas"
    )

    // fila encabezado
    val headerRow = sheet.createRow(0)
    headers.forEachIndexed { index, titulo ->
        headerRow.createCell(index).setCellValue(titulo)
    }

    // productos
    productos.forEachIndexed { index, producto ->
        val row = sheet.createRow(index + 1)
        row.createCell(0).setCellValue(producto.codigo)
        row.createCell(1).setCellValue(producto.descripcion)
        row.createCell(2).setCellValue(producto.categoria)
        row.createCell(3).setCellValue(producto.cantidad.toDouble())
        row.createCell(4).setCellValue(producto.unidad)
        row.createCell(5).setCellValue(producto.ubicacion)
        row.createCell(6).setCellValue(producto.proveedor)
        row.createCell(7).setCellValue(producto.costo)
        row.createCell(8).setCellValue(producto.stockMinimo.toDouble())
        row.createCell(9).setCellValue(producto.fechaIngreso)
        row.createCell(10).setCellValue(producto.notas)
    }

    // ajustar columnas
    for (i in headers.indices) {
        sheet.autoSizeColumn(i)
    }

    // archivo en caché para evitar problemas de permisos persistentes
    val file = File(context.cacheDir, "inventario_${System.currentTimeMillis()}.xlsx")

    // guardar
    try {
        val outputStream = FileOutputStream(file)
        workbook.write(outputStream)
        outputStream.flush()
        outputStream.close()
        workbook.close()

        // Abrir el Excel generado
        val uri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            file
        )
        val intent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(uri, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(Intent.createChooser(intent, "Abrir con:"))

    } catch (e: Exception) {
        e.printStackTrace()
        Toast.makeText(context, "Error al generar Excel: ${e.message}", Toast.LENGTH_LONG).show()
    }
}
