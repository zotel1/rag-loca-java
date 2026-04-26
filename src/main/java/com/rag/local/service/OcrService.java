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

        System.out.println("📄 OCR iniciado para: " + filePath);

        try (PDDocument document = PDDocument.load(new File(filePath))) {

            PDFRenderer renderer = new PDFRenderer(document);

            ITesseract tesseract = new Tesseract();
            tesseract.setDatapath(TESSDATA_PATH);
            tesseract.setLanguage("spa");

            // 🔥 CONFIGURACIÓN PRO
            tesseract.setOcrEngineMode(1); // LSTM only (mejor precisión)
            tesseract.setPageSegMode(1);   // auto segmentation

            StringBuilder result = new StringBuilder();

            for (int i = 0; i < document.getNumberOfPages(); i++) {

                try {
                    System.out.println("📄 Procesando página " + (i + 1));

                    BufferedImage image = renderer.renderImageWithDPI(i, 300);

                    // 🔥 OCR
                    String pageText = tesseract.doOCR(image);

                    // 🔥 LIMPIEZA OCR (CLAVE)
                    pageText = cleanOcrText(pageText);

                    result.append(pageText).append("\n\n");

                } catch (Exception e) {
                    System.out.println("⚠️ Error en página " + (i + 1) + ", se omite");
                }
            }

            System.out.println("✅ OCR finalizado");

            return result.toString();

        } catch (Exception e) {
            throw new RuntimeException("Error en OCR", e);
        }
    }

    /**
     * 🔥 Limpieza específica para OCR (MUY importante)
     */
    private String cleanOcrText(String text) {
        return text
                .replaceAll("[^\\x00-\\x7F]", "") // elimina caracteres raros OCR
                .replaceAll("-\\n", "")           // une palabras cortadas
                .replaceAll("\\n", " ")
                .replaceAll("\\s+", " ")
                .trim();
    }
}