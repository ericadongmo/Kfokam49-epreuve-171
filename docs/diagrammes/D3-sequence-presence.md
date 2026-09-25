# D3 — Séquence : « marquer sa présence »

**Source :** `api/contrat.yaml` — `POST /api/presences`.
**Codes HTTP couverts :** 201 (nominal), 410 (CODE_EXPIRE), 409 (DEJA_PRESENT), 400 (CODE_INCONNU).

```mermaid
sequenceDiagram
    autonumber
    actor E as Étudiant
    participant F as Front Next.js
    participant C as PresenceController
    participant S as PresenceService
    participant R as PresenceRepository
    participant SR as SessionRepository

    E->>F: saisit le code de présence
    F->>C: POST /api/presences { code, etudiantId }
    C->>C: validation @Valid

    alt champ manquant ou invalide
        C-->>F: 400 { code: "REQUETE_INVALIDE" }
    else requête valide
        C->>S: enregistrer(code, etudiantId)
        S->>SR: findByCode(code)

        alt code inconnu
            SR-->>S: Optional.empty()
            S-->>C: CodeInconnuException
            C-->>F: 400 { code: "CODE_INCONNU" }

        else code connu
            SR-->>S: Session

            alt code expiré (RG1, Q2)
                S->>S: maintenant > expirationAt
                S-->>C: CodeExpireException
                C-->>F: 410 { code: "CODE_EXPIRE" }

            else session clôturée (RG16, Q3)
                S->>S: session.cloturee == true
                S-->>C: SessionClotureeException
                C-->>F: 400 { code: "SESSION_CLOTUREE" }

            else étudiant déjà présent (RG13, Q3)
                S->>R: existsBySessionIdAndEtudiantId()
                R-->>S: true
                S-->>C: DejaPresentException
                C-->>F: 409 { code: "DEJA_PRESENT" }

            else cas nominal
                S->>R: save(new Presence(source=ETUDIANT))
                R-->>S: Presence
                S-->>C: Presence
                C-->>F: 201 { id, sessionId, etudiantId, source }
            end
        end
    end

    F-->>E: affiche le résultat
```

## Correspondance avec le contrat

| Cas | Code HTTP | Corps |
|---|---|---|
| Nominal | `201` | `{ id, sessionId, etudiantId, source }` |
| Code inconnu | `400` | `{ code: "CODE_INCONNU", message }` |
| Champ manquant | `400` | `{ code: "REQUETE_INVALIDE", message }` |
| Code expiré | `410` | `{ code: "CODE_EXPIRE", message }` |
| Déjà présent | `409` | `{ code: "DEJA_PRESENT", message }` |
| Session clôturée | `400` | `{ code: "SESSION_CLOTUREE", message }` |