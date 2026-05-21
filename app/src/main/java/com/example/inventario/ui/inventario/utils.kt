package com.example.inventario.ui.inventario

import android.content.Context
import android.net.Uri
import com.example.inventario.data.Entrada
import com.example.inventario.data.Factura
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
        CellType.STRING -> cell.stringCellValue.trim()
        CellType.NUMERIC -> {
            val valNum = cell.numericCellValue
            if (valNum == valNum.toLong().toDouble()) {
                valNum.toLong().toString()
            } else {
                valNum.toString()
            }
        }
        CellType.BOOLEAN -> cell.booleanCellValue.toString()
        CellType.FORMULA -> {
            try {
                cell.numericCellValue.toString()
            } catch (e: Exception) {
                cell.stringCellValue
            }
        }
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

fun importarEntradasExcel(
    context: Context,
    uri: Uri,
    bodegaId: String
): List<Entrada> {
    val entradas = mutableListOf<Entrada>()
    try {
        val inputStream = context.contentResolver.openInputStream(uri)
        val workbook = XSSFWorkbook(inputStream)
        val sheet = workbook.getSheetAt(0)
        for (i in 1..sheet.lastRowNum) {
            val row = sheet.getRow(i) ?: continue
            try {
                val fecha = getCellValueAsString(row.getCell(0))
                val codigo = getCellValueAsString(row.getCell(1))
                val descripcion = getCellValueAsString(row.getCell(2))
                val cantidad = row.getCell(3)?.numericCellValue?.toInt() ?: 0
                val unidad = getCellValueAsString(row.getCell(4))
                val ubicacion = getCellValueAsString(row.getCell(5))
                val proveedor = getCellValueAsString(row.getCell(6))
                val costo = row.getCell(7)?.numericCellValue ?: 0.0
                val stockMin = row.getCell(8)?.numericCellValue?.toInt() ?: 0
                val numFactura = getCellValueAsString(row.getCell(9))
                val notas = getCellValueAsString(row.getCell(10))

                if (codigo.isNotEmpty()) {
                    entradas.add(Entrada(
                        fecha = fecha,
                        codigo = codigo,
                        descripcion = descripcion,
                        cantidad = cantidad,
                        unidad = unidad,
                        ubicacion = ubicacion,
                        proveedor = proveedor,
                        costoUnitario = costo,
                        stockMinimo = stockMin,
                        numeroFactura = numFactura,
                        notas = notas,
                        bodegaId = bodegaId
                    ))
                }
            } catch (e: Exception) { e.printStackTrace() }
        }
        workbook.close()
    } catch (e: Exception) { e.printStackTrace() }
    return entradas
}

fun importarFacturasExcel(
    context: Context,
    uri: Uri,
    bodegaId: String
): List<Factura> {
    val facturas = mutableListOf<Factura>()
    try {
        val inputStream = context.contentResolver.openInputStream(uri)
        val workbook = XSSFWorkbook(inputStream)
        val sheet = workbook.getSheetAt(0)
        for (i in 1..sheet.lastRowNum) {
            val row = sheet.getRow(i) ?: continue
            try {
                val fecha = getCellValueAsString(row.getCell(0))
                val numero = getCellValueAsString(row.getCell(1))
                val proveedor = getCellValueAsString(row.getCell(2))
                val productos = getCellValueAsString(row.getCell(3))
                val total = row.getCell(4)?.numericCellValue ?: 0.0
                val usuario = getCellValueAsString(row.getCell(5))
                val notas = getCellValueAsString(row.getCell(6))

                if (numero.isNotEmpty()) {
                    facturas.add(Factura(
                        fecha = fecha,
                        numeroFactura = numero,
                        proveedor = proveedor,
                        productos = productos,
                        total = total,
                        usuario = usuario,
                        notas = notas,
                        bodegaId = bodegaId
                    ))
                }
            } catch (e: Exception) { e.printStackTrace() }
        }
        workbook.close()
    } catch (e: Exception) { e.printStackTrace() }
    return facturas
}
