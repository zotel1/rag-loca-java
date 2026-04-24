package com.rag.local.service;

import org.springframework.stereotype.Service;

import java.security.MessageDigest;

@Service
public class HashService {

    public String generateHash(String text) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(text.getBytes());

            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                hexString.append(String.format("%02x", b));
            }
            return hexString.toString();
        } catch (Exception e) {
            throw new RuntimeException("Error generando hash", e);
        }
    }
}
