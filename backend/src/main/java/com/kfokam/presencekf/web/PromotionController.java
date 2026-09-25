package com.kfokam.presencekf.web;

import com.kfokam.presencekf.service.PromotionService;
import com.kfokam.presencekf.web.dto.PromotionResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class PromotionController {

    private final PromotionService promotionService;

    public PromotionController(PromotionService promotionService) {
        this.promotionService = promotionService;
    }

    @GetMapping("/api/promotions")
    public List<PromotionResponse> lister() {
        return promotionService.lister();
    }
}
