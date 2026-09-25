package com.kfokam.presencekf.service;

import com.kfokam.presencekf.domain.TentativeCode;
import com.kfokam.presencekf.error.ApiException;
import com.kfokam.presencekf.repository.TentativeCodeRepository;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.util.Comparator;
import java.util.List;

/**
 * EF17 · RG15 : fenêtre glissante des 5 dernières tentatives d'un étudiant.
 * Si elles sont toutes en échec, la dernière déclenche un blocage de 2 minutes ;
 * une tentative réussie casse la série (compteur remis à zéro, section 7).
 */
@Component
public class RateLimitService {

    static final int SEUIL_ECHECS = 5;
    static final Duration DUREE_BLOCAGE = Duration.ofMinutes(2);
    private static final Duration FENETRE_HISTORIQUE = Duration.ofHours(1);

    private final TentativeCodeRepository tentativeCodeRepository;

    public RateLimitService(TentativeCodeRepository tentativeCodeRepository) {
        this.tentativeCodeRepository = tentativeCodeRepository;
    }

    public void verifierAutorise(Long etudiantId) {
        List<TentativeCode> recentes = tentativeCodeRepository
                .findByEtudiantIdAndAtAfterOrderByAtAsc(etudiantId, Instant.now().minus(FENETRE_HISTORIQUE));

        if (recentes.size() < SEUIL_ECHECS) {
            return;
        }

        List<TentativeCode> cinqDernieres = recentes.subList(recentes.size() - SEUIL_ECHECS, recentes.size());
        boolean toutesEnEchec = cinqDernieres.stream().noneMatch(TentativeCode::isSucces);
        if (!toutesEnEchec) {
            return;
        }

        Instant derniereTentative = cinqDernieres.stream()
                .map(TentativeCode::getAt)
                .max(Comparator.naturalOrder())
                .orElseThrow();
        Instant finBlocage = derniereTentative.plus(DUREE_BLOCAGE);
        if (Instant.now().isBefore(finBlocage)) {
            long secondesRestantes = Duration.between(Instant.now(), finBlocage).toSeconds() + 1;
            throw ApiException.tropDeTentatives(secondesRestantes);
        }
    }

    public void enregistrerEchec(Long etudiantId) {
        enregistrer(etudiantId, false);
    }

    public void enregistrerSucces(Long etudiantId) {
        enregistrer(etudiantId, true);
    }

    private void enregistrer(Long etudiantId, boolean succes) {
        TentativeCode tentative = new TentativeCode();
        tentative.setEtudiantId(etudiantId);
        tentative.setAt(Instant.now());
        tentative.setSucces(succes);
        tentativeCodeRepository.save(tentative);
    }
}
