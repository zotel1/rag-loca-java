package com.rag.local.service;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class TextChunkerServiceTest {

    private final TextChunkerService chunker = new TextChunkerService();

    @Test
    void shouldSplitTextIntoParagraphChunks() {

        String text = """
                Este es el primer párrafo.

                Este es el segundo párrafo que es un poco más largo
                y tiene múltiples líneas.

                Este es el tercer párrafo.
                """;

        List<String> chunks = chunker.chunkText(text);

        // Debe generar múltiples chunks
        assertThat(chunks).isNotEmpty();

        // Cada chunk no debe ser gigante
        chunks.forEach(chunk ->
                assertThat(chunk.length()).isLessThanOrEqualTo(600)
        );
    }
}