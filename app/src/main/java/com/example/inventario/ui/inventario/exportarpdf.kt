package com.example.inventario.ui.inventario
import android.content.ContentValues
import android.content.Context
import android.content.Intent

import android.graphics.Paint
import android.graphics.pdf.PdfDocument

import android.net.Uri

import android.os.Environment

import android.provider.MediaStore

import android.widget.Toast

import androidx.core.content.FileProvider

import com.example.inventario.data.producto

import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun exportarInventarioPDF(

    context: Context,

    productos: List<producto>,

    bodega: String

) {

    val pdfDocument = PdfDocument()

    val pageInfo = PdfDocument.PageInfo
        .Builder(
            1400,
            2000,
            1
        )
        .create()

    val page =
        pdfDocument.startPage(pageInfo)

    val canvas =
        page.canvas

    val tituloPaint = Paint().apply {

        textSize = 30f

        isFakeBoldText = true
    }

    val textoPaint = Paint().apply {

        textSize = 16f
    }

    val lineaPaint = Paint().apply {

        strokeWidth = 1f
    }

    // titulo

    canvas.drawText(

        "Reporte General de Inventario",

        50f,

        60f,

        tituloPaint
    )

    // fecha

    val fechaActual =

        SimpleDateFormat(

            "dd/MM/yyyy HH:mm",

            Locale.getDefault()

        ).format(Date())

    canvas.drawText(

        "Bodega: $bodega",

        50f,

        100f,

        textoPaint
    )

    canvas.drawText(

        "Fecha: $fechaActual",

        1000f,

        100f,

        textoPaint
    )

    // encabezados

    var y = 160f

    canvas.drawText(
        "Código",
        40f,
        y,
        textoPaint
    )

    canvas.drawText(
        "Descripción",
        150f,
        y,
        textoPaint
    )

    canvas.drawText(
        "Categoría",
        330f,
        y,
        textoPaint
    )

    canvas.drawText(
        "Cantidad",
        470f,
        y,
        textoPaint
    )

    canvas.drawText(
        "Unidad",
        570f,
        y,
        textoPaint
    )

    canvas.drawText(
        "Ubicación",
        660f,
        y,
        textoPaint
    )

    canvas.drawText(
        "Proveedor",
        800f,
        y,
        textoPaint
    )

    canvas.drawText(
        "Costo",
        980f,
        y,
        textoPaint
    )

    canvas.drawText(
        "Fecha",
        1080f,
        y,
        textoPaint
    )

    canvas.drawText(
        "Notas",
        1200f,
        y,
        textoPaint
    )

    y += 10f

    canvas.drawLine(

        40f,

        y,

        1360f,

        y,

        lineaPaint
    )

    y += 35f

    // productos

    productos.forEach { producto ->

        canvas.drawText(

            producto.codigo,

            40f,

            y,

            textoPaint
        )

        canvas.drawText(

            producto.descripcion
                .take(15),

            150f,

            y,

            textoPaint
        )

        canvas.drawText(

            producto.categoria
                .take(12),

            330f,

            y,

            textoPaint
        )

        canvas.drawText(

            producto.cantidad.toString(),

            470f,

            y,

            textoPaint
        )

        canvas.drawText(

            producto.unidad
                .take(8),

            570f,

            y,

            textoPaint
        )

        canvas.drawText(

            producto.ubicacion
                .take(10),

            660f,

            y,

            textoPaint
        )

        canvas.drawText(

            producto.proveedor
                .take(10),

            800f,

            y,

            textoPaint
        )

        canvas.drawText(

            "Q ${producto.costo}",

            980f,

            y,

            textoPaint
        )

        canvas.drawText(

            producto.fechaIngreso
                .take(10),

            1080f,

            y,

            textoPaint
        )

        canvas.drawText(

            producto.notas
                .take(10),

            1200f,

            y,

            textoPaint
        )

        y += 30f
    }

    pdfDocument.finishPage(page)

    // archivo temporal

    val file = File(

        context.cacheDir,

        "inventario_${System.currentTimeMillis()}.pdf"
    )

    try {

        val outputStream =

            FileOutputStream(file)

        pdfDocument.writeTo(outputStream)

        outputStream.flush()

        outputStream.close()

        pdfDocument.close()

        // abrir pdf

        val uri: Uri =

            FileProvider.getUriForFile(

                context,

                "${context.packageName}.fileprovider",

                file
            )

        val intent = Intent(

            Intent.ACTION_VIEW

        ).apply {

            setDataAndType(

                uri,

                "application/pdf"
            )

            addFlags(
                Intent.FLAG_GRANT_READ_URI_PERMISSION
            )

            addFlags(
                Intent.FLAG_ACTIVITY_NEW_TASK
            )
        }

        context.startActivity(

            Intent.createChooser(

                intent,

                "Abrir PDF"
            )
        )

    } catch (e: Exception) {

        e.printStackTrace()

        Toast.makeText(

            context,

            "Error al generar PDF",

            Toast.LENGTH_LONG

        ).show()
    }
}