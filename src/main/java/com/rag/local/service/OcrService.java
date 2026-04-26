package com.rag.local.service;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;
import java.io.File;

@Service
public class OcrService {

    private static final String TESSDATA_PATH = "C:\\Program Files\\Tesseract-OCR\\tessdata";

    public String extractTextFromPdf(String filePath) {

        try (PDDocument document = PDDocument.load(new File(filePath))) {

            PDFRenderer renderer = new PDFRenderer(document);

            ITesseract tesseract = new Tesseract();

            // 🔥 CLAVE
            tesseract.setDatapath(TESSDATA_PATH);
            tesseract.setLanguage("spa");

            StringBuilder result = new StringBuilder();

            for (int i = 0; i < document.getNumberOfPages(); i++) {

                BufferedImage image = renderer.renderImageWithDPI(i, 300);

                String pageText = tesseract.doOCR(image);

                result.append(pageText).append("\n\n");
            }

            return result.toString();

        } catch (Exception e) {
            throw new RuntimeException("Error en OCR", e);
        }
    }
}