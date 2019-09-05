package com.dongtronic.keeptalking

import org.apache.pdfbox.pdmodel.PDDocument

import java.io.*

object PdfSplitter {

    @Throws(IOException::class)
    fun splitFile(version: ManualVersion, pageNumbers: List<Int>): File {
        val fileName = version.filename
        val pageNumberString = StringBuilder()

        val fis = if (version !== ManualVersion.MANUAL) {
            PdfSplitter::class.java.classLoader.getResourceAsStream(fileName)
        } else {
            FileInputStream(File(fileName))
        }

        fis.use { fileStream ->
            var newFileName = ""
            PDDocument.load(fileStream).use { document ->

                (document.numberOfPages - 1 downTo 0).forEach{ page ->
                    if (!pageNumbers.contains(page + 1)) {
                        document.removePage(page)
                    }
                }

                pageNumberString.append("Manual ").append(version.manualName).append(" pages ")
                pageNumbers.forEach { page -> pageNumberString.append(page).append("-") }
                newFileName = pageNumberString.reverse().deleteCharAt(0).reverse().append(".pdf").toString()
                newFileName = newFileName.replace(" ", "_")
                document.save(newFileName)
            }
            return File(newFileName)
        }
    }
}
