"use client";

import { useEffect, useState } from "react";
import { api, ApiError } from "@/lib/api";
import type { Etudiant, RelectureAssignee } from "@/lib/types";
import Banniere from "@/components/Banniere";
import PromotionEtudiantPicker from "@/components/PromotionEtudiantPicker";

const ETIQUETTES: Record<string, string> = {
  EN_ATTENTE: "etiquette--attente",
  EN_COURS: "etiquette--cours",
  RENDUE: "etiquette--rendue",
};

function LigneRelecture({ relecture, relecteurId, onChange }: {
  relecture: RelectureAssignee;
  relecteurId: number;
  onChange: () => void;
}) {
  const [ouverte, setOuverte] = useState(false);
  const [note, setNote] = useState(relecture.note?.toString() ?? "");
  const [commentaire, setCommentaire] = useState(relecture.commentaire ?? "");
  const [erreur, setErreur] = useState<string | null>(null);
  const [enCours, setEnCours] = useState(false);

  async function ouvrir() {
    setErreur(null);
    if (relecture.statut === "EN_ATTENTE") {
      try {
        await api.relectures.commencer(relecture.relectureId);
        onChange();
      } catch (e) {
        if (e instanceof ApiError) setErreur(e.message);
        return;
      }
    }
    setOuverte(true);
  }

  async function envoyer() {
    const noteNombre = Number(note);
    if (!Number.isInteger(noteNombre) || noteNombre < 0 || noteNombre > 20) {
      setErreur("La note doit être un entier entre 0 et 20.");
      return;
    }
    setErreur(null);
    setEnCours(true);
    try {
      await api.relectures.rendre(relecture.relectureId, noteNombre, commentaire, relecteurId);
      setOuverte(false);
      onChange();
    } catch (e) {
      if (e instanceof ApiError) setErreur(e.message);
    } finally {
      setEnCours(false);
    }
  }

  return (
    <li className="carte">
      <p>
        <a href={relecture.lien} target="_blank" rel="noreferrer">
          {relecture.lien}
        </a>
      </p>
      <p>
        <span className={`etiquette ${ETIQUETTES[relecture.statut]}`}>{relecture.statut.replace("_", " ")}</span>
      </p>
      <Banniere type="erreur" texte={erreur} />

      {!ouverte && relecture.statut !== "RENDUE" && <button onClick={ouvrir}>Relire</button>}
      {!ouverte && relecture.statut === "RENDUE" && (
        <div>
          <p>
            Note rendue : <strong>{relecture.note}/20</strong> — {relecture.commentaire}
          </p>
          <button className="secondaire" onClick={() => setOuverte(true)}>
            Corriger
          </button>
        </div>
      )}

      {ouverte && (
        <div className="champ-groupe">
          <label htmlFor={`note-${relecture.relectureId}`}>Note (0 à 20)</label>
          <input
            id={`note-${relecture.relectureId}`}
            type="number"
            min={0}
            max={20}
            step={1}
            value={note}
            onChange={(e) => setNote(e.target.value)}
          />
          <label htmlFor={`commentaire-${relecture.relectureId}`}>Commentaire</label>
          <textarea
            id={`commentaire-${relecture.relectureId}`}
            rows={3}
            value={commentaire}
            onChange={(e) => setCommentaire(e.target.value)}
          />
          <div className="liste-actions">
            <button onClick={envoyer} disabled={enCours}>
              {enCours ? "Envoi…" : "Envoyer la note"}
            </button>
            <button className="secondaire" onClick={() => setOuverte(false)}>
              Annuler
            </button>
          </div>
        </div>
      )}
    </li>
  );
}

export default function EcranRelecteur() {
  const [etudiant, setEtudiant] = useState<Etudiant | null>(null);
  const [relectures, setRelectures] = useState<RelectureAssignee[]>([]);
  const [erreur, setErreur] = useState<string | null>(null);

  async function charger() {
    if (!etudiant) return;
    try {
      setRelectures(await api.etudiants.relectures(etudiant.id));
    } catch (e) {
      if (e instanceof ApiError) setErreur(e.message);
    }
  }

  useEffect(() => {
    // eslint-disable-next-line react-hooks/set-state-in-effect -- synchronisation avec l'API au changement d'étudiant
    charger();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [etudiant]);

  return (
    <div>
      <h2>Écran relecteur</h2>

      <section className="carte">
        <h2>1. Qui suis-je ?</h2>
        <PromotionEtudiantPicker onSelect={setEtudiant} />
      </section>

      {etudiant && (
        <section>
          <Banniere type="erreur" texte={erreur} />
          <h2>2. Mes relectures assignées</h2>
          {relectures.length === 0 && <p>Aucune relecture assignée pour l&apos;instant.</p>}
          <ul style={{ listStyle: "none", padding: 0 }}>
            {relectures.map((r) => (
              <LigneRelecture key={r.relectureId} relecture={r} relecteurId={etudiant.id} onChange={charger} />
            ))}
          </ul>
        </section>
      )}
    </div>
  );
}
