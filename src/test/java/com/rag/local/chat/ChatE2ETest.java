package com.rag.local.chat;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ChatE2ETest {

    @LocalServerPort
    private int port;

    private final org.springframework.web.client.RestTemplate restTemplate =
            new org.springframework.web.client.RestTemplate();
    @Test
    void shouldUploadAndAnswerQuestion() {

        String baseUrl = "http://localhost:" + port;

        // 1. Upload del PDF
        String uploadUrl = baseUrl + "/api/upload";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, String> uploadBody = Map.of(
                "path", "C:\\Users\\zottt\\Desktop\\Instituto-Superior-de-Formacion-docente-Ituzaingo\\primer_anio\\arquitectura_de_computadoras\\Conceptos Basicos de electricidad.pdf"
        );

        HttpEntity<Map<String, String>> uploadRequest = new HttpEntity<>(uploadBody, headers);

        ResponseEntity<String> uploadResponse =
                restTemplate.postForEntity(uploadUrl, uploadRequest, String.class);

        assertThat(uploadResponse.getStatusCode()).isEqualTo(HttpStatus.OK);


        // 2. Pregunta al chat
        String chatUrl = baseUrl + "/api/chat";

        Map<String, String> chatBody = Map.of(
                "question", "¿Qué es la electricidad?"
        );

        HttpEntity<Map<String, String>> chatRequest = new HttpEntity<>(chatBody, headers);

        ResponseEntity<String> chatResponse =
                restTemplate.postForEntity(chatUrl, chatRequest, String.class);

        assertThat(chatResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

        String response = chatResponse.getBody();

        System.out.println("RESPUESTA E2E: " + response);

        assertThat(response).isNotNull();
        assertThat(response).isNotBlank();
    }
}