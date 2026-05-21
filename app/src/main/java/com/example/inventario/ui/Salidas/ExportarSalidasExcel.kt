package com.example.inventario.ui.Salidas

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.core.content.FileProvider
import com.example.inventario.data.Salida
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.File
import java.io.FileOutputStream

fun exportarSalidasExcel(context: Context, salidas: List<Salida>, periodo: String) {
    val workbook = XSSFWorkbook()
    val sheet = workbook.createSheet("Salidas")

    // Encabezados
    val headers = listOf(
        "Fecha",
        "Código",
        "Descripción",
        "Cantidad",
        "Destino",
        "Responsable",
        "Vehículo",
        "Notas"
    )

    // Título y Periodo
    val titleRow = sheet.createRow(0)
    titleRow.createCell(0).setCellValue("Reporte de Salidas - Periodo: $periodo")

    val headerRow = sheet.createRow(2)
    headers.forEachIndexed { index, titulo ->
        headerRow.createCell(index).setCellValue(titulo)
    }

    // Datos
    salidas.forEachIndexed { index, salida ->
        val row = sheet.createRow(index + 3)
        row.createCell(0).setCellValue(salida.fecha)
        row.createCell(1).setCellValue(salida.codigo)
        row.createCell(2).setCellValue(salida.descripcion)
        row.createCell(3).setCellValue(salida.cantidad.toDouble())
        row.createCell(4).setCellValue(salida.destino)
        row.createCell(5).setCellValue(salida.responsable)
        row.createCell(6).setCellValue(salida.vehiculo)
        row.createCell(7).setCellValue(salida.notas)
    }

    // Ajustar columnas
    for (i in headers.indices) {
        sheet.autoSizeColumn(i)
    }

    val file = File(context.cacheDir, "salidas_${System.currentTimeMillis()}.xlsx")

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
