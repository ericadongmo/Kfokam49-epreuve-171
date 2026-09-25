package com.kfokam.presencekf.error;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Point d'entrée unique qui traduit chaque exception en {@code { code, message }}
 * (contrainte C4 / ENF3). Aucune stack trace ne quitte jamais ce handler.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ErreurDto> handleApiException(ApiException ex) {
        return ResponseEntity.status(ex.getStatus()).body(new ErreurDto(ex.getCode(), ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErreurDto> handleValidation(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getFieldErrors().stream()
                .findFirst()
                .map(fe -> fe.getField() + " : " + fe.getDefaultMessage())
                .orElse("Un champ requis est manquant ou invalide.");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErreurDto("CHAMP_MANQUANT", message));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErreurDto> handleNotReadable(HttpMessageNotReadableException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErreurDto("CHAMP_MANQUANT", "Le corps de la requête est absent ou mal formé."));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErreurDto> handleUnexpected(Exception ex) {
        log.error("Erreur interne non gérée", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErreurDto("ERREUR_INTERNE", "Une erreur inattendue est survenue."));
    }
}
