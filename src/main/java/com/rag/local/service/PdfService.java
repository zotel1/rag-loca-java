package com.rag.local.service;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class PdfService {

    public String extractText(String filePath) {

        try (PDDocument document = PDDocument.load(new File(filePath))) {

            PDFTextStripper stripper = new PDFTextStripper();
            String text = stripper.getText(document);

            return text
                    .replaceAll("-\\n", "") // une palabras cortadas
                    .replaceAll("\\n", "\n")
                    .replaceAll("\\s+", " ")
                    .replaceAll("S\\s+CHED", "SCHED")
                    .trim();

        } catch (Exception e) {
            throw new RuntimeException("Error leyendo PDF", e);
        }
    }
}
