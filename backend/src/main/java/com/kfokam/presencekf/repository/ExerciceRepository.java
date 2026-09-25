package com.kfokam.presencekf.repository;

import com.kfokam.presencekf.domain.Exercice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ExerciceRepository extends JpaRepository<Exercice, Long> {

    Optional<Exercice> findBySessionIdAndEtudiantId(Long sessionId, Long etudiantId);

    boolean existsBySessionIdAndEtudiantId(Long sessionId, Long etudiantId);

    List<Exercice> findByEtudiantIdIn(List<Long> etudiantIds);
}
