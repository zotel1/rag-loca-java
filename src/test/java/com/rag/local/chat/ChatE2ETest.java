package com.rag.local.chat;

import com.rag.local.client.OllamaClient;
import com.rag.local.model.EmbeddingResponse;
import com.rag.local.service.ChatService;
import com.rag.local.service.SearchService;
import com.rag.local.service.TextChunkerService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ChatE2ETest {

    @MockitoBean
    private SearchService searchService;

    @MockitoBean
    private OllamaClient ollamaClient;

    @Autowired
    private ChatService chatService;

    @LocalServerPort
    private int port;

    private final RestTemplate restTemplate = new RestTemplate();

    @Test
    void shouldRespondQuicklyWithLightModel() {

        /*
        // mock embedding
        EmbeddingResponse embeddingResponse = new EmbeddingResponse();
        embeddingResponse.setEmbedding(List.of(0.1, 0.2, 0.3));

        Mockito.when(ollamaClient.generateEmbedding(Mockito.anyString()))
                .thenReturn(embeddingResponse);

        // mock respuesta del modelo
        Mockito.when(ollamaClient.chat(Mockito.anyString()))
                .thenReturn("Respuesta mockeada");

        String response = chatService.ask("¿Qué es la electricidad?");

        assertThat(response).isNotBlank(); */

        Mockito.when(searchService.search(Mockito.anyString()))
                .thenReturn(List.of("La electricidad es una forma de energía."));

        Mockito.when(ollamaClient.chat(Mockito.anyString()))
                .thenReturn("Respuesta mockeada");

        String response = chatService.ask("¿Qué es la electricidad?");

        assertThat(response).isNotBlank();
    }
}