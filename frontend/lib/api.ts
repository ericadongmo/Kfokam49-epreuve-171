import type {
  Cloture,
  Etudiant,
  Exercice,
  LigneTableau,
  NoteExercice,
  Presence,
  PresenceAvecNom,
  Promotion,
  Relecture,
  RelectureAssignee,
  SessionCours,
} from "./types";

/**
 * Couche d'appels API dédiée (F3) : tous les composants passent par ici,
 * jamais par `fetch` directement. Aucune règle métier n'est recalculée côté
 * frontend (la moyenne, par exemple, vient toujours de `GET /api/tableau`).
 */
const BASE_URL = process.env.NEXT_PUBLIC_API_URL ?? "http://localhost:8080";

export class ApiError extends Error {
  code: string;

  constructor(code: string, message: string) {
    super(message);
    this.code = code;
  }
}

async function appel<T>(chemin: string, options: RequestInit = {}): Promise<T> {
  const reponse = await fetch(`${BASE_URL}${chemin}`, {
    ...options,
    headers: { "Content-Type": "application/json", ...options.headers },
  });

  if (!reponse.ok) {
    let code = "ERREUR_INTERNE";
    let message = "Une erreur inattendue est survenue.";
    try {
      const corps = await reponse.json();
      code = corps.code ?? code;
      message = corps.message ?? message;
    } catch {
      // le corps n'était pas du JSON exploitable : on garde le message générique
    }
    throw new ApiError(code, message);
  }

  if (reponse.status === 204) {
    return undefined as T;
  }
  return (await reponse.json()) as T;
}

export const api = {
  promotions: {
    lister: () => appel<Promotion[]>("/api/promotions"),
  },

  etudiants: {
    lister: (promotionId: number) => appel<Etudiant[]>(`/api/etudiants?promotionId=${promotionId}`),
    relectures: (etudiantId: number) => appel<RelectureAssignee[]>(`/api/etudiants/${etudiantId}/relectures`),
  },

  sessions: {
    ouvrir: (titre: string, promotionId: number) =>
      appel<SessionCours>("/api/sessions", {
        method: "POST",
        body: JSON.stringify({ titre, promotionId }),
      }),
    trouver: (id: number) => appel<SessionCours>(`/api/sessions/${id}`),
    trouverParCode: (code: string) => appel<SessionCours>(`/api/sessions/code/${encodeURIComponent(code)}`),
    presences: (id: number) => appel<PresenceAvecNom[]>(`/api/sessions/${id}/presences`),
    ajouterPresenceManuelle: (sessionId: number, etudiantId: number) =>
      appel<Presence>(`/api/sessions/${sessionId}/presences/manuel`, {
        method: "POST",
        body: JSON.stringify({ etudiantId }),
      }),
    cloturer: (id: number) => appel<Cloture>(`/api/sessions/${id}/cloture`, { method: "POST" }),
  },

  presences: {
    marquer: (code: string, etudiantId: number) =>
      appel<Presence>("/api/presences", {
        method: "POST",
        body: JSON.stringify({ code, etudiantId }),
      }),
  },

  exercices: {
    deposer: (sessionId: number, etudiantId: number, lien: string) =>
      appel<Exercice>("/api/exercices", {
        method: "POST",
        body: JSON.stringify({ sessionId, etudiantId, lien }),
      }),
    trouverPourSessionEtEtudiant: (sessionId: number, etudiantId: number) =>
      appel<Exercice>(`/api/sessions/${sessionId}/exercices/${etudiantId}`),
    remplacerLien: (id: number, etudiantId: number, lien: string) =>
      appel<Exercice>(`/api/exercices/${id}?etudiantId=${etudiantId}`, {
        method: "PUT",
        body: JSON.stringify({ lien }),
      }),
    note: (id: number) => appel<NoteExercice>(`/api/exercices/${id}/note`),
  },

  relectures: {
    commencer: (id: number) => appel<Relecture>(`/api/relectures/${id}/commencer`, { method: "POST" }),
    rendre: (id: number, note: number, commentaire: string, relecteurId: number) =>
      appel<Relecture>(`/api/relectures/${id}`, {
        method: "POST",
        body: JSON.stringify({ note, commentaire, relecteurId }),
      }),
  },

  tableau: {
    pourPromotion: (promotionId: number) => appel<LigneTableau[]>(`/api/tableau?promotionId=${promotionId}`),
  },
};
