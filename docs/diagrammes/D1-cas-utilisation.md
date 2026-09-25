# D1 — Diagramme de cas d'utilisation

**Source :** `docs/CAHIER_DES_CHARGES.md` — sections 2 (acteurs) et 4 (exigences fonctionnelles).

```mermaid
flowchart LR
    Formateur(("Formateur"))
    Etudiant(("Etudiant"))
    Relecteur(("Relecteur<br/>étudiant assigné"))

    subgraph PresenceKF["PresenceKF — cas d'utilisation"]
        UC1["Ouvrir une session<br/>et obtenir un code"]
        UC2["Consulter le tableau<br/>de la promotion"]
        UC3["Ajouter une présence<br/>manuellement"]
        UC4["Clôturer une session"]

        UC5["Marquer sa présence<br/>avec un code"]
        UC6["Déposer le lien<br/>de son exercice"]
        UC7["Remplacer le lien<br/>de son exercice"]
        UC8["Consulter sa note<br/>et le commentaire"]

        UC9["Rendre une note<br/>et un commentaire"]
        UC10["Corriger sa note<br/>avant clôture"]
    end

    Formateur --> UC1
    Formateur --> UC2
    Formateur --> UC3
    Formateur --> UC4

    Etudiant --> UC5
    Etudiant --> UC6
    Etudiant --> UC7
    Etudiant --> UC8

    Relecteur --> UC9
    Relecteur --> UC10
```

## Légende des renvois

| Cas                           | Exigence      | Règle                   |
| ----------------------------- | ------------- | ----------------------- |
| Ouvrir une session            | EF1           | RG1                     |
| Consulter le tableau          | EF12, EF13    | —                       |
| Ajouter une présence manuelle | EF5           | RG11 (Q14)              |
| Clôturer une session          | EF16          | RG12                    |
| Marquer sa présence           | EF2, EF3, EF4 | RG1, RG13, RG15, RG16   |
| Déposer le lien               | EF6, EF7      | RG9, RG14               |
| Remplacer le lien             | EF15          | RG10 (Q13)              |
| Consulter sa note             | EF14          | RG6 (Q8)                |
| Rendre une note               | EF9, EF11     | RG2, RG3, RG4, RG5      |
| Corriger sa note              | EF10          | RG7 (Q10 prime sur Q15) |
