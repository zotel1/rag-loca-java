package com.rag.local.client;

import com.rag.local.model.EmbeddingResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Component
public class OllamaClient {

    @Value("${ollama.base-url}")
    private String baseUrl;

    @Value("${ollama.embedding-model}")
    private String embeddingModel;

    private final RestTemplate restTemplate = new RestTemplate();

    private static final int MAX_INPUT_CHARS = 1000;

    public EmbeddingResponse generateEmbedding(String text) {

        String url = baseUrl + "/api/embeddings";

        // 🔥 recorte inteligente
        if (text.length() > MAX_INPUT_CHARS) {
            text = text.substring(0, MAX_INPUT_CHARS);
        }

        Map<String, Object> request = new HashMap<>();
        request.put("model", embeddingModel);
        request.put("prompt", text);

        System.out.println("🧠 Generando embedding...");
        System.out.println("🧠 Tamaño texto: " + text.length());

        return restTemplate.postForObject(url, request, EmbeddingResponse.class);
    }

    public String chat(String prompt) {

        String url = baseUrl + "/api/generate";

        Map<String, Object> request = new HashMap<>();
        request.put("model", "phi3");
        request.put("prompt", prompt);
        request.put("stream", false);

        System.out.println("\n🚀 OLLAMA DEBUG");
        System.out.println("🚀 URL: " + url);
        System.out.println("🚀 Request: " + request);

        Map response = restTemplate.postForObject(url, request, Map.class);

        System.out.println("🚀 Response cruda: " + response);

        if (response == null) {
            System.out.println("❌ Ollama devolvió null");
            return "Error: respuesta null";
        }

        Object result = response.get("response");

        if (result == null) {
            System.out.println("❌ Campo 'response' es null");
            return "Error: sin respuesta del modelo";
        }

        return result.toString();
    }
}