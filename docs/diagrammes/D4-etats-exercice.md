# D4 — États-transitions : cycle de vie d'un exercice (bonus)

**Source :** `docs/CAHIER_DES_CHARGES.md` — sections 3, 4 (EF6–EF15) et 6 (RG4, RG7, RG8, RG9, RG10).

```mermaid
stateDiagram-v2
    [*] --> DEPOSE : EF6 — l'étudiant dépose le lien (Q12)
    DEPOSE --> EN_ATTENTE : EF8 — le système assigne un relecteur (Q6, Q7)
    DEPOSE --> DEPOSE : EF15 — remplacement du lien (Q13, RG10)
    EN_ATTENTE --> EN_ATTENTE : EF15 — remplacement du lien (Q13)
    EN_ATTENTE --> EN_COURS : le relecteur ouvre l'écran (RG10)
    EN_COURS --> RELU : EF9 — note + commentaire (Q9, RG3)
    RELU --> RELU : EF10 — correction avant clôture (Q10)
    RELU --> FIGE : EF16 — clôture de la session (RG12)
    EN_ATTENTE --> FIGE : EF16 — clôture sans relecture (Q11, RG8)
    EN_COURS --> FIGE : EF16 — clôture pendant relecture
    FIGE --> [*]
```

## Lecture

| État | Signification |
|---|---|
| `DEPOSE` | Lien déposé, aucun relecteur assigné (cas « aucun présent ») |
| `EN_ATTENTE` | Relecteur assigné, note pas encore rendue |
| `EN_COURS` | Relecteur a ouvert l'écran, lien non remplaçable (Q13) |
| `RELU` | Note + commentaire rendus, modifiables jusqu'à clôture (Q10) |
| `FIGE` | Session clôturée, note définitive (RG12) |