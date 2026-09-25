"use client";

import Link from "next/link";
import { usePathname } from "next/navigation";

const LIENS = [
  { href: "/formateur", label: "Formateur" },
  { href: "/etudiant", label: "Étudiant" },
  { href: "/relecteur", label: "Relecteur" },
];

export default function Nav() {
  const chemin = usePathname();
  return (
    <nav className="entete-nav" aria-label="Écrans">
      {LIENS.map((lien) => (
        <Link key={lien.href} href={lien.href} aria-current={chemin === lien.href ? "page" : undefined}>
          {lien.label}
        </Link>
      ))}
    </nav>
  );
}
