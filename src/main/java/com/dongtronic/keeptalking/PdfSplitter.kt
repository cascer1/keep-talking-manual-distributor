package com.dongtronic.keeptalking

import org.apache.pdfbox.pdmodel.PDDocument

import java.io.*

object PdfSplitter {

    @Throws(IOException::class)
    fun splitFile(version: ManualVersion, pageNumbers: List<Int>): File {
        val fileName = version.filename
        val inputStream: InputStream?

        inputStream = if (version !== ManualVersion.MANUAL) {
            PdfSplitter::class.java.classLoader.getResourceAsStream(fileName)
        } else {
            val file = File(fileName)
            FileInputStream(file)
        }

        val document = PDDocument.load(inputStream)

        val pageNumberString = StringBuilder()

        for (i in document.numberOfPages - 1 downTo 0) {
            if (!pageNumbers.contains(i + 1)) {
                document.removePage(i)
            }
        }

        pageNumberString.append("Manual ").append(version.manualName).append(" pages ")
        pageNumbers.forEach { page -> pageNumberString.append(page).append("-") }
        var newFileName = pageNumberString.reverse().deleteCharAt(0).reverse().append(".pdf").toString()
        newFileName = newFileName.replace(" ", "_")
        document.save(newFileName)
        document.close()

        return File(newFileName)
    }
}
