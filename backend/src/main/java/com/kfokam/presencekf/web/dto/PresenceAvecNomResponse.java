package com.kfokam.presencekf.web.dto;

import com.kfokam.presencekf.domain.enums.SourcePresence;

public record PresenceAvecNomResponse(
        Long etudiantId,
        String nom,
        SourcePresence source
) {
}
