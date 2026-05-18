package com.example.inventario.ui.inventario

import android.content.Context
import android.net.Uri
import com.tom_roush.pdfbox.pdmodel.PDDocument
import com.tom_roush.pdfbox.text.PDFTextStripper

fun leerPDF(

    context: Context,

    uri: Uri

): String {

    return try {

        val inputStream =

            context.contentResolver
                .openInputStream(uri)

        val document =
            PDDocument.load(inputStream)

        val texto =

            PDFTextStripper()
                .getText(document)

        document.close()

        inputStream?.close()

        texto

    } catch (e: Exception) {

        ""
    }
}