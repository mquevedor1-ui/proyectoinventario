package com.example.inventario.ui.inventario

import android.content.Context
import android.net.Uri
import com.example.inventario.data.producto
import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.ss.usermodel.CellType
import org.apache.poi.xssf.usermodel.XSSFWorkbook

/**
 * Lee una celda de Excel de forma segura, convirtiéndola a String sin importar su tipo.
 */
fun getCellValueAsString(cell: Cell?): String {
    if (cell == null) return ""
    return when (cell.cellType) {
        CellType.STRING -> cell.stringCellValue
        CellType.NUMERIC -> cell.numericCellValue.toLong().toString()
        CellType.BOOLEAN -> cell.booleanCellValue.toString()
        else -> ""
    }
}

fun importarExcel(
    context: Context,
    uri: Uri,
    bodegaId: String
): List<producto> {
    val productos = mutableListOf<producto>()
    
    try {
        val inputStream = context.contentResolver.openInputStream(uri)
        val workbook = XSSFWorkbook(inputStream)
        val sheet = workbook.getSheetAt(0)

        // Empezamos en 1 para saltar el encabezado
        for (i in 1..sheet.lastRowNum) {
            val row = sheet.getRow(i) ?: continue
            try {
                val codigo = getCellValueAsString(row.getCell(0))
                val descripcion = getCellValueAsString(row.getCell(1))
                val categoriaNom = getCellValueAsString(row.getCell(2))
                val cantidad = row.getCell(3)?.numericCellValue?.toInt() ?: 0

                if (codigo.isNotEmpty() && descripcion.isNotEmpty()) {
                    val nuevoProducto = producto(
                        bodegaId = bodegaId,
                        codigo = codigo,
                        descripcion = descripcion,
                        categoria = categoriaNom,
                        prefijoCategoria = if (categoriaNom.isNotEmpty()) categoriaNom.take(1).uppercase() else "P",
                        cantidad = cantidad,
                        unidad = "Unidad",
                        ubicacion = getCellValueAsString(row.getCell(5)),
                        proveedor = getCellValueAsString(row.getCell(6)),
                        costo = row.getCell(7)?.numericCellValue ?: 0.0,
                        stockMinimo = row.getCell(8)?.numericCellValue?.toInt() ?: 0,
                        fechaIngreso = getCellValueAsString(row.getCell(9)),
                        notas = getCellValueAsString(row.getCell(10))
                    )
                    productos.add(nuevoProducto)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        workbook.close()
        inputStream?.close()
    } catch (e: Exception) {
        e.printStackTrace()
    }

    return productos
}
