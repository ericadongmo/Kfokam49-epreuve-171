package com.kfokam.presencekf.web.dto;

import com.kfokam.presencekf.domain.enums.StatutRelecture;

/** EF14 · RG6 : jamais le nom ni l'identifiant du relecteur. */
public record NoteExerciceResponse(
        StatutRelecture statut,
        Integer note,
        String commentaire
) {
}
