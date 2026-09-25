# Changelog — PresenceKF

Format inspiré de [Keep a Changelog](https://keepachangelog.com/fr/).

## [0.1.0] — Première version (Must + Should + Could)

### Ajouté

- Backend Spring Boot 3 / Java 17, wrapper `mvnw` commité, base H2 en
  mémoire, migrations Flyway (`V1__init.sql` conforme à D2,
  `V2__donnees_demo.sql` — ENF6).
- API conforme à `api/contrat.yaml` : les 5 opérations imposées, plus les
  opérations libres (clôture de session, ajout manuel de présence,
  consultation de note, résolution de session par code, remplacement de
  lien, listes promotions/étudiants/relectures).
- Frontend Next.js avec les trois écrans imposés (F2) : formateur, étudiant,
  relecteur.
- Toutes les exigences fonctionnelles Must (EF1-EF13, issues #1 à #9),
  Should (EF14-EF16, issues #10 à #12) et Could (EF17, issue #13).
- Tests : unitaires sur RG2/RG4/RG5 (tirage du relecteur) et RG15 (blocage
  après 5 échecs), intégration bout en bout sur le parcours complet et sur
  le format d'erreur imposé (B6).

### Corrigé pendant le développement

- La migration de données de démo insérait des identifiants explicites sans
  faire redémarrer les séquences `IDENTITY` de H2 : la première session
  créée par l'application entrait en collision avec l'id 1 des données de
  démo (`PRIMARY KEY violation`). Corrigé par des `ALTER TABLE ... RESTART
  WITH` à la fin de `V2__donnees_demo.sql`.
- Le blocage après 5 codes invalides (RG15) ne se déclenchait jamais : la
  méthode `@Transactional` qui enregistre l'échec était annulée par le
  rollback provoqué par l'`ApiException` levée juste après. Corrigé en
  isolant l'enregistrement dans sa propre transaction
  (`Propagation.REQUIRES_NEW`), avec un test d'intégration dédié pour éviter
  la régression (un test unitaire à base de mocks ne pouvait pas voir ce
  bug, propre au comportement transactionnel réel).

### Décisions documentées (voir `docs/CAHIER_DES_CHARGES.md` section 7)

- `RELECTURE_DEJA_RENDUE` (409) est réservé à
  `POST /api/relectures/{id}/commencer` ; sur `POST /api/relectures/{id}`,
  la correction d'une note déjà rendue est autorisée tant que la session
  n'est pas clôturée (RG7/RG12, Q10 prime sur Q15).
- `relecteurId` est un champ additionnel (toléré par le schéma OpenAPI) sur
  `POST /api/relectures/{id}` : l'application n'a pas d'authentification,
  c'est le seul moyen pour le serveur de vérifier RG2 (auto-relecture) et
  l'identité de l'appelant.
