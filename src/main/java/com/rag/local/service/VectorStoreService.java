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

            try {
                // filtro básico
                if (chunk == null || chunk.isBlank()) continue;
                if (chunk.length() < 50) continue;
                if (chunk.length() > 1200) {
                    chunk = chunk.substring(0, 1200);
                }; // evita error de contexto

                String id = hashService.generateUUIDFromText(chunk);

                List<Double> embedding = embeddingService.generateEmbedding(chunk);

                if (embedding == null || embedding.isEmpty()) {
                    System.out.println("❌ Embedding vacío, se ignora chunk");
                    continue;
                }

                Map<String, Object> payload = new HashMap<>();
                payload.put("hash", hashService.generateHash(chunk));
                payload.put("text", chunk);

                QdrantPoint point = new QdrantPoint(id, embedding, payload);

                qdrantClient.upsertPoints(List.of(point));

            } catch (Exception e) {
                System.out.println("❌ Error guardando chunk: " + e.getMessage());
            }
        }
    }
}