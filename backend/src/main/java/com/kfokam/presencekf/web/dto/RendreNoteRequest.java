package com.kfokam.presencekf.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * {@code relecteurId} est une extension libre du contrat (champ additionnel
 * toléré par le schéma OpenAPI) : l'application n'a pas d'authentification,
 * c'est le seul moyen pour le serveur de vérifier RG2 (AUTO_RELECTURE) et que
 * l'appelant est bien le relecteur assigné.
 */
public record RendreNoteRequest(
        Integer note,
        @NotBlank(message = "le commentaire est requis") String commentaire,
        @NotNull(message = "relecteurId est requis") Long relecteurId
) {
}
