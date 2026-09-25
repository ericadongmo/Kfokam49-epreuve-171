package com.kfokam.presencekf.web.dto;

import com.kfokam.presencekf.domain.enums.StatutRelecture;

public record RelectureResponse(
        Long id,
        StatutRelecture statut
) {
}
