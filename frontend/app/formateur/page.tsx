"use client";

import { useEffect, useState } from "react";
import { api, ApiError } from "@/lib/api";
import type { Etudiant, LigneTableau, PresenceAvecNom, Promotion, SessionCours } from "@/lib/types";
import Banniere from "@/components/Banniere";

function formatHeure(iso: string): string {
  return new Date(iso).toLocaleTimeString("fr-FR", { hour: "2-digit", minute: "2-digit", second: "2-digit" });
}

export default function EcranFormateur() {
  const [promotions, setPromotions] = useState<Promotion[]>([]);
  const [promotionId, setPromotionId] = useState<number | "">("");
  const [etudiants, setEtudiants] = useState<Etudiant[]>([]);
  const [titre, setTitre] = useState("Séance du jour");
  const [session, setSession] = useState<SessionCours | null>(null);
  const [presences, setPresences] = useState<PresenceAvecNom[]>([]);
  const [etudiantAjout, setEtudiantAjout] = useState<number | "">("");
  const [tableau, setTableau] = useState<LigneTableau[] | null>(null);

  const [chargementOuverture, setChargementOuverture] = useState(false);
  const [erreur, setErreur] = useState<string | null>(null);
  const [succes, setSucces] = useState<string | null>(null);
  const [maintenant, setMaintenant] = useState(() => Date.now());

  useEffect(() => {
    api.promotions
      .lister()
      .then((liste) => {
        setPromotions(liste);
        if (liste.length === 1) setPromotionId(liste[0].id);
      })
      .catch((e: ApiError) => setErreur(e.message));
  }, []);

  useEffect(() => {
    if (promotionId === "") return;
    api.etudiants.lister(promotionId).then(setEtudiants).catch((e: ApiError) => setErreur(e.message));
  }, [promotionId]);

  useEffect(() => {
    if (!session) return;
    const intervalle = setInterval(() => setMaintenant(Date.now()), 1000);
    return () => clearInterval(intervalle);
  }, [session]);

  async function rafraichirPresences(sessionId: number) {
    try {
      setPresences(await api.sessions.presences(sessionId));
    } catch (e) {
      if (e instanceof ApiError) setErreur(e.message);
    }
  }

  async function ouvrirSession() {
    if (promotionId === "") {
      setErreur("Choisissez une promotion.");
      return;
    }
    setErreur(null);
    setSucces(null);
    setChargementOuverture(true);
    try {
      const nouvelle = await api.sessions.ouvrir(titre, promotionId);
      setSession(nouvelle);
      await rafraichirPresences(nouvelle.id);
    } catch (e) {
      if (e instanceof ApiError) setErreur(e.message);
    } finally {
      setChargementOuverture(false);
    }
  }

  async function ajouterPresenceManuelle() {
    if (!session || etudiantAjout === "") return;
    setErreur(null);
    try {
      await api.sessions.ajouterPresenceManuelle(session.id, etudiantAjout);
      setEtudiantAjout("");
      await rafraichirPresences(session.id);
    } catch (e) {
      if (e instanceof ApiError) setErreur(e.message);
    }
  }

  async function cloturer() {
    if (!session) return;
    setErreur(null);
    try {
      const resultat = await api.sessions.cloturer(session.id);
      setSession({ ...session, cloturee: resultat.cloturee });
      setSucces("Session clôturée : plus aucun dépôt ni marquage n'est accepté.");
    } catch (e) {
      if (e instanceof ApiError) setErreur(e.message);
    }
  }

  async function chargerTableau() {
    if (promotionId === "") return;
    setErreur(null);
    try {
      setTableau(await api.tableau.pourPromotion(promotionId));
    } catch (e) {
      if (e instanceof ApiError) setErreur(e.message);
    }
  }

  const secondesRestantes = session
    ? Math.max(0, Math.floor((new Date(session.expirationAt).getTime() - maintenant) / 1000))
    : 0;
  const expire = session ? secondesRestantes <= 0 : false;

  return (
    <div>
      <h2>Écran formateur</h2>
      <Banniere type="erreur" texte={erreur} />
      <Banniere type="succes" texte={succes} />

      <section className="carte">
        <h2>1. Ouvrir une session</h2>
        <div className="champ-groupe">
          <label htmlFor="promotion-formateur">Promotion</label>
          <select
            id="promotion-formateur"
            value={promotionId}
            onChange={(e) => setPromotionId(e.target.value ? Number(e.target.value) : "")}
          >
            <option value="">— choisir —</option>
            {promotions.map((p) => (
              <option key={p.id} value={p.id}>
                {p.nom}
              </option>
            ))}
          </select>
        </div>
        <div className="champ-groupe">
          <label htmlFor="titre">Titre de la séance</label>
          <input id="titre" type="text" value={titre} onChange={(e) => setTitre(e.target.value)} />
        </div>
        <button onClick={ouvrirSession} disabled={chargementOuverture || promotionId === ""}>
          {chargementOuverture ? "Ouverture…" : "Ouvrir la session"}
        </button>
      </section>

      {session && (
        <section className="carte">
          <h2>2. {session.titre}</h2>
          <p className="code-affiche">{session.code}</p>
          <p>
            {session.cloturee ? (
              <span className="etiquette etiquette--rendue">Session clôturée</span>
            ) : expire ? (
              <span className="etiquette etiquette--attente">Code expiré</span>
            ) : (
              <>
                Expire dans <strong>{secondesRestantes}s</strong> (à {formatHeure(session.expirationAt)})
              </>
            )}
          </p>

          <h3>Présents ({presences.length})</h3>
          <ul>
            {presences.map((p) => (
              <li key={p.etudiantId}>
                {p.nom} — <em>{p.source === "FORMATEUR" ? "ajouté par le formateur" : "code"}</em>
              </li>
            ))}
            {presences.length === 0 && <li>Aucun étudiant présent pour l&apos;instant.</li>}
          </ul>
          <button className="secondaire" onClick={() => rafraichirPresences(session.id)}>
            Actualiser
          </button>

          {!session.cloturee && (
            <div className="champ-groupe" style={{ marginTop: 16 }}>
              <label htmlFor="ajout-manuel">Ajouter une présence manuellement</label>
              <select
                id="ajout-manuel"
                value={etudiantAjout}
                onChange={(e) => setEtudiantAjout(e.target.value ? Number(e.target.value) : "")}
              >
                <option value="">— choisir un étudiant —</option>
                {etudiants.map((e) => (
                  <option key={e.id} value={e.id}>
                    {e.nom}
                  </option>
                ))}
              </select>
              <button onClick={ajouterPresenceManuelle} disabled={etudiantAjout === ""}>
                Ajouter
              </button>
            </div>
          )}

          {!session.cloturee && (
            <button className="secondaire" style={{ marginTop: 16 }} onClick={cloturer}>
              Clôturer la session
            </button>
          )}
        </section>
      )}

      <section className="carte">
        <h2>3. Tableau récapitulatif</h2>
        <button className="secondaire" onClick={chargerTableau} disabled={promotionId === ""}>
          Charger le tableau
        </button>
        {tableau && (
          <table>
            <thead>
              <tr>
                <th>Étudiant</th>
                <th>Présences</th>
                <th>Exercices</th>
                <th>Moyenne</th>
                <th>Relectures en attente</th>
              </tr>
            </thead>
            <tbody>
              {tableau.map((ligne) => (
                <tr key={ligne.etudiantId}>
                  <td>{ligne.nom}</td>
                  <td>{ligne.presences}</td>
                  <td>{ligne.exercicesDeposes}</td>
                  <td>{ligne.moyenne === null ? "—" : ligne.moyenne.toFixed(1)}</td>
                  <td>{ligne.relecturesEnAttente > 0 ? <strong>{ligne.relecturesEnAttente}</strong> : 0}</td>
                </tr>
              ))}
            </tbody>
          </table>
        )}
      </section>
    </div>
  );
}
