package com.kfokam.presencekf.repository;

import com.kfokam.presencekf.domain.Etudiant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EtudiantRepository extends JpaRepository<Etudiant, Long> {

    List<Etudiant> findByPromotionIdOrderByNomAsc(Long promotionId);
}
