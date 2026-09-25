package com.kfokam.presencekf.web;

import com.kfokam.presencekf.service.RelectureService;
import com.kfokam.presencekf.web.dto.RelectureAssigneeResponse;
import com.kfokam.presencekf.web.dto.RelectureResponse;
import com.kfokam.presencekf.web.dto.RendreNoteRequest;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class RelectureController {

    private final RelectureService relectureService;

    public RelectureController(RelectureService relectureService) {
        this.relectureService = relectureService;
    }

    @PostMapping("/api/relectures/{id}")
    public RelectureResponse rendre(@PathVariable Long id, @Valid @RequestBody RendreNoteRequest request) {
        return relectureService.rendre(id, request.note(), request.commentaire(), request.relecteurId());
    }

    @PostMapping("/api/relectures/{id}/commencer")
    public RelectureResponse commencer(@PathVariable Long id) {
        return relectureService.commencer(id);
    }

    @GetMapping("/api/etudiants/{id}/relectures")
    public List<RelectureAssigneeResponse> pourRelecteur(@PathVariable Long id) {
        return relectureService.pourRelecteur(id);
    }
}
