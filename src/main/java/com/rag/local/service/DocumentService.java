package com.rag.local.service;

import com.rag.local.client.OllamaClient;
import com.rag.local.client.QdrantClient;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DocumentService {

    private final PdfService pdfService;
    private final ChunkService chunkService;
    private final VectorStoreService vectorStoreService;
    private final QdrantClient qdrantClient;

    public DocumentService(PdfService pdfService, ChunkService chunkService, VectorStoreService vectorStoreService, QdrantClient qdrantClient) {
        this.pdfService = pdfService;
        this.chunkService = chunkService;
        this.vectorStoreService = vectorStoreService;
        this.qdrantClient = qdrantClient;
    }

    public void indexPdf(String filePath) {

        qdrantClient.createCollectionIfNotExists();

        String text = pdfService.extractText(filePath);
        if (filePath == null || filePath.isBlank()) {
            throw new RuntimeException("Path invalido");
        }
        List<String> chunks = chunkService.splitText(text, 500);

        vectorStoreService.storeChunks(chunks);

        System.out.println("Documento indexado correctamente");
    }
}
