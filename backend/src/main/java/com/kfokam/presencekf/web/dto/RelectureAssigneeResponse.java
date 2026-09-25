package com.kfokam.presencekf.web.dto;

import com.kfokam.presencekf.domain.enums.StatutRelecture;

/** Une relecture assignée à un relecteur (écran relecteur, F2). */
public record RelectureAssigneeResponse(
        Long relectureId,
        Long exerciceId,
        String lien,
        StatutRelecture statut,
        Integer note,
        String commentaire
) {
}
