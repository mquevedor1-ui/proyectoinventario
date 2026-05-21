package com.example.inventario.ui.Facturas

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.core.content.FileProvider
import com.example.inventario.data.Factura
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.File
import java.io.FileOutputStream

fun exportarFacturasExcel(context: Context, facturas: List<Factura>, periodo: String) {
    val workbook = XSSFWorkbook()
    val sheet = workbook.createSheet("Facturas")

    // Encabezados
    val headers = listOf(
        "Fecha",
        "Nº Factura",
        "Proveedor",
        "Productos",
        "Total",
        "Usuario",
        "Notas"
    )

    // Título y Periodo
    val titleRow = sheet.createRow(0)
    titleRow.createCell(0).setCellValue("Reporte de Facturas - Periodo: $periodo")

    val headerRow = sheet.createRow(2)
    headers.forEachIndexed { index, titulo ->
        headerRow.createCell(index).setCellValue(titulo)
    }

    // Datos
    facturas.forEachIndexed { index, factura ->
        val row = sheet.createRow(index + 3)
        row.createCell(0).setCellValue(factura.fecha)
        row.createCell(1).setCellValue(factura.numeroFactura)
        row.createCell(2).setCellValue(factura.proveedor)
        row.createCell(3).setCellValue(factura.productos)
        row.createCell(4).setCellValue(factura.total)
        row.createCell(5).setCellValue(factura.usuario)
        row.createCell(6).setCellValue(factura.notas)
    }

    // Ajustar columnas
    for (i in headers.indices) {
        sheet.autoSizeColumn(i)
    }

    val file = File(context.cacheDir, "facturas_${System.currentTimeMillis()}.xlsx")

    try {
        val fos = FileOutputStream(file)
        workbook.write(fos)
        fos.close()
        workbook.close()

        val uri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
        val intent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(uri, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(Intent.createChooser(intent, "Abrir Excel con:"))
    } catch (e: Exception) {
        Toast.makeText(context, "Error al generar Excel: ${e.message}", Toast.LENGTH_LONG).show()
    }
}
