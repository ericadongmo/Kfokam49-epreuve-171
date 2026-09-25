package com.kfokam.presencekf.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DeposerExerciceRequest(
        @NotNull(message = "sessionId est requis") Long sessionId,
        @NotNull(message = "etudiantId est requis") Long etudiantId,
        @NotBlank(message = "lien est requis") String lien
) {
}
