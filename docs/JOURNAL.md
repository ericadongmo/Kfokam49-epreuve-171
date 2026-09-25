# Journal de bord — <matricule>

> Une entrée **par étape**, écrite **au moment où tu la termines**, pas à la fin de la journée.
> Trois lignes suffisent. Un journal rédigé d'un bloc juste avant de soumettre se repère
> immédiatement dans l'historique Git et ne compte pas.

Chaque entrée répond aux trois mêmes questions :

- **Fait** — ce que tu viens de terminer
- **Bloqué** — ce qui t'a coûté du temps, et combien
- **IA** — ce que tu lui as demandé, et **comment tu as vérifié sa réponse**

---

## Étape 1 — Analyse et conception

**Fait :** cahier des charges (9 exigences fonctionnelles, 12 règles de gestion), les trois diagrammes en Mermaid, 11 issues créées, contrat d'API complété, commit `[JALON] analyse` poussé.

**Bloqué :** 12 min sur la contradiction entre Q10 et Q15. Tranchée en faveur de Q10 : Q11 décrit un usage réel et concret du formateur, Q15 n'est qu'une intention générale. Noté en section 7.

**IA :** m'a proposé un découpage en 18 tickets, j'en ai retenu 11. Les autres étaient des tâches techniques (« créer l'entité », « configurer Flyway »), pas des résultats utilisateur. Vérifié en relisant chaque titre : est-ce que le client le comprendrait ?

---

## Étape 2 — Première version

**Fait :** backend Spring Boot complet (13 issues, Must à Could) conforme à `api/contrat.yaml`, frontend Next.js avec les trois écrans, tests unitaires + intégration verts, `npm run build` et `./mvnw test` passent.

**Bloqué :** ~20 min sur deux bugs trouvés en testant l'API réelle après le build (pas visibles en tests unitaires mockés) : (1) les id explicites de `V2__donnees_demo.sql` ne faisaient pas avancer les séquences IDENTITY de H2, la première session créée entrait en collision avec l'id 1 de démo ; (2) le blocage RG15 après 5 codes invalides ne se déclenchait jamais car le rollback transactionnel de l'`ApiException` annulait l'enregistrement de l'échec lui-même. Corrigés, tests de non-régression ajoutés (voir `CHANGELOG.md`).

**IA :** a généré l'implémentation initiale des 13 issues (backend + frontend) à partir du cahier des charges et du contrat déjà écrits à l'étape 1. Vérifié par : relecture du contrat un endpoint à la fois, exécution de la suite de tests, puis un parcours API complet à la main (`curl`) rejouant chaque critère d'acceptation des 13 issues sur le backend réellement démarré — c'est ce parcours, pas les tests unitaires, qui a révélé les deux bugs ci-dessus. Écran par écran vérifié par rendu serveur (`curl` sur chaque route) et `npm run build`/`lint` ; pas de clic-à-clic dans un vrai navigateur, aucun outil de navigateur n'étant disponible dans cet environnement — à refaire manuellement avant la démo.

---

## Étape 3 — Enveloppe

**Fait :**

**Bloqué :**

**IA :**

**Ce que j'ai sorti du périmètre pour absorber le changement, et pourquoi :**

---

## Étape 4 — Version finale

**Fait :**

**Bloqué :**

**IA :**

---

## Étape 5 — Épreuve Git

**Fait :**

**Bloqué :**

**IA :**

---

## Étape 6 — Soumission

**Fait :**

**Ce que je referais autrement avec une journée de plus :**
