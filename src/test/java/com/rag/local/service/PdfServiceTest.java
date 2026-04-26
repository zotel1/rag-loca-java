package com.rag.local.service;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.net.URL;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class PdfServiceTest {

    private String getResourcePath(String fileName) {

        ClassLoader classLoader = getClass().getClassLoader();
        URL resource = classLoader.getResource(fileName);

        if (resource == null) {
            throw new RuntimeException("Archivo no encontrado en classpath: " + fileName);
        }

        try {
            return new File(resource.toURI()).getAbsolutePath();
        } catch (Exception e) {
            throw new RuntimeException("Error convirtiendo path: " + fileName, e);
        }
    }

    @Test
    void shouldExtractTextFromDigitalPdf() {

        OcrService ocrMock = mock(OcrService.class);

        when(ocrMock.extractTextFromPdf(anyString()))
                .thenReturn("Texto OCR fallback por si acaso...");

        PdfService pdfService = new PdfService(ocrMock);

        String path = getResourcePath("digital.pdf");

        String text = pdfService.extractText(path);

        assertNotNull(text);
        assertFalse(text.trim().isEmpty());
    }

    @Test
    void shouldFallbackToOcrWhenTextIsPoor() {

        OcrService ocrMock = mock(OcrService.class);

        when(ocrMock.extractTextFromPdf(anyString()))
                .thenReturn("Texto OCR simulado suficientemente largo para testear comportamiento...");

        PdfService pdfService = new PdfService(ocrMock);

        String path = getResourcePath("imagen-ocr.pdf");

        String text = pdfService.extractText(path);

        // ✔ Validaciones
        assertNotNull(text);
        assertFalse(text.trim().isEmpty());

        // ✔ Se debe usar OCR
        verify(ocrMock, times(1)).extractTextFromPdf(anyString());
    }
}