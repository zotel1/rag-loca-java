package com.rag.local.service;

import com.rag.local.client.QdrantClient;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class SearchService {

    private final EmbeddingService embeddingService;
    private final QdrantClient qdrantClient;

    public SearchService(EmbeddingService embeddingService,
                         QdrantClient qdrantClient) {
        this.embeddingService = embeddingService;
        this.qdrantClient = qdrantClient;
    }

    public List<String> search(String query) {

        System.out.println("\n🔎 SEARCH DEBUG");
        System.out.println("🔎 Query: " + query);

        // 1. embedding de la pregunta
        List<Double> queryEmbedding = embeddingService.generateEmbedding(query);

        System.out.println("🔎 Embedding generado, tamaño: " + queryEmbedding.size());

        // 2. buscar en Qdrant
        List<Map<String, Object>> results = qdrantClient.search(queryEmbedding, 3);

        System.out.println("🔎 Resultados crudos de Qdrant: " + results);

        // 3. extraer texto del payload
        List<String> texts = new ArrayList<>();

        for (Map<String, Object> result : results) {
            Map<String, Object> payload = (Map<String, Object>) result.get("payload");

            if (payload != null && payload.get("text") != null) {
                texts.add((String) payload.get("text"));
            }
        }

        System.out.println("🔎 Textos extraídos: " + texts.size());

        return texts;
    }
}