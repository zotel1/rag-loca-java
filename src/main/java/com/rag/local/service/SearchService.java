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

        // 2. traer más candidatos
        int topK = 10;
        List<Map<String, Object>> results = qdrantClient.search(queryEmbedding, topK);

        System.out.println("🔎 Resultados crudos de Qdrant: " + results.size());

        // 3. ordenar por score (mayor = mejor)
        results.sort((a, b) -> {
            Double scoreA = (Double) a.get("score");
            Double scoreB = (Double) b.get("score");

            if (scoreA == null) return 1;
            if (scoreB == null) return -1;

            return Double.compare(scoreB, scoreA);
        });

        // 4. filtrar + limitar top reales
        List<String> texts = new ArrayList<>();

        int maxResults = 4;

        for (Map<String, Object> result : results) {

            if (texts.size() >= maxResults) break;

            Double score = (Double) result.get("score");

            if (score != null && score < 0.80) {
                continue;
            }

            Map<String, Object> payload = (Map<String, Object>) result.get("payload");

            if (payload != null && payload.get("text") != null) {

                String text = (String) payload.get("text");

                if (text.length() > 50) {
                    texts.add(text);
                }
            }
        }

        System.out.println("🔎 Textos finales (top rankeados): " + texts.size());

        return texts;
    }
}