package com.kfokam.presencekf.service;

import com.kfokam.presencekf.domain.Session;
import com.kfokam.presencekf.error.ApiException;
import com.kfokam.presencekf.repository.PromotionRepository;
import com.kfokam.presencekf.repository.SessionRepository;
import com.kfokam.presencekf.web.dto.ClotureResponse;
import com.kfokam.presencekf.web.dto.OuvrirSessionRequest;
import com.kfokam.presencekf.web.dto.SessionResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;

@Service
public class SessionService {

    /** RG1 : un code de présence expire 15 minutes après l'ouverture de la session. */
    static final Duration DUREE_VALIDITE_CODE = Duration.ofMinutes(15);

    private static final Long FORMATEUR_DEMO_ID = 1L;

    private final SessionRepository sessionRepository;
    private final PromotionRepository promotionRepository;
    private final CodeGenerator codeGenerator;

    public SessionService(SessionRepository sessionRepository, PromotionRepository promotionRepository, CodeGenerator codeGenerator) {
        this.sessionRepository = sessionRepository;
        this.promotionRepository = promotionRepository;
        this.codeGenerator = codeGenerator;
    }

    @Transactional
    public SessionResponse ouvrir(OuvrirSessionRequest request) {
        if (!promotionRepository.existsById(request.promotionId())) {
            throw ApiException.promotionInconnue();
        }

        Session session = new Session();
        session.setTitre(request.titre());
        session.setPromotionId(request.promotionId());
        session.setFormateurId(FORMATEUR_DEMO_ID);
        session.setCode(genererCodeUnique());

        Instant ouverture = Instant.now();
        session.setOuvertureAt(ouverture);
        session.setExpirationAt(ouverture.plus(DUREE_VALIDITE_CODE));
        session.setCloturee(false);

        Session sauvegardee = sessionRepository.save(session);
        return toResponse(sauvegardee);
    }

    @Transactional(readOnly = true)
    public Session trouver(Long id) {
        return sessionRepository.findById(id).orElseThrow(ApiException::sessionInconnue);
    }

    @Transactional(readOnly = true)
    public Session trouverParCode(String code) {
        return sessionRepository.findByCode(code).orElseThrow(ApiException::codeInconnu);
    }

    /** EF16 · RG9, RG12, RG16 : gèle les notes, arrête dépôts et marquages. */
    @Transactional
    public ClotureResponse cloturer(Long id) {
        Session session = trouver(id);
        if (session.isCloturee()) {
            throw ApiException.sessionDejaCloturee();
        }
        session.setCloturee(true);
        session.setClotureAt(Instant.now());
        sessionRepository.save(session);
        return new ClotureResponse(session.getId(), session.isCloturee(), session.getClotureAt());
    }

    private String genererCodeUnique() {
        String code;
        do {
            code = codeGenerator.genererCode();
        } while (sessionRepository.existsByCode(code));
        return code;
    }

    private SessionResponse toResponse(Session session) {
        return new SessionResponse(
                session.getId(),
                session.getTitre(),
                session.getPromotionId(),
                session.getCode(),
                session.getOuvertureAt(),
                session.getExpirationAt(),
                session.isCloturee()
        );
    }
}
