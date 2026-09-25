package com.kfokam.presencekf.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record MarquerPresenceRequest(
        @NotBlank(message = "le code est requis") String code,
        @NotNull(message = "etudiantId est requis") Long etudiantId
) {
}
