package com.kfokam.presencekf.repository;

import com.kfokam.presencekf.domain.TentativeCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;

public interface TentativeCodeRepository extends JpaRepository<TentativeCode, Long> {

    List<TentativeCode> findByEtudiantIdAndAtAfterOrderByAtAsc(Long etudiantId, Instant after);
}
