package com.rag.local.client;

import com.rag.local.model.EmbeddingResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * Cliente encargado de comunicarse con Ollama.
 * Se utiliza para generar embeddings de texto.
 */
@Component
public class OllamaClient {

    @Value("${ollama.base-url}")
    private String baseUrl;

    @Value("${ollama.embedding-model}")
    private String embeddingModel;

    private final RestTemplate restTemplate = new RestTemplate();

    public EmbeddingResponse generateEmbedding(String text) {

        String url = baseUrl + "/api/embeddings";

        // Cuerpo de la petición hacia Ollama
        Map<String, Object> request = new HashMap<>();
        request.put("model", embeddingModel);
        request.put("prompt", text);

        // Llamada HTTP POST
        return restTemplate.postForObject(url, request, EmbeddingResponse.class);
    }
}