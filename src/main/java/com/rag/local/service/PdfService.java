package com.rag.local.service;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;

import java.io.File;

/**
 * Servicio encargado de:
 * - extraer texto de PDFs
 * - decidir si usar OCR o no
 */
@Service
public class PdfService {

    private final OcrService ocrService;

    public PdfService(OcrService ocrService) {
        this.ocrService = ocrService;
    }

    public String extractText(String filePath) {

        String text = extractWithPdfBox(filePath);

        // decisión inteligente
        if (!isTextValid(text)) {
            System.out.println("⚠️ Texto pobre detectado → usando OCR...");
            text = ocrService.extractTextFromPdf(filePath);
        }

        // 🔴 PROTECCIÓN CLAVE (evita null SIEMPRE)
        if (text == null) {
            return "";
        }

        return cleanText(text);
    }

    private String extractWithPdfBox(String filePath) {
        try (PDDocument document = PDDocument.load(new File(filePath))) {

            PDFTextStripper stripper = new PDFTextStripper();
            return stripper.getText(document);

        } catch (Exception e) {
            // 🔴 importante: no romper toda la app por un PDF
            System.out.println("⚠️ Error leyendo PDF con PDFBox, fallback a OCR");
            return null;
        }
    }

    /**
     * Detecta si el texto extraído es usable o basura.
     */
    private boolean isTextValid(String text) {

        if (text == null || text.trim().isEmpty() || text.length() < 300) {
            return false;
        }

        long letters = text.chars().filter(Character::isLetter).count();
        double ratio = (double) letters / text.length();

        return ratio > 0.6;
    }

    /**
     * Limpieza final del texto.
     */
    private String cleanText(String text) {

        if (text == null) {
            return "";
        }

        return text
                .replaceAll("-\\n", "")
                .replaceAll("\\n", "\n")
                .replaceAll("\\s+", " ")
                .trim();
    }
}