package com.ulskjourney.ulskjourney.utils

import android.os.Environment
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfName.Table
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.layout.Document
import com.itextpdf.layout.element.Paragraph
import com.itextpdf.layout.element.Table
import com.ulskjourney.ulskjourney.model.models.Mark
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


class Report {
    @Throws(IOException::class)
    fun generatePdf(marks: List<Mark>) {
        val pdfPath: String = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString()
        val file = File(pdfPath, "report.pdf")
        val pdfWriter = PdfWriter(FileOutputStream(file))
        val pdfDocument = PdfDocument(pdfWriter)
        val document = Document(pdfDocument)
        val paragraph = Paragraph("Your marks ")
        document.add(paragraph)
        val columnWidth = floatArrayOf(200f, 200f, 200f)
        val table = Table(columnWidth)
        for (mark in marks) {
            table.addCell("Mark:" + mark.name)
            table.addCell("longitude" + mark.longitude)
            table.addCell("latitude" + mark.latitude)
        }
        document.add(table)
        document.close()
    }
}