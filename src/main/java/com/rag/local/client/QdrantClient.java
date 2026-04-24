package com.rag.local.client;

import com.rag.local.model.QdrantPoint;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Cliente encargado de comunicarse con Qdrant.
 * Permite almacenar vectores en una colección.
 */
@Component
public class QdrantClient {

    @Value("${qdrant.base-url}")
    private String baseUrl;

    @Value("${qdrant.collection}")
    private String collection;

    private final RestTemplate restTemplate = new RestTemplate();

    public void upsertPoints(List<QdrantPoint> points) {

        String url = baseUrl + "/collections/" + collection + "/points";

        // Construimos el body según API de Qdrant
        Map<String, Object> request = new HashMap<>();
        request.put("points", points);

        restTemplate.put(url, request);
    }
}