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
        System.out.println("👉 Pregunta: " + question);

        // 1. Buscar contexto relevante
        List<String> contextChunks = searchService.search(question);

        System.out.println("👉 Context chunks encontrados: " + contextChunks.size());

        if (contextChunks.isEmpty()) {
            System.out.println("❌ No hay contexto, se corta el flujo");
            return "No encontré información relevante en los documentos.";
        }

        // 2. Construir prompt
        String prompt = buildPrompt(question, contextChunks);

        System.out.println("👉 PROMPT ENVIADO A OLLAMA:\n" + prompt);

        // 3. Llamar al modelo
        String response = ollamaClient.chat(prompt);

        System.out.println("👉 RESPUESTA FINAL:\n" + response);
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