package com.kfokam.presencekf.web.dto;

import com.kfokam.presencekf.domain.enums.StatutExercice;

public record ExerciceResponse(
        Long id,
        StatutExercice statut
) {
}
