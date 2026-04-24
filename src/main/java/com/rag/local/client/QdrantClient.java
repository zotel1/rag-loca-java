package com.rag.local.client;

import com.rag.local.model.QdrantPoint;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Cliente encargado de comunicarse con Qdrant.
 * Permite:
 * - Crear colección si no existe
 * - Insertar vectores (points)
 */
@Component
public class QdrantClient {

    @Value("${qdrant.base-url}")
    private String baseUrl;

    @Value("${qdrant.collection}")
    private String collection;

    private final RestTemplate restTemplate = new RestTemplate();

    /**
     * Inserta puntos en Qdrant (upsert = inserta o actualiza).
     */
    public void upsertPoints(List<QdrantPoint> points) {

        String url = baseUrl + "/collections/" + collection + "/points";

        Map<String, Object> request = new HashMap<>();
        request.put("points", points);

        restTemplate.put(url, request);
    }

    /**
     * Verifica si la colección existe.
     * Si no existe, la crea automáticamente.
     */
    public void createCollectionIfNotExists() {

        String url = baseUrl + "/collections/" + collection;

        try {
            restTemplate.getForObject(url, String.class);
            System.out.println("✅ Collection already exists: " + collection);

        } catch (HttpClientErrorException e) {

            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {

                System.out.println("⚠️ Collection not found. Creating: " + collection);

                createCollection();

            } else {
                throw e; // otro error → lo propagamos
            }
        }
    }

    /**
     * Crea la colección en Qdrant.
     */
    private void createCollection() {

        String url = baseUrl + "/collections/" + collection;

        Map<String, Object> vectors = new HashMap<>();
        vectors.put("size", 768); // importante: coincide con nomic-embed-text
        vectors.put("distance", "Cosine");

        Map<String, Object> body = new HashMap<>();
        body.put("vectors", vectors);

        restTemplate.put(url, body);

        System.out.println("✅ Collection created successfully");
    }

    public List<Map<String, Object>> search(List<Double> vector, int limit) {
        String url = baseUrl + "/collections/" + collection + "/points/search";

        Map<String, Object> request = new HashMap<>();
        request.put("vector", vector);
        request.put("limit", limit);
        request.put("with_payload", true);

        Map response = restTemplate.postForObject(url, request, Map.class);

        return (List<Map<String, Object>>) response.get("result");
    }
}