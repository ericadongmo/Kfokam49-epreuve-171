-- Données de démonstration (ENF6) : rechargées à chaque démarrage puisque la
-- base H2 est en mémoire. Une session fraîche est ouverte au démarrage pour
-- que son code de présence soit toujours valable 15 minutes.

INSERT INTO promotion (id, nom) VALUES (1, 'KFOKAM48 — Promotion Démo');

INSERT INTO formateur (id, nom) VALUES (1, 'Formateur Démo');

INSERT INTO etudiant (id, nom, promotion_id) VALUES
    (1, 'Adama Traoré', 1),
    (2, 'Bianca Nguema', 1),
    (3, 'Carlos Mendes', 1),
    (4, 'Diane Fotso', 1),
    (5, 'Eliott Nkolo', 1),
    (6, 'Fatou Diallo', 1),
    (7, 'Gaspard Mbida', 1),
    (8, 'Hélène Tchoua', 1),
    (9, 'Ismaël Ouedraogo', 1),
    (10, 'Julie Ateba', 1),
    (11, 'Kevin Sango', 1),
    (12, 'Laura Kamdem', 1);

INSERT INTO session_cours (id, titre, promotion_id, formateur_id, code, ouverture_at, expiration_at, cloture_at, cloturee)
VALUES (1, 'Séance de démonstration', 1, 1, 'DEMO01', CURRENT_TIMESTAMP, DATEADD('MINUTE', 15, CURRENT_TIMESTAMP), NULL, FALSE);

-- Présences : 6 étudiants sur 12, marquées à l'ouverture de la session.
INSERT INTO presence (session_id, etudiant_id, source, marquee_at) VALUES
    (1, 1, 'ETUDIANT', CURRENT_TIMESTAMP),
    (1, 2, 'ETUDIANT', CURRENT_TIMESTAMP),
    (1, 3, 'ETUDIANT', CURRENT_TIMESTAMP),
    (1, 4, 'ETUDIANT', CURRENT_TIMESTAMP),
    (1, 5, 'FORMATEUR', CURRENT_TIMESTAMP),
    (1, 6, 'ETUDIANT', CURRENT_TIMESTAMP);

-- Un exercice déposé par l'étudiant 1, relu par l'étudiant 2 (relecture en attente).
INSERT INTO exercice (id, session_id, etudiant_id, lien, statut, depose_at)
VALUES (1, 1, 1, 'https://github.com/demo/exercice-adama', 'EN_ATTENTE', CURRENT_TIMESTAMP);

INSERT INTO relecture (id, exercice_id, relecteur_id, note, commentaire, statut, assignee_at, rendu_at)
VALUES (1, 1, 2, NULL, NULL, 'EN_ATTENTE', CURRENT_TIMESTAMP, NULL);

-- Un second exercice déjà relu, pour illustrer une moyenne non nulle.
INSERT INTO exercice (id, session_id, etudiant_id, lien, statut, depose_at)
VALUES (2, 1, 3, 'https://github.com/demo/exercice-carlos', 'RELU', CURRENT_TIMESTAMP);

INSERT INTO relecture (id, exercice_id, relecteur_id, note, commentaire, statut, assignee_at, rendu_at)
VALUES (2, 2, 4, 16, 'Bon travail, quelques tests manquants.', 'RENDUE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Les étudiants 5 à 12 n'ont ni exercice ni note : ENF6 (« au moins un étudiant sans note »).

-- Les INSERT ci-dessus fixent des id explicites ; H2 n'avance pas seul la
-- séquence IDENTITY dans ce cas (elle resterait à 1). Sans ce redémarrage,
-- la première ligne créée par l'application (ex. POST /api/sessions)
-- entrerait en collision avec l'id 1 déjà utilisé par les données de démo.
ALTER TABLE promotion ALTER COLUMN id RESTART WITH 2;
ALTER TABLE formateur ALTER COLUMN id RESTART WITH 2;
ALTER TABLE etudiant ALTER COLUMN id RESTART WITH 13;
ALTER TABLE session_cours ALTER COLUMN id RESTART WITH 2;
ALTER TABLE exercice ALTER COLUMN id RESTART WITH 3;
ALTER TABLE relecture ALTER COLUMN id RESTART WITH 3;

