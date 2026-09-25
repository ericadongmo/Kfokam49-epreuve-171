package com.kfokam.presencekf.domain;

import com.kfokam.presencekf.domain.enums.StatutExercice;
import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "exercice", uniqueConstraints = {
        @UniqueConstraint(name = "uk_exercice_session_etudiant", columnNames = {"session_id", "etudiant_id"})
})
public class Exercice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "session_id", nullable = false)
    private Long sessionId;

    @Column(name = "etudiant_id", nullable = false)
    private Long etudiantId;

    @Column(nullable = false, length = 2048)
    private String lien;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatutExercice statut;

    @Column(name = "depose_at", nullable = false)
    private Instant deposeAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSessionId() {
        return sessionId;
    }

    public void setSessionId(Long sessionId) {
        this.sessionId = sessionId;
    }

    public Long getEtudiantId() {
        return etudiantId;
    }

    public void setEtudiantId(Long etudiantId) {
        this.etudiantId = etudiantId;
    }

    public String getLien() {
        return lien;
    }

    public void setLien(String lien) {
        this.lien = lien;
    }

    public StatutExercice getStatut() {
        return statut;
    }

    public void setStatut(StatutExercice statut) {
        this.statut = statut;
    }

    public Instant getDeposeAt() {
        return deposeAt;
    }

    public void setDeposeAt(Instant deposeAt) {
        this.deposeAt = deposeAt;
    }
}
