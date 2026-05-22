package com.example.inventario.ui.Facturas

import android.content.Context
import android.widget.Toast

import com.example.inventario.data.Factura

import org.apache.poi.xssf.usermodel.XSSFWorkbook

import java.io.File
import java.io.FileOutputStream
import java.io.IOException

fun exportarFacturasExcel(

    context: Context,

    facturas: List<Factura>,

    periodo: String

) {

    val workbook = XSSFWorkbook()

    val sheet =
        workbook.createSheet("Facturas")

    // encabezados

    val headers = listOf(

        "Fecha",

        "Nº Factura",

        "Proveedor",

        "Productos",

        "Total",

        "Usuario",

        "Notas"
    )

    // titulo

    val titleRow =
        sheet.createRow(0)

    titleRow
        .createCell(0)
        .setCellValue(

            "Reporte de Facturas - Periodo: $periodo"
        )

    // encabezados tabla

    val headerRow =
        sheet.createRow(2)

    headers.forEachIndexed { index, titulo ->

        headerRow
            .createCell(index)
            .setCellValue(titulo)
    }

    // datos

    facturas.forEachIndexed { index, factura ->

        val row =
            sheet.createRow(index + 3)

        row.createCell(0)
            .setCellValue(factura.fecha)

        row.createCell(1)
            .setCellValue(factura.numeroFactura)

        row.createCell(2)
            .setCellValue(factura.proveedor)

        row.createCell(3)
            .setCellValue(factura.productos)

        row.createCell(4)
            .setCellValue(factura.total)

        row.createCell(5)
            .setCellValue(factura.usuario)

        row.createCell(6)
            .setCellValue(factura.notas)
    }

    // ajustar columnas

    for (i in headers.indices) {

        sheet.autoSizeColumn(i)
    }

    val file = File(

        context.cacheDir,

        "facturas_${System.currentTimeMillis()}.xlsx"
    )

    try {

        val fos =
            FileOutputStream(file)

        workbook.write(fos)

        fos.flush()

        fos.close()

        workbook.close()

        Toast.makeText(

            context,

            "Excel generado correctamente",

            Toast.LENGTH_LONG

        ).show()

    } catch (e: IOException) {

        Toast.makeText(

            context,

            "Error de archivo Excel",

            Toast.LENGTH_LONG

        ).show()

        e.printStackTrace()

    } catch (e: Exception) {

        Toast.makeText(

            context,

            "Error: ${e.message}",

            Toast.LENGTH_LONG

        ).show()

        e.printStackTrace()
    }
}