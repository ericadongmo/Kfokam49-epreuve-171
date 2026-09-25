type Type = "erreur" | "succes" | "info";

export default function Banniere({ type, texte }: { type: Type; texte: string | null }) {
  if (!texte) {
    return null;
  }
  return (
    <p role={type === "erreur" ? "alert" : "status"} className={`banniere banniere--${type}`}>
      {texte}
    </p>
  );
}
