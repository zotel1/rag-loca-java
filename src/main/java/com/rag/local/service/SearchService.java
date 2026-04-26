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

        // 2. buscar en Qdrant (aumentamos contexto)
        int topK = 6;
        List<Map<String, Object>> results = qdrantClient.search(queryEmbedding, topK);

        System.out.println("🔎 Resultados crudos de Qdrant: " + results.size());

        // 3. extraer texto del payload con filtro de calidad
        List<String> texts = new ArrayList<>();

        for (Map<String, Object> result : results) {

            Double score = (Double) result.get("score");

            if (score != null && score < 0.65) {
                continue; // filtro básico de relevancia
            }

            Map<String, Object> payload = (Map<String, Object>) result.get("payload");

            if (payload != null && payload.get("text") != null) {

                String text = (String) payload.get("text");

                if (text.length() > 50) { // evita basura
                    texts.add(text);
                }
            }
        }

        System.out.println("🔎 Textos filtrados finales: " + texts.size());

        return texts;
    }
}