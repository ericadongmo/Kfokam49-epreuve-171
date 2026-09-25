package com.kfokam.presencekf.web;

import com.kfokam.presencekf.service.EtudiantService;
import com.kfokam.presencekf.web.dto.EtudiantResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class EtudiantController {

    private final EtudiantService etudiantService;

    public EtudiantController(EtudiantService etudiantService) {
        this.etudiantService = etudiantService;
    }

    @GetMapping("/api/etudiants")
    public List<EtudiantResponse> lister(@RequestParam(required = false) Long promotionId) {
        return etudiantService.lister(promotionId);
    }
}
