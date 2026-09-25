package com.kfokam.presencekf.error;

import org.springframework.http.HttpStatus;

/**
 * Exception métier portant directement le code HTTP et le code d'erreur
 * stable attendus par le contrat (api/contrat.yaml). Interceptée une seule
 * fois par {@link GlobalExceptionHandler}.
 */
public class ApiException extends RuntimeException {

    private final HttpStatus status;
    private final String code;

    public ApiException(HttpStatus status, String code, String message) {
        super(message);
        this.status = status;
        this.code = code;
    }

    public static ApiException champManquant(String message) {
        return new ApiException(HttpStatus.BAD_REQUEST, "CHAMP_MANQUANT", message);
    }

    public static ApiException codeInconnu() {
        return new ApiException(HttpStatus.BAD_REQUEST, "CODE_INCONNU", "Ce code de présence n'existe pas.");
    }

    public static ApiException codeExpire() {
        return new ApiException(HttpStatus.GONE, "CODE_EXPIRE", "Le code de présence a expiré.");
    }

    public static ApiException dejaPresent() {
        return new ApiException(HttpStatus.CONFLICT, "DEJA_PRESENT", "Cet étudiant est déjà présent à cette session.");
    }

    public static ApiException lienInvalide() {
        return new ApiException(HttpStatus.BAD_REQUEST, "LIEN_INVALIDE", "Le lien fourni n'est pas une URL valide.");
    }

    public static ApiException exerciceDejaDepose() {
        return new ApiException(HttpStatus.CONFLICT, "EXERCICE_DEJA_DEPOSE", "Un exercice a déjà été déposé pour cette session.");
    }

    public static ApiException noteInvalide() {
        return new ApiException(HttpStatus.BAD_REQUEST, "NOTE_INVALIDE", "La note doit être un entier compris entre 0 et 20.");
    }

    public static ApiException autoRelecture() {
        return new ApiException(HttpStatus.FORBIDDEN, "AUTO_RELECTURE", "Un étudiant ne peut pas relire son propre exercice.");
    }

    public static ApiException relectureDejaRendue() {
        return new ApiException(HttpStatus.CONFLICT, "RELECTURE_DEJA_RENDUE", "Cette relecture ne peut plus être modifiée.");
    }

    public static ApiException promotionInconnue() {
        return new ApiException(HttpStatus.NOT_FOUND, "PROMOTION_INCONNUE", "Cette promotion n'existe pas.");
    }

    public static ApiException sessionInconnue() {
        return new ApiException(HttpStatus.NOT_FOUND, "SESSION_INCONNUE", "Cette session n'existe pas.");
    }

    public static ApiException sessionDejaCloturee() {
        return new ApiException(HttpStatus.CONFLICT, "SESSION_DEJA_CLOTUREE", "Cette session est déjà clôturée.");
    }

    public static ApiException sessionCloturee() {
        return new ApiException(HttpStatus.CONFLICT, "SESSION_CLOTUREE", "Cette session est clôturée.");
    }

    public static ApiException accesRefuse() {
        return new ApiException(HttpStatus.FORBIDDEN, "ACCES_REFUSE", "Accès refusé.");
    }

    public static ApiException exerciceInconnu() {
        return new ApiException(HttpStatus.NOT_FOUND, "EXERCICE_INCONNU", "Cet exercice n'existe pas.");
    }

    public static ApiException relectureInconnue() {
        return new ApiException(HttpStatus.NOT_FOUND, "RELECTURE_INCONNUE", "Cette relecture n'existe pas.");
    }

    public static ApiException etudiantInconnu() {
        return new ApiException(HttpStatus.NOT_FOUND, "ETUDIANT_INCONNU", "Cet étudiant n'existe pas.");
    }

    public static ApiException remplacementImpossible() {
        return new ApiException(HttpStatus.CONFLICT, "REMPLACEMENT_IMPOSSIBLE", "Le lien ne peut plus être remplacé : la relecture a commencé.");
    }

    public static ApiException tropDeTentatives(long secondesRestantes) {
        return new ApiException(HttpStatus.TOO_MANY_REQUESTS, "TROP_DE_TENTATIVES",
                "Trop de codes invalides. Réessayez dans " + secondesRestantes + " secondes.");
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getCode() {
        return code;
    }
}
