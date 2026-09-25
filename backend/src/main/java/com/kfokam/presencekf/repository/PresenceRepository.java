package com.kfokam.presencekf.repository;

import com.kfokam.presencekf.domain.Presence;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PresenceRepository extends JpaRepository<Presence, Long> {

    Optional<Presence> findBySessionIdAndEtudiantId(Long sessionId, Long etudiantId);

    boolean existsBySessionIdAndEtudiantId(Long sessionId, Long etudiantId);

    List<Presence> findBySessionId(Long sessionId);

    int countByEtudiantIdIn(List<Long> etudiantIds);

    List<Presence> findByEtudiantIdIn(List<Long> etudiantIds);
}
