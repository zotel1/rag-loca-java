package com.rag.local.service;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TextChunkerService {

    public List<String> chunkText(String text) {

        List<String> chunks = new ArrayList<>();

        // Dividir por párrafos (doble salto de línea)
        String[] paragraphs = text.split("\\n\\s*\\n");

        StringBuilder currentChunk = new StringBuilder();

        for (String paragraph : paragraphs) {

            // limpiar texto
            paragraph = paragraph.trim();

            if (paragraph.isEmpty()) continue;

            // Si el chunk actual es muy grande, lo cerramos
            if (currentChunk.length() + paragraph.length() > 500) {
                chunks.add(currentChunk.toString());
                currentChunk = new StringBuilder();
            }

            currentChunk.append(paragraph).append("\n");
        }

        // agregar último chunk
        if (!currentChunk.isEmpty()) {
            chunks.add(currentChunk.toString());
        }

        return chunks;
    }
}