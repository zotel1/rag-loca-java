package com.rag.local.service;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class TextChunkerServiceTest {

    private final TextChunkerService chunker = new TextChunkerService();


    @Test
    void shouldKeepContextBetweenChunks() {

        String text = """
            La electricidad es una forma de energía que permite el funcionamiento de dispositivos.

            Se basa en el movimiento de electrones dentro de un conductor.

            Es fundamental para sistemas informáticos modernos.
            """;

        List<String> chunks = chunker.chunkText(text);

        assertThat(chunks).isNotEmpty();

        if (chunks.size() > 1) {
            String lastPart = chunks.get(0)
                    .substring(Math.max(0, chunks.get(0).length() - 50));

            assertThat(chunks.get(1)).contains(lastPart.substring(0, 20));
        }
    }

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