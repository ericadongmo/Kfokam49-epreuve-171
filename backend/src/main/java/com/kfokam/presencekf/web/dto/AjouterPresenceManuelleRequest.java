package com.kfokam.presencekf.web.dto;

import jakarta.validation.constraints.NotNull;

public record AjouterPresenceManuelleRequest(
        @NotNull(message = "etudiantId est requis") Long etudiantId
) {
}
