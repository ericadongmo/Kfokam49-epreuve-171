package com.kfokam.presencekf.web.dto;

import jakarta.validation.constraints.NotBlank;

public record RemplacerLienRequest(
        @NotBlank(message = "lien est requis") String lien
) {
}
