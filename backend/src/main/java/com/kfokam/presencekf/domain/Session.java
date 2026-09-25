package com.kfokam.presencekf.domain;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "session_cours")
public class Session {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titre;

    @Column(name = "promotion_id", nullable = false)
    private Long promotionId;

    @Column(name = "formateur_id", nullable = false)
    private Long formateurId;

    @Column(nullable = false, unique = true, length = 6)
    private String code;

    @Column(name = "ouverture_at", nullable = false)
    private Instant ouvertureAt;

    @Column(name = "expiration_at", nullable = false)
    private Instant expirationAt;

    @Column(name = "cloture_at")
    private Instant clotureAt;

    @Column(nullable = false)
    private boolean cloturee = false;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public Long getPromotionId() {
        return promotionId;
    }

    public void setPromotionId(Long promotionId) {
        this.promotionId = promotionId;
    }

    public Long getFormateurId() {
        return formateurId;
    }

    public void setFormateurId(Long formateurId) {
        this.formateurId = formateurId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Instant getOuvertureAt() {
        return ouvertureAt;
    }

    public void setOuvertureAt(Instant ouvertureAt) {
        this.ouvertureAt = ouvertureAt;
    }

    public Instant getExpirationAt() {
        return expirationAt;
    }

    public void setExpirationAt(Instant expirationAt) {
        this.expirationAt = expirationAt;
    }

    public Instant getClotureAt() {
        return clotureAt;
    }

    public void setClotureAt(Instant clotureAt) {
        this.clotureAt = clotureAt;
    }

    public boolean isCloturee() {
        return cloturee;
    }

    public void setCloturee(boolean cloturee) {
        this.cloturee = cloturee;
    }
}
