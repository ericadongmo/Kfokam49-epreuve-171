import type { Metadata } from "next";
import Nav from "@/components/Nav";
import "./globals.css";

export const metadata: Metadata = {
  title: "PresenceKF",
  description: "Présences, exercices et relectures par les pairs — KFOKAM48",
};

export default function RootLayout({ children }: { children: React.ReactNode }) {
  return (
    <html lang="fr">
      <body>
        <header className="entete">
          <h1>PresenceKF</h1>
          <Nav />
        </header>
        <main>{children}</main>
      </body>
    </html>
  );
}
