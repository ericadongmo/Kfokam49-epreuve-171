package com.kfokam.presencekf.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record OuvrirSessionRequest(
        @NotBlank(message = "le titre est requis") String titre,
        @NotNull(message = "promotionId est requis") Long promotionId
) {
}
