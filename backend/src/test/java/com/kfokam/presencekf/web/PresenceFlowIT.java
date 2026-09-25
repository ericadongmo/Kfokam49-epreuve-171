package com.kfokam.presencekf.web;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Test d'intégration bout en bout (B6) : ouverture de session, marquage de
 * présence, refus de double présence au format d'erreur imposé (ENF3), dépôt
 * d'exercice, lecture du tableau formateur.
 */
@SpringBootTest
@AutoConfigureMockMvc
class PresenceFlowIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void parcours_complet_presence_puis_exercice_puis_tableau() throws Exception {
        String reponseOuverture = mockMvc.perform(post("/api/sessions")
                        .contentType("application/json")
                        .content("""
                                { "titre": "Séance de test", "promotionId": 1 }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.code").isNotEmpty())
                .andExpect(jsonPath("$.expirationAt").isNotEmpty())
                .andReturn().getResponse().getContentAsString();

        JsonNode session = objectMapper.readTree(reponseOuverture);
        String code = session.get("code").asText();
        long sessionId = session.get("id").asLong();

        mockMvc.perform(post("/api/presences")
                        .contentType("application/json")
                        .content("{ \"code\": \"" + code + "\", \"etudiantId\": 7 }"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.source").value("ETUDIANT"));

        // EF4 : double présence refusée, au format { code, message } (ENF3).
        mockMvc.perform(post("/api/presences")
                        .contentType("application/json")
                        .content("{ \"code\": \"" + code + "\", \"etudiantId\": 7 }"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.code").value("DEJA_PRESENT"))
                .andExpect(jsonPath("$.message").isNotEmpty());

        mockMvc.perform(post("/api/exercices")
                        .contentType("application/json")
                        .content("{ \"sessionId\": " + sessionId + ", \"etudiantId\": 7, \"lien\": \"https://example.com/exo\" }"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.statut").isNotEmpty());

        mockMvc.perform(get("/api/tableau").param("promotionId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[?(@.etudiantId == 7)].exercicesDeposes").value(org.hamcrest.Matchers.hasItem(1)));
    }

    @Test
    void promotion_inconnue_renvoie_le_format_d_erreur_impose() throws Exception {
        mockMvc.perform(get("/api/tableau").param("promotionId", "999999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("PROMOTION_INCONNUE"))
                .andExpect(jsonPath("$.message").isNotEmpty());
    }
}
