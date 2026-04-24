package com.rag.local.controller;

import com.rag.local.service.DocumentService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/upload")
public class UploadController {

    private final DocumentService  documentService;

    public UploadController(DocumentService documentService) {
        this.documentService = documentService;
    }

    @PostMapping
    public String upload(@RequestBody Map<String, String> body) {

        String path = body.get("path");

        documentService.indexPdf(path);

        return "Documento indexado correctamente";
    }
}
