package com.kfokam.presencekf.web.dto;

public record LigneTableauResponse(
        Long etudiantId,
        String nom,
        long presences,
        long exercicesDeposes,
        Double moyenne,
        long relecturesEnAttente
) {
}
