package com.kfokam.presencekf.domain;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "tentative_code")
public class TentativeCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "etudiant_id", nullable = false)
    private Long etudiantId;

    @Column(name = "session_id")
    private Long sessionId;

    @Column(nullable = false)
    private Instant at;

    @Column(nullable = false)
    private boolean succes;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getEtudiantId() {
        return etudiantId;
    }

    public void setEtudiantId(Long etudiantId) {
        this.etudiantId = etudiantId;
    }

    public Long getSessionId() {
        return sessionId;
    }

    public void setSessionId(Long sessionId) {
        this.sessionId = sessionId;
    }

    public Instant getAt() {
        return at;
    }

    public void setAt(Instant at) {
        this.at = at;
    }

    public boolean isSucces() {
        return succes;
    }

    public void setSucces(boolean succes) {
        this.succes = succes;
    }
}
