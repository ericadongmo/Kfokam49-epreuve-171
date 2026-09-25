package com.kfokam.presencekf.web.dto;

import java.time.Instant;

public record SessionResponse(
        Long id,
        String titre,
        Long promotionId,
        String code,
        Instant ouvertureAt,
        Instant expirationAt,
        boolean cloturee
) {
}
