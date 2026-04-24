package com.rag.local.service;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias para HashService.
 * Verifica consistencia y unicidad de los hashes.
 */
class HashServiceTest {

    private final HashService hashService = new HashService();

    @Test
    void mismoTextoDebeGenerarMismoHash() {
        String texto = "Hola mundo";

        String hash1 = hashService.generateHash(texto);
        String hash2 = hashService.generateHash(texto);

        assertEquals(hash1, hash2);
    }

    @Test
    void textosDistintosDebenGenerarHashesDistintos() {
        String texto1 = "Hola mundo";
        String texto2 = "Hola mundo!";

        String hash1 = hashService.generateHash(texto1);
        String hash2 = hashService.generateHash(texto2);

        assertNotEquals(hash1, hash2);
    }
}