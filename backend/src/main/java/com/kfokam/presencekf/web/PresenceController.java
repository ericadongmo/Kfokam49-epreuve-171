package com.kfokam.presencekf.web;

import com.kfokam.presencekf.service.PresenceService;
import com.kfokam.presencekf.web.dto.AjouterPresenceManuelleRequest;
import com.kfokam.presencekf.web.dto.MarquerPresenceRequest;
import com.kfokam.presencekf.web.dto.PresenceResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class PresenceController {

    private final PresenceService presenceService;

    public PresenceController(PresenceService presenceService) {
        this.presenceService = presenceService;
    }

    @PostMapping("/api/presences")
    public ResponseEntity<PresenceResponse> marquer(@Valid @RequestBody MarquerPresenceRequest request) {
        PresenceResponse reponse = presenceService.marquerParCode(request.code(), request.etudiantId());
        return ResponseEntity.status(HttpStatus.CREATED).body(reponse);
    }

    @PostMapping("/api/sessions/{id}/presences/manuel")
    public ResponseEntity<PresenceResponse> ajouterManuellement(@PathVariable Long id,
                                                                  @Valid @RequestBody AjouterPresenceManuelleRequest request) {
        PresenceResponse reponse = presenceService.ajouterManuellement(id, request.etudiantId());
        return ResponseEntity.status(HttpStatus.CREATED).body(reponse);
    }
}
