import Link from "next/link";

export default function Accueil() {
  return (
    <div className="accueil-liens">
      <p>Trois écrans, un cycle de séance complet (F2).</p>
      <Link href="/formateur">
        <strong>Formateur</strong>
        Ouvrir une session, suivre les présences, clôturer, consulter le tableau de la promotion.
      </Link>
      <Link href="/etudiant">
        <strong>Étudiant</strong>
        Marquer sa présence avec le code, déposer ou remplacer le lien de son exercice, voir sa note.
      </Link>
      <Link href="/relecteur">
        <strong>Relecteur</strong>
        Consulter les exercices qui me sont assignés, rendre ou corriger une note et un commentaire.
      </Link>
    </div>
  );
}
