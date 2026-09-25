package com.kfokam.presencekf.web;

import com.kfokam.presencekf.domain.Exercice;
import com.kfokam.presencekf.service.ExerciceService;
import com.kfokam.presencekf.service.RelectureService;
import com.kfokam.presencekf.web.dto.*;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class ExerciceController {

    private final ExerciceService exerciceService;
    private final RelectureService relectureService;

    public ExerciceController(ExerciceService exerciceService, RelectureService relectureService) {
        this.exerciceService = exerciceService;
        this.relectureService = relectureService;
    }

    @PostMapping("/api/exercices")
    public ResponseEntity<ExerciceResponse> deposer(@Valid @RequestBody DeposerExerciceRequest request) {
        ExerciceResponse reponse = exerciceService.deposer(request.sessionId(), request.etudiantId(), request.lien());
        return ResponseEntity.status(HttpStatus.CREATED).body(reponse);
    }

    /** EF15 · RG10 : libre, non imposé par le contrat. */
    @PutMapping("/api/exercices/{id}")
    public ResponseEntity<ExerciceResponse> remplacerLien(@PathVariable Long id,
                                                            @RequestParam Long etudiantId,
                                                            @Valid @RequestBody RemplacerLienRequest request) {
        ExerciceResponse reponse = exerciceService.remplacerLien(id, etudiantId, request.lien());
        return ResponseEntity.ok(reponse);
    }

    @GetMapping("/api/sessions/{sessionId}/exercices/{etudiantId}")
    public ResponseEntity<ExerciceResponse> trouverPourSessionEtEtudiant(@PathVariable Long sessionId,
                                                                          @PathVariable Long etudiantId) {
        Exercice exercice = exerciceService.trouverPourSessionEtEtudiant(sessionId, etudiantId);
        return ResponseEntity.ok(new ExerciceResponse(exercice.getId(), exercice.getStatut()));
    }

    /** EF14 · RG6 : imposé par le contrat. */
    @GetMapping("/api/exercices/{id}/note")
    public ResponseEntity<NoteExerciceResponse> note(@PathVariable Long id) {
        return ResponseEntity.ok(relectureService.noteDeExercice(id));
    }
}
