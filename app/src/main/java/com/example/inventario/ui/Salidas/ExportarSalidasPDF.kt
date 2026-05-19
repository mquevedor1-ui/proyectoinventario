package com.example.inventario.ui.Salidas

import android.content.Context
import android.content.Intent

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.pdf.PdfDocument

import android.widget.Toast

import androidx.core.content.FileProvider

import com.example.inventario.data.Salida

import java.io.File
import java.io.FileOutputStream

fun exportarSalidasPDF(

    context: Context,

    salidas: List<Salida>

) {

    val file = File(

        context.cacheDir,

        "salidas_${System.currentTimeMillis()}.pdf"
    )

    val pdfDocument = PdfDocument()

    val paint = Paint()

    val titlePaint = Paint()

    // pagina

    val pageInfo = PdfDocument.PageInfo
        .Builder(
            595,
            842,
            1
        )
        .create()

    val page =
        pdfDocument.startPage(pageInfo)

    val canvas: Canvas =
        page.canvas

    // titulo

    titlePaint.textSize = 18f

    titlePaint.isFakeBoldText = true

    canvas.drawText(

        "Reporte de Salidas de Inventario",

        40f,

        50f,

        titlePaint
    )

    paint.textSize = 11f

    var yPosition = 100f

    // encabezados

    paint.isFakeBoldText = true

    canvas.drawText(
        "Fecha",
        20f,
        yPosition,
        paint
    )

    canvas.drawText(
        "Código",
        90f,
        yPosition,
        paint
    )

    canvas.drawText(
        "Descripción",
        160f,
        yPosition,
        paint
    )

    canvas.drawText(
        "Cant.",
        310f,
        yPosition,
        paint
    )

    canvas.drawText(
        "Destino",
        360f,
        yPosition,
        paint
    )

    canvas.drawText(
        "Responsable",
        470f,
        yPosition,
        paint
    )

    yPosition += 20f

    paint.isFakeBoldText = false

    canvas.drawLine(

        20f,

        yPosition - 10f,

        570f,

        yPosition - 10f,

        paint
    )

    // datos

    salidas.forEach { salida ->

        if (

            yPosition > 800

        ) {

            return@forEach
        }

        canvas.drawText(

            salida.fecha.take(10),

            20f,

            yPosition,

            paint
        )

        canvas.drawText(

            salida.codigo,

            90f,

            yPosition,

            paint
        )

        canvas.drawText(

            salida.descripcion.take(18),

            160f,

            yPosition,

            paint
        )

        canvas.drawText(

            salida.cantidad.toString(),

            310f,

            yPosition,

            paint
        )

        canvas.drawText(

            salida.destino.take(12),

            360f,

            yPosition,

            paint
        )

        canvas.drawText(

            salida.responsable.take(12),

            470f,

            yPosition,

            paint
        )

        yPosition += 20f
    }

    pdfDocument.finishPage(page)

    // guardar

    try {

        val fos = FileOutputStream(file)

        pdfDocument.writeTo(fos)

        fos.flush()

        fos.close()

        pdfDocument.close()

        // abrir pdf

        val uri = FileProvider.getUriForFile(

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

                "Abrir Reporte PDF"
            )
        )

    } catch (e: Exception) {

        e.printStackTrace()

        Toast.makeText(

            context,

            "Error al generar PDF: ${e.message}",

            Toast.LENGTH_LONG

        ).show()
    }
}