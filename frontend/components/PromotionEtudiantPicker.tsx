"use client";

import { useEffect, useState } from "react";
import { api, ApiError } from "@/lib/api";
import type { Etudiant, Promotion } from "@/lib/types";

interface Props {
  onSelect: (etudiant: Etudiant | null) => void;
}

/**
 * EF... « choisir son nom dans une liste, sans mot de passe » : sélection de
 * la promotion puis de l'étudiant, réutilisée par les écrans étudiant et
 * relecteur (le relecteur est un étudiant, section 2 du cahier des charges).
 */
export default function PromotionEtudiantPicker({ onSelect }: Props) {
  const [promotions, setPromotions] = useState<Promotion[]>([]);
  const [etudiants, setEtudiants] = useState<Etudiant[]>([]);
  const [promotionId, setPromotionId] = useState<number | "">("");
  const [etudiantId, setEtudiantId] = useState<number | "">("");
  const [erreur, setErreur] = useState<string | null>(null);
  const [chargement, setChargement] = useState(true);

  useEffect(() => {
    api.promotions
      .lister()
      .then((liste) => {
        setPromotions(liste);
        if (liste.length === 1) {
          setPromotionId(liste[0].id);
        }
      })
      .catch((e: ApiError) => setErreur(e.message))
      .finally(() => setChargement(false));
  }, []);

  useEffect(() => {
    if (promotionId === "") {
      // eslint-disable-next-line react-hooks/set-state-in-effect -- réinitialisation liée au changement de promotionId
      setEtudiants([]);
      return;
    }
    api.etudiants
      .lister(promotionId)
      .then(setEtudiants)
      .catch((e: ApiError) => setErreur(e.message));
  }, [promotionId]);

  useEffect(() => {
    const etudiant = etudiants.find((e) => e.id === etudiantId) ?? null;
    onSelect(etudiant);
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [etudiantId, etudiants]);

  if (chargement) {
    return <p role="status">Chargement des promotions…</p>;
  }

  return (
    <div className="champ-groupe">
      {erreur && <p className="erreur">{erreur}</p>}

      <label htmlFor="promotion">Ma promotion</label>
      <select
        id="promotion"
        value={promotionId}
        onChange={(e) => {
          setPromotionId(e.target.value ? Number(e.target.value) : "");
          setEtudiantId("");
        }}
      >
        <option value="">— choisir —</option>
        {promotions.map((p) => (
          <option key={p.id} value={p.id}>
            {p.nom}
          </option>
        ))}
      </select>

      <label htmlFor="etudiant">Mon nom</label>
      <select
        id="etudiant"
        value={etudiantId}
        disabled={promotionId === ""}
        onChange={(e) => setEtudiantId(e.target.value ? Number(e.target.value) : "")}
      >
        <option value="">— choisir —</option>
        {etudiants.map((e) => (
          <option key={e.id} value={e.id}>
            {e.nom}
          </option>
        ))}
      </select>
    </div>
  );
}
