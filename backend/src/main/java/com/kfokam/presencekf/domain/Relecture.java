package com.kfokam.presencekf.domain;

import com.kfokam.presencekf.domain.enums.StatutRelecture;
import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "relecture")
public class Relecture {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "exercice_id", nullable = false, unique = true)
    private Long exerciceId;

    @Column(name = "relecteur_id", nullable = false)
    private Long relecteurId;

    @Column
    private Integer note;

    @Column(length = 4000)
    private String commentaire;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatutRelecture statut;

    @Column(name = "assignee_at", nullable = false)
    private Instant assigneeAt;

    @Column(name = "rendu_at")
    private Instant renduAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getExerciceId() {
        return exerciceId;
    }

    public void setExerciceId(Long exerciceId) {
        this.exerciceId = exerciceId;
    }

    public Long getRelecteurId() {
        return relecteurId;
    }

    public void setRelecteurId(Long relecteurId) {
        this.relecteurId = relecteurId;
    }

    public Integer getNote() {
        return note;
    }

    public void setNote(Integer note) {
        this.note = note;
    }

    public String getCommentaire() {
        return commentaire;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    public StatutRelecture getStatut() {
        return statut;
    }

    public void setStatut(StatutRelecture statut) {
        this.statut = statut;
    }

    public Instant getAssigneeAt() {
        return assigneeAt;
    }

    public void setAssigneeAt(Instant assigneeAt) {
        this.assigneeAt = assigneeAt;
    }

    public Instant getRenduAt() {
        return renduAt;
    }

    public void setRenduAt(Instant renduAt) {
        this.renduAt = renduAt;
    }
}
