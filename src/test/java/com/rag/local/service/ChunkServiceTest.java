package com.rag.local.service;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias para ChunkService.
 * Verifica que el texto se divida correctamente en partes.
 */
class ChunkServiceTest {

    private final ChunkService chunkService = new ChunkService();

    @Test
    void deberiaDividirTextoEnChunks() {
        // Texto de prueba
        String texto = "a".repeat(1000);

        // Ejecutamos el método
        List<String> chunks = chunkService.splitText(texto, 200);

        // Validamos que no esté vacío
        assertFalse(chunks.isEmpty());

        // Validamos cantidad esperada aproximada
        assertTrue(chunks.size() >= 5);
    }

    @Test
    void ningunChunkDebeSuperarElTamanoMaximo() {
        String texto = "a".repeat(500);

        List<String> chunks = chunkService.splitText(texto, 100);

        // Validamos que ningún chunk sea más grande de lo permitido
        for (String chunk : chunks) {
            assertTrue(chunk.length() <= 100);
        }
    }
}