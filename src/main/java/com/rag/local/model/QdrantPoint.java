package com.rag.local.model;

/*Representa un punto que se guarda en Qdrant.
* Incluye:
* - id unico (usamos hash)
* - vector (embedding)
* - payload (texto original u otros datos)*/

import java.util.List;
import java.util.Map;

public class QdrantPoint {

    private String id;
    private List<Double> vector;
    private Map<String, Object> payload;

    public QdrantPoint(String id, List<Double> vector, Map<String, Object> payload) {
        this.id = id;
        this.vector = vector;
        this.payload = payload;
    }
    public String getId() {
        return id;
    }

    public List<Double> getVector() {
        return vector;
    }

    public Map<String, Object> getPayload() {
        return payload;
    }
}
