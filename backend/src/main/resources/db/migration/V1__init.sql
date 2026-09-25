-- Schéma initial de PresenceKF, conforme à docs/diagrammes/D2-classes.md.
-- Toute évolution ultérieure passe par une nouvelle migration (contrainte C2).

CREATE TABLE promotion (
    id   BIGINT AUTO_INCREMENT PRIMARY KEY,
    nom  VARCHAR(255) NOT NULL
);

CREATE TABLE formateur (
    id   BIGINT AUTO_INCREMENT PRIMARY KEY,
    nom  VARCHAR(255) NOT NULL
);

CREATE TABLE etudiant (
    id            BIGINT AUTO_INCREMENT PRIMARY KEY,
    nom           VARCHAR(255) NOT NULL,
    promotion_id  BIGINT NOT NULL,
    CONSTRAINT fk_etudiant_promotion FOREIGN KEY (promotion_id) REFERENCES promotion (id)
);

CREATE TABLE session_cours (
    id             BIGINT AUTO_INCREMENT PRIMARY KEY,
    titre          VARCHAR(255) NOT NULL,
    promotion_id   BIGINT NOT NULL,
    formateur_id   BIGINT NOT NULL,
    code           VARCHAR(6) NOT NULL UNIQUE,
    ouverture_at   TIMESTAMP NOT NULL,
    expiration_at  TIMESTAMP NOT NULL,
    cloture_at     TIMESTAMP,
    cloturee       BOOLEAN NOT NULL DEFAULT FALSE,
    CONSTRAINT fk_session_promotion FOREIGN KEY (promotion_id) REFERENCES promotion (id),
    CONSTRAINT fk_session_formateur FOREIGN KEY (formateur_id) REFERENCES formateur (id)
);

CREATE TABLE presence (
    id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    session_id   BIGINT NOT NULL,
    etudiant_id  BIGINT NOT NULL,
    source       VARCHAR(20) NOT NULL,
    marquee_at   TIMESTAMP NOT NULL,
    CONSTRAINT fk_presence_session FOREIGN KEY (session_id) REFERENCES session_cours (id),
    CONSTRAINT fk_presence_etudiant FOREIGN KEY (etudiant_id) REFERENCES etudiant (id),
    CONSTRAINT uk_presence_session_etudiant UNIQUE (session_id, etudiant_id)
);

CREATE TABLE exercice (
    id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    session_id   BIGINT NOT NULL,
    etudiant_id  BIGINT NOT NULL,
    lien         VARCHAR(2048) NOT NULL,
    statut       VARCHAR(20) NOT NULL,
    depose_at    TIMESTAMP NOT NULL,
    CONSTRAINT fk_exercice_session FOREIGN KEY (session_id) REFERENCES session_cours (id),
    CONSTRAINT fk_exercice_etudiant FOREIGN KEY (etudiant_id) REFERENCES etudiant (id),
    CONSTRAINT uk_exercice_session_etudiant UNIQUE (session_id, etudiant_id)
);

CREATE TABLE relecture (
    id            BIGINT AUTO_INCREMENT PRIMARY KEY,
    exercice_id   BIGINT NOT NULL UNIQUE,
    relecteur_id  BIGINT NOT NULL,
    note          INTEGER,
    commentaire   VARCHAR(4000),
    statut        VARCHAR(20) NOT NULL,
    assignee_at   TIMESTAMP NOT NULL,
    rendu_at      TIMESTAMP,
    CONSTRAINT fk_relecture_exercice FOREIGN KEY (exercice_id) REFERENCES exercice (id),
    CONSTRAINT fk_relecture_relecteur FOREIGN KEY (relecteur_id) REFERENCES etudiant (id),
    CONSTRAINT ck_relecture_note CHECK (note IS NULL OR (note >= 0 AND note <= 20))
);

CREATE TABLE tentative_code (
    id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    etudiant_id  BIGINT NOT NULL,
    session_id   BIGINT,
    at           TIMESTAMP NOT NULL,
    succes       BOOLEAN NOT NULL,
    CONSTRAINT fk_tentative_etudiant FOREIGN KEY (etudiant_id) REFERENCES etudiant (id)
);

CREATE INDEX idx_presence_session ON presence (session_id);
CREATE INDEX idx_exercice_session ON exercice (session_id);
CREATE INDEX idx_relecture_relecteur ON relecture (relecteur_id);
CREATE INDEX idx_tentative_etudiant_at ON tentative_code (etudiant_id, at);
