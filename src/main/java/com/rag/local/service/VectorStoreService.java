package com.rag.local.service;

import com.rag.local.client.QdrantClient;
import com.rag.local.model.QdrantPoint;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Servicio encargado de:
 * - generar embeddings
 * - construir puntos
 * - enviarlos a Qdrant
 */
@Service
public class VectorStoreService {

    private final EmbeddingService embeddingService;
    private final HashService hashService;
    private final QdrantClient qdrantClient;

    public VectorStoreService(
            EmbeddingService embeddingService,
            HashService hashService,
            QdrantClient qdrantClient
    ) {
        this.embeddingService = embeddingService;
        this.hashService = hashService;
        this.qdrantClient = qdrantClient;
    }

    public void storeChunks(List<String> chunks) {

        for (String chunk : chunks) {

            // Generamos hash único para evitar duplicados
            String id = hashService.generateUUIDFromText(chunk);

            // Generamos embedding
            List<Double> embedding = embeddingService.generateEmbedding(chunk);

            // Payload con el texto original
            Map<String, Object> payload = new HashMap<>();
            payload.put("hash", hashService.generateHash(chunk));

            QdrantPoint point = new QdrantPoint(id, embedding, payload);

            // Guardamos en Qdrant
            qdrantClient.upsertPoints(List.of(point));
        }
    }
}