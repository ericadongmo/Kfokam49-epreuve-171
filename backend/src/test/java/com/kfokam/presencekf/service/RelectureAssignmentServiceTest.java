package com.kfokam.presencekf.service;

import com.kfokam.presencekf.domain.Exercice;
import com.kfokam.presencekf.domain.Presence;
import com.kfokam.presencekf.domain.Relecture;
import com.kfokam.presencekf.domain.enums.SourcePresence;
import com.kfokam.presencekf.domain.enums.StatutExercice;
import com.kfokam.presencekf.repository.PresenceRepository;
import com.kfokam.presencekf.repository.RelectureRepository;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Test unitaire pur (pas de Spring, pas de base) sur RG2, RG4, RG5 : le
 * relecteur est tiré parmi les présents, jamais l'auteur, jamais deux fois.
 */
class RelectureAssignmentServiceTest {

    private final PresenceRepository presenceRepository = mock(PresenceRepository.class);
    private final RelectureRepository relectureRepository = mock(RelectureRepository.class);
    private final RelectureAssignmentService service = new RelectureAssignmentService(presenceRepository, relectureRepository);

    @Test
    void n_assigne_jamais_l_auteur_comme_relecteur() {
        Exercice exercice = exercice(1L, 10L, 100L);
        when(relectureRepository.findByExerciceId(1L)).thenReturn(Optional.empty());
        when(presenceRepository.findBySessionId(10L)).thenReturn(List.of(
                presence(10L, 100L),
                presence(10L, 200L)
        ));
        when(relectureRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        Optional<Relecture> resultat = service.assigner(exercice);

        assertThat(resultat).isPresent();
        assertThat(resultat.get().getRelecteurId()).isEqualTo(200L);
        assertThat(exercice.getStatut()).isEqualTo(StatutExercice.EN_ATTENTE);
    }

    @Test
    void laisse_l_exercice_depose_sans_relecture_si_l_auteur_est_le_seul_present() {
        Exercice exercice = exercice(1L, 10L, 100L);
        when(relectureRepository.findByExerciceId(1L)).thenReturn(Optional.empty());
        when(presenceRepository.findBySessionId(10L)).thenReturn(List.of(presence(10L, 100L)));

        Optional<Relecture> resultat = service.assigner(exercice);

        assertThat(resultat).isEmpty();
        verify(relectureRepository, never()).save(any());
        assertThat(exercice.getStatut()).isEqualTo(StatutExercice.DEPOSE);
    }

    @Test
    void n_assigne_pas_un_second_relecteur_si_un_existe_deja() {
        Exercice exercice = exercice(1L, 10L, 100L);
        when(relectureRepository.findByExerciceId(1L)).thenReturn(Optional.of(new Relecture()));

        Optional<Relecture> resultat = service.assigner(exercice);

        assertThat(resultat).isEmpty();
        verifyNoInteractions(presenceRepository);
    }

    private Exercice exercice(Long id, Long sessionId, Long etudiantId) {
        Exercice exercice = new Exercice();
        exercice.setId(id);
        exercice.setSessionId(sessionId);
        exercice.setEtudiantId(etudiantId);
        exercice.setStatut(StatutExercice.DEPOSE);
        exercice.setDeposeAt(Instant.now());
        return exercice;
    }

    private Presence presence(Long sessionId, Long etudiantId) {
        Presence presence = new Presence();
        presence.setSessionId(sessionId);
        presence.setEtudiantId(etudiantId);
        presence.setSource(SourcePresence.ETUDIANT);
        presence.setMarqueeAt(Instant.now());
        return presence;
    }
}
