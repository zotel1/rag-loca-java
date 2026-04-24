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

        // 1. Buscar contexto relevante
        List<String> contextChunks = searchService.search(question);

        if (contextChunks.isEmpty()) {
            return "No encontré información relevante en los documentos.";
        }

        // 2. Construir prompt
        String prompt = buildPrompt(question, contextChunks);

        // 3. Llamar al modelo
        return ollamaClient.chat(prompt);
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