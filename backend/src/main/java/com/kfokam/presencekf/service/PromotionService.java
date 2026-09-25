package com.kfokam.presencekf.service;

import com.kfokam.presencekf.repository.PromotionRepository;
import com.kfokam.presencekf.web.dto.PromotionResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PromotionService {

    private final PromotionRepository promotionRepository;

    public PromotionService(PromotionRepository promotionRepository) {
        this.promotionRepository = promotionRepository;
    }

    @Transactional(readOnly = true)
    public List<PromotionResponse> lister() {
        return promotionRepository.findAll().stream()
                .map(p -> new PromotionResponse(p.getId(), p.getNom()))
                .toList();
    }
}
