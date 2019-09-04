package com.dongtronic.keeptalking;

import org.apache.pdfbox.pdmodel.PDDocument;

import java.io.*;
import java.util.List;

public class PdfSplitter {

    public static File splitFile(ManualVersion version, List<Integer> pageNumbers) throws IOException {
        String fileName = version.getFilename();
        InputStream in;

        if (version != ManualVersion.MANUAL) {
            in = PdfSplitter.class.getClassLoader().getResourceAsStream(fileName);
        } else {
            File file = new File(fileName);
            in = new FileInputStream(file);
        }

        PDDocument document = PDDocument.load(in);

        StringBuilder pageNumberString = new StringBuilder();

        for (int i = document.getNumberOfPages() - 1; i >= 0; i--) {
            if (!pageNumbers.contains(i + 1)) {
                document.removePage(i);
            }
        }

        pageNumberString.append("Manual ").append(version.getManualName()).append(" pages ");
        pageNumbers.forEach(page -> pageNumberString.append(page).append("-"));
        String newFileName = pageNumberString.reverse().deleteCharAt(0).reverse().append(".pdf").toString();
        newFileName = newFileName.replace(" ", "_");
        document.save(newFileName);
        document.close();

        return new File(newFileName);
    }
}
