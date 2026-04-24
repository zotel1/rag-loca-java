package com.rag.local;

import com.rag.local.client.QdrantClient;
import com.rag.local.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
public class ChatApplication implements CommandLineRunner {

	@Autowired
	private PdfService pdfService;

	@Autowired
	private ChunkService chunkService;

	@Autowired
	private HashService hashService;

	@Autowired
	private EmbeddingService embeddingService;

	@Autowired
	private VectorStoreService vectorStoreService;

	@Autowired
	private QdrantClient qdrantClient;

	@Autowired
	private SearchService searchService;

	public static void main(String[] args) {
		SpringApplication.run(ChatApplication.class, args);


	}

	@Override
	public void run(String... args) throws InterruptedException {



		List<Double> embedding = embeddingService.generateEmbedding("Hola mundo");
		System.out.println("Embedding size: " + embedding.size());

		String text = pdfService.extractText("C:\\Users\\zottt\\Desktop\\Instituto-Superior-de-Formacion-docente-Ituzaingo\\primer_anio\\arquitectura_de_computadoras\\Conceptos Basicos de electricidad.pdf");
		List<String> chunks = chunkService.splitText(text, 500);

		for (String chunk : chunks) {
			String hash = hashService.generateHash(chunk);

		}

		qdrantClient.createCollectionIfNotExists();

		vectorStoreService.storeChunks(chunks);
		Thread.sleep(2000);
		System.out.println("Chunks guardados en Qdrant");

		List<String> results = searchService.search("¿Qué es la electricidad?");
		System.out.println("Resultados encontrados: " + results.size());
		results.forEach(System.out::println);
	}
}
