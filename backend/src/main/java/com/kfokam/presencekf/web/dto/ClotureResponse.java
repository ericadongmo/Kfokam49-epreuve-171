package com.kfokam.presencekf.web.dto;

import java.time.Instant;

public record ClotureResponse(
        Long id,
        boolean cloturee,
        Instant clotureAt
) {
}
