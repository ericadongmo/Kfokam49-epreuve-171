package com.kfokam.presencekf.web.dto;

import com.kfokam.presencekf.domain.enums.SourcePresence;

public record PresenceResponse(
        Long id,
        Long sessionId,
        Long etudiantId,
        SourcePresence source
) {
}
