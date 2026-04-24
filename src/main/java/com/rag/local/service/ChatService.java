package com.rag.local.service;

import com.rag.local.client.OllamaClient;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatService {

    private final SearchService searchService;
    private final OllamaClient ollamaClient;

    public ChatService(SearchService searchService,
                       OllamaClient ollamaClient) {
        this.searchService = searchService;
        this.ollamaClient = ollamaClient;
    }

    public String ask(String question) {

        System.out.println("\n================ CHAT DEBUG ================");
        System.out.println("👉 Question: " + question);

        if (question == null || question.isBlank()) {
            return "La pregunta no puede estar vacía";
        }

        List<String> contextChunks = searchService.search(question);

        System.out.println("👉 Context chunks: " + contextChunks.size());

        if (contextChunks.isEmpty()) {
            return "No encontré información relevante en los documentos.";
        }

        String prompt = buildPrompt(question, contextChunks);

        String response = ollamaClient.chat(prompt);

        // limpiar ruido del modelo
        if (response != null) {
            response = response.replace("Respuesta:", "").trim();
        }

        System.out.println("👉 Final response:\n" + response);
        System.out.println("===========================================\n");

        return response;
    }

    private String buildPrompt(String question, List<String> contextChunks) {

        StringBuilder sb = new StringBuilder();

        sb.append("Sos un asistente que responde preguntas usando SOLO el contexto dado.\n");
        sb.append("Si la respuesta no está en el contexto, decí que no lo sabés.\n\n");

        sb.append("CONTEXTO:\n");
        for (String chunk : contextChunks) {
            sb.append(chunk).append("\n---\n");
        }

        sb.append("\nPREGUNTA:\n");
        sb.append(question).append("\n\n");

        sb.append("RESPUESTA:");

        return sb.toString();
    }
}