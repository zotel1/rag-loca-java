package com.rag.local.service;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TextChunkerService {

    // tamaño máximo seguro para evitar error de contexto en embeddings
    private static final int MAX_CHUNK_SIZE = 800;

    // overlap para mantener continuidad semántica
    private static final int OVERLAP_SIZE = 120;

    // ignorar ruido
    private static final int MIN_PARAGRAPH_LENGTH = 30;

    public List<String> chunkText(String text) {

        List<String> chunks = new ArrayList<>();

        // 1. limpiar texto
        String cleanedText = cleanText(text);

        // 2. dividir por párrafos reales
        String[] paragraphs = cleanedText.split("\\n\\s*\\n");

        StringBuilder currentChunk = new StringBuilder();

        for (String paragraph : paragraphs) {

            paragraph = paragraph.trim();

            // 3. ignorar ruido
            if (paragraph.length() < MIN_PARAGRAPH_LENGTH) continue;

            // 4. si el párrafo es demasiado grande → dividirlo
            List<String> parts = splitLargeParagraph(paragraph);

            for (String part : parts) {

                // 5. si se pasa del tamaño → cerrar chunk actual
                if (currentChunk.length() + part.length() > MAX_CHUNK_SIZE) {

                    chunks.add(currentChunk.toString().trim());

                    // aplicar overlap
                    String overlap = getOverlap(currentChunk.toString());
                    currentChunk = new StringBuilder(overlap);
                }

                currentChunk.append(part).append("\n\n");
            }
        }

        // 6. último chunk
        if (!currentChunk.isEmpty()) {
            chunks.add(currentChunk.toString().trim());
        }

        return chunks;
    }

    // divide párrafos grandes sin romper todo el sistema
    private List<String> splitLargeParagraph(String paragraph) {

        List<String> parts = new ArrayList<>();

        if (paragraph.length() <= MAX_CHUNK_SIZE) {
            parts.add(paragraph);
            return parts;
        }

        int start = 0;

        while (start < paragraph.length()) {

            int end = Math.min(start + MAX_CHUNK_SIZE, paragraph.length());

            if (end < paragraph.length()) {
                int lastSpace = paragraph.lastIndexOf(" ", end);
                if (lastSpace > start) {
                    end = lastSpace;
                }
            }

            parts.add(paragraph.substring(start, end).trim());

            start = end;
        }

        return parts;
    }

    private String cleanText(String text) {
        return text
                .replaceAll("\\r", "")
                .replaceAll("[\\t]+", " ")
                .replaceAll(" +", " ")
                .replaceAll("Concepto Definición Unidad", "")
                .trim();
    }

    private String getOverlap(String text) {
        if (text.length() <= OVERLAP_SIZE) return text;
        return text.substring(text.length() - OVERLAP_SIZE);
    }
}