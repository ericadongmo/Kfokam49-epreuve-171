package com.kfokam.presencekf.service;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;

/**
 * Génère les codes de présence : 6 caractères alphanumériques majuscules,
 * tirés via {@link SecureRandom} pour rester non devinables (ENF4).
 */
@Component
public class CodeGenerator {

    private static final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int LONGUEUR = 6;

    private final SecureRandom random = new SecureRandom();

    public String genererCode() {
        StringBuilder sb = new StringBuilder(LONGUEUR);
        for (int i = 0; i < LONGUEUR; i++) {
            sb.append(ALPHABET.charAt(random.nextInt(ALPHABET.length())));
        }
        return sb.toString();
    }
}
