package com.rag.local.service;


import com.rag.local.client.OllamaClient;
import com.rag.local.model.EmbeddingResponse;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Servicio que encapsula la lógica de generación de embeddings.
 * Se apoya en OllamaClient para comunicarse con el modelo.
 */
@Service
public class EmbeddingService {

    private final OllamaClient ollamaClient;

    public EmbeddingService(OllamaClient ollamaClient) {
        this.ollamaClient = ollamaClient;
    }

    public List<Double> generateEmbedding(String text) {

        EmbeddingResponse response = ollamaClient.generateEmbedding(text);

        if (response == null || response.getEmbedding() == null) {
            throw new RuntimeException("Error generando embedding: respuesta null");
        }

        return response.getEmbedding();
    }
}