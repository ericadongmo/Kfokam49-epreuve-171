package com.kfokam.presencekf.web;

import com.kfokam.presencekf.domain.Presence;
import com.kfokam.presencekf.domain.Session;
import com.kfokam.presencekf.repository.EtudiantRepository;
import com.kfokam.presencekf.service.PresenceService;
import com.kfokam.presencekf.service.SessionService;
import com.kfokam.presencekf.web.dto.*;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/sessions")
public class SessionController {

    private final SessionService sessionService;
    private final PresenceService presenceService;
    private final EtudiantRepository etudiantRepository;

    public SessionController(SessionService sessionService, PresenceService presenceService,
                              EtudiantRepository etudiantRepository) {
        this.sessionService = sessionService;
        this.presenceService = presenceService;
        this.etudiantRepository = etudiantRepository;
    }

    @PostMapping
    public ResponseEntity<SessionResponse> ouvrir(@Valid @RequestBody OuvrirSessionRequest request) {
        SessionResponse reponse = sessionService.ouvrir(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(reponse);
    }

    @GetMapping("/{id}")
    public SessionResponse trouver(@PathVariable Long id) {
        return toResponse(sessionService.trouver(id));
    }

    /** Libre : permet à l'écran étudiant/relecteur de retrouver la session à partir du code de classe. */
    @GetMapping("/code/{code}")
    public SessionResponse trouverParCode(@PathVariable String code) {
        return toResponse(sessionService.trouverParCode(code));
    }

    @GetMapping("/{id}/presences")
    public List<PresenceAvecNomResponse> presences(@PathVariable Long id) {
        Map<Long, String> noms = etudiantRepository.findAll().stream()
                .collect(Collectors.toMap(com.kfokam.presencekf.domain.Etudiant::getId, com.kfokam.presencekf.domain.Etudiant::getNom));
        List<Presence> presences = presenceService.listerPourSession(id);
        return presences.stream()
                .map(p -> new PresenceAvecNomResponse(p.getEtudiantId(), noms.get(p.getEtudiantId()), p.getSource()))
                .toList();
    }

    @PostMapping("/{id}/cloture")
    public ClotureResponse cloturer(@PathVariable Long id) {
        return sessionService.cloturer(id);
    }

    private SessionResponse toResponse(Session session) {
        return new SessionResponse(session.getId(), session.getTitre(), session.getPromotionId(), session.getCode(),
                session.getOuvertureAt(), session.getExpirationAt(), session.isCloturee());
    }
}
