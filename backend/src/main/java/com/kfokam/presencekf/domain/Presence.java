package com.kfokam.presencekf.domain;

import com.kfokam.presencekf.domain.enums.SourcePresence;
import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "presence", uniqueConstraints = {
        @UniqueConstraint(name = "uk_presence_session_etudiant", columnNames = {"session_id", "etudiant_id"})
})
public class Presence {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "session_id", nullable = false)
    private Long sessionId;

    @Column(name = "etudiant_id", nullable = false)
    private Long etudiantId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private SourcePresence source;

    @Column(name = "marquee_at", nullable = false)
    private Instant marqueeAt;

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

    public SourcePresence getSource() {
        return source;
    }

    public void setSource(SourcePresence source) {
        this.source = source;
    }

    public Instant getMarqueeAt() {
        return marqueeAt;
    }

    public void setMarqueeAt(Instant marqueeAt) {
        this.marqueeAt = marqueeAt;
    }
}
