package com.kfokam.presencekf.service;

import com.kfokam.presencekf.domain.TentativeCode;
import com.kfokam.presencekf.error.ApiException;
import com.kfokam.presencekf.repository.TentativeCodeRepository;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/** Test unitaire pur sur RG15 : blocage après 5 échecs consécutifs, remis à zéro par un succès. */
class RateLimitServiceTest {

    private final TentativeCodeRepository repository = mock(TentativeCodeRepository.class);
    private final RateLimitService service = new RateLimitService(repository);

    @Test
    void bloque_apres_cinq_echecs_consecutifs() {
        when(repository.findByEtudiantIdAndAtAfterOrderByAtAsc(any(), any())).thenReturn(tentatives(5, false));

        assertThatThrownBy(() -> service.verifierAutorise(1L))
                .isInstanceOf(ApiException.class)
                .extracting("code").isEqualTo("TROP_DE_TENTATIVES");
    }

    @Test
    void n_est_pas_bloque_si_moins_de_cinq_echecs() {
        when(repository.findByEtudiantIdAndAtAfterOrderByAtAsc(any(), any())).thenReturn(tentatives(4, false));

        assertThatDoesNotThrow(() -> service.verifierAutorise(1L));
    }

    @Test
    void un_succes_dans_les_cinq_dernieres_tentatives_casse_le_blocage() {
        List<TentativeCode> tentatives = tentatives(4, false);
        tentatives.add(tentative(true, Instant.now()));

        when(repository.findByEtudiantIdAndAtAfterOrderByAtAsc(any(), any())).thenReturn(tentatives);

        assertThatDoesNotThrow(() -> service.verifierAutorise(1L));
    }

    private static void assertThatDoesNotThrow(Runnable runnable) {
        runnable.run();
    }

    private List<TentativeCode> tentatives(int nombre, boolean succes) {
        List<TentativeCode> liste = new ArrayList<>();
        for (int i = 0; i < nombre; i++) {
            liste.add(tentative(succes, Instant.now().minusSeconds((nombre - i) * 5L)));
        }
        return liste;
    }

    private TentativeCode tentative(boolean succes, Instant at) {
        TentativeCode t = new TentativeCode();
        t.setEtudiantId(1L);
        t.setSucces(succes);
        t.setAt(at);
        return t;
    }
}
