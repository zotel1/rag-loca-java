package com.rag.local.model;


import java.util.List;

/**
 * Representa la respuesta de Ollama al generar embeddings.
 * Contiene el vector numérico que representa el texto.
 */
public class EmbeddingResponse {

    private List<Double> embedding;

    public List<Double> getEmbedding() {
        return embedding;
    }

    public void setEmbedding(List<Double> embedding) {
        this.embedding = embedding;
    }
}