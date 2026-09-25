package com.kfokam.presencekf.service;

import com.kfokam.presencekf.repository.EtudiantRepository;
import com.kfokam.presencekf.web.dto.EtudiantResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EtudiantService {

    private final EtudiantRepository etudiantRepository;

    public EtudiantService(EtudiantRepository etudiantRepository) {
        this.etudiantRepository = etudiantRepository;
    }

    @Transactional(readOnly = true)
    public List<EtudiantResponse> lister(Long promotionId) {
        List<com.kfokam.presencekf.domain.Etudiant> etudiants = promotionId != null
                ? etudiantRepository.findByPromotionIdOrderByNomAsc(promotionId)
                : etudiantRepository.findAll();
        return etudiants.stream()
                .map(e -> new EtudiantResponse(e.getId(), e.getNom(), e.getPromotionId()))
                .toList();
    }
}
