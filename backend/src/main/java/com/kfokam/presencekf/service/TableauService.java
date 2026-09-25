package com.kfokam.presencekf.service;

import com.kfokam.presencekf.domain.Etudiant;
import com.kfokam.presencekf.domain.Exercice;
import com.kfokam.presencekf.domain.Presence;
import com.kfokam.presencekf.domain.Relecture;
import com.kfokam.presencekf.domain.enums.StatutRelecture;
import com.kfokam.presencekf.error.ApiException;
import com.kfokam.presencekf.repository.*;
import com.kfokam.presencekf.web.dto.LigneTableauResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/** EF12, EF13 · RG8 · ENF2 : une passe par collection, pas de requête par étudiant. */
@Service
public class TableauService {

    private final PromotionRepository promotionRepository;
    private final EtudiantRepository etudiantRepository;
    private final PresenceRepository presenceRepository;
    private final ExerciceRepository exerciceRepository;
    private final RelectureRepository relectureRepository;

    public TableauService(PromotionRepository promotionRepository, EtudiantRepository etudiantRepository,
                           PresenceRepository presenceRepository, ExerciceRepository exerciceRepository,
                           RelectureRepository relectureRepository) {
        this.promotionRepository = promotionRepository;
        this.etudiantRepository = etudiantRepository;
        this.presenceRepository = presenceRepository;
        this.exerciceRepository = exerciceRepository;
        this.relectureRepository = relectureRepository;
    }

    @Transactional(readOnly = true)
    public List<LigneTableauResponse> pourPromotion(Long promotionId) {
        if (!promotionRepository.existsById(promotionId)) {
            throw ApiException.promotionInconnue();
        }

        List<Etudiant> etudiants = etudiantRepository.findByPromotionIdOrderByNomAsc(promotionId);
        List<Long> etudiantIds = etudiants.stream().map(Etudiant::getId).toList();

        Map<Long, Long> presencesParEtudiant = presenceRepository.findByEtudiantIdIn(etudiantIds).stream()
                .collect(Collectors.groupingBy(Presence::getEtudiantId, Collectors.counting()));

        List<Exercice> exercices = exerciceRepository.findByEtudiantIdIn(etudiantIds);
        Map<Long, Long> exercicesParEtudiant = exercices.stream()
                .collect(Collectors.groupingBy(Exercice::getEtudiantId, Collectors.counting()));

        List<Long> exerciceIds = exercices.stream().map(Exercice::getId).toList();
        List<Relecture> relecturesRecues = relectureRepository.findByExerciceIdIn(exerciceIds);
        Map<Long, Long> exerciceIdVersEtudiant = exercices.stream()
                .collect(Collectors.toMap(Exercice::getId, Exercice::getEtudiantId));

        Map<Long, List<Integer>> notesParEtudiant = relecturesRecues.stream()
                .filter(r -> r.getStatut() == StatutRelecture.RENDUE)
                .collect(Collectors.groupingBy(
                        r -> exerciceIdVersEtudiant.get(r.getExerciceId()),
                        Collectors.mapping(Relecture::getNote, Collectors.toList())));

        Map<Long, Long> relecturesEnAttenteParRelecteur = relectureRepository.findByRelecteurIdIn(etudiantIds).stream()
                .filter(r -> r.getStatut() != StatutRelecture.RENDUE)
                .collect(Collectors.groupingBy(Relecture::getRelecteurId, Collectors.counting()));

        return etudiants.stream()
                .map(e -> new LigneTableauResponse(
                        e.getId(),
                        e.getNom(),
                        presencesParEtudiant.getOrDefault(e.getId(), 0L),
                        exercicesParEtudiant.getOrDefault(e.getId(), 0L),
                        moyenne(notesParEtudiant.get(e.getId())),
                        relecturesEnAttenteParRelecteur.getOrDefault(e.getId(), 0L)
                ))
                .toList();
    }

    private Double moyenne(List<Integer> notes) {
        if (notes == null || notes.isEmpty()) {
            return null;
        }
        return notes.stream().mapToInt(Integer::intValue).average().orElseThrow();
    }
}
