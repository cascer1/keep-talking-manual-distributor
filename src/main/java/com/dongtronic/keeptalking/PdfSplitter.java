package com.dongtronic.keeptalking;

import org.apache.pdfbox.pdmodel.PDDocument;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class PdfSplitter {

    public static File splitFile(ManualVersion version, List<Integer> pageNumbers) throws IOException {
        String fileName = version.getFilename();
        File file;

        if (version != ManualVersion.MANUAL) {
            file = new File(PdfSplitter.class.getClassLoader().getResource(fileName).getFile());
        } else {
            file = new File(fileName);
        }

        PDDocument document = PDDocument.load(file);

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
