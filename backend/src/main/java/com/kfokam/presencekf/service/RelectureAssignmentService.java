package com.kfokam.presencekf.service;

import com.kfokam.presencekf.domain.Exercice;
import com.kfokam.presencekf.domain.Presence;
import com.kfokam.presencekf.domain.Relecture;
import com.kfokam.presencekf.domain.enums.StatutExercice;
import com.kfokam.presencekf.domain.enums.StatutRelecture;
import com.kfokam.presencekf.repository.PresenceRepository;
import com.kfokam.presencekf.repository.RelectureRepository;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * EF8 · RG2, RG4, RG5 : tire un relecteur au hasard parmi les étudiants présents
 * à la session, hors auteur, dès qu'un exercice est déposé.
 */
@Component
public class RelectureAssignmentService {

    private final PresenceRepository presenceRepository;
    private final RelectureRepository relectureRepository;
    private final SecureRandom random = new SecureRandom();

    public RelectureAssignmentService(PresenceRepository presenceRepository, RelectureRepository relectureRepository) {
        this.presenceRepository = presenceRepository;
        this.relectureRepository = relectureRepository;
    }

    /**
     * @return la relecture créée, ou vide si aucun candidat n'est présent (l'exercice reste DEPOSE).
     */
    public Optional<Relecture> assigner(Exercice exercice) {
        if (relectureRepository.findByExerciceId(exercice.getId()).isPresent()) {
            return Optional.empty();
        }

        List<Long> candidats = presenceRepository.findBySessionId(exercice.getSessionId()).stream()
                .map(Presence::getEtudiantId)
                .filter(id -> !id.equals(exercice.getEtudiantId()))
                .distinct()
                .collect(Collectors.toList());

        if (candidats.isEmpty()) {
            return Optional.empty();
        }

        Long relecteurId = candidats.get(random.nextInt(candidats.size()));

        Relecture relecture = new Relecture();
        relecture.setExerciceId(exercice.getId());
        relecture.setRelecteurId(relecteurId);
        relecture.setStatut(StatutRelecture.EN_ATTENTE);
        relecture.setAssigneeAt(Instant.now());

        exercice.setStatut(StatutExercice.EN_ATTENTE);

        return Optional.of(relectureRepository.save(relecture));
    }
}
