package com.rag.local;

import com.rag.local.service.ChunkService;
import com.rag.local.service.HashService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.rag.local.service.PdfService;

import java.util.List;

@SpringBootApplication
public class ChatApplication implements CommandLineRunner {

	@Autowired
	private PdfService pdfService;

	@Autowired
	private ChunkService chunkService;

	@Autowired
	private HashService hashService;

	public static void main(String[] args) {
		SpringApplication.run(ChatApplication.class, args);


	}

	@Override
	public void run(String... args) {

		String text = pdfService.extractText("C:\\Users\\zottt\\Desktop\\Instituto-Superior-de-Formacion-docente-Ituzaingo\\primer_anio\\arquitectura_de_computadoras\\Conceptos Basicos de electricidad.pdf");
		List<String> chunks = chunkService.splitText(text, 500);

		for (String chunk : chunks) {
			String hash = hashService.generateHash(chunk);
			System.out.println(hash);
		}
	}
}
