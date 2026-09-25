package com.kfokam.presencekf.repository;

import com.kfokam.presencekf.domain.Relecture;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RelectureRepository extends JpaRepository<Relecture, Long> {

    Optional<Relecture> findByExerciceId(Long exerciceId);

    List<Relecture> findByRelecteurIdIn(List<Long> relecteurIds);

    List<Relecture> findByExerciceIdIn(List<Long> exerciceIds);
}
