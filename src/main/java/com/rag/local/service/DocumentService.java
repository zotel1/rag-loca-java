package com.rag.local.service;

import com.rag.local.client.QdrantClient;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DocumentService {

    private final PdfService pdfService;
    private final VectorStoreService vectorStoreService;
    private final QdrantClient qdrantClient;
    private final TextChunkerService textChunkerService;

    public DocumentService(PdfService pdfService,
                           VectorStoreService vectorStoreService,
                           QdrantClient qdrantClient,
                           TextChunkerService textChunkerService) {
        this.pdfService = pdfService;
        this.vectorStoreService = vectorStoreService;
        this.qdrantClient = qdrantClient;
        this.textChunkerService = textChunkerService;
    }

    public void indexPdf(String filePath) {

        // validar ANTES de usar
        if (filePath == null || filePath.isBlank()) {
            throw new RuntimeException("Invalid file path");
        }

        qdrantClient.createCollectionIfNotExists();

        String text = pdfService.extractText(filePath);

        if (text == null || text.isBlank()) {
            throw new RuntimeException("Empty PDF content");
        }

        List<String> chunks = textChunkerService.chunkText(text);

        vectorStoreService.storeChunks(chunks);

        System.out.println("Documento indexado correctamente");
    }
}