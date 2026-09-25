package com.kfokam.presencekf.service;

import com.kfokam.presencekf.error.ApiException;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.URISyntaxException;

@Component
public class UrlValidator {

    public void valider(String lien) {
        try {
            URI uri = new URI(lien);
            if (uri.getScheme() == null || uri.getHost() == null
                    || !(uri.getScheme().equalsIgnoreCase("http") || uri.getScheme().equalsIgnoreCase("https"))) {
                throw ApiException.lienInvalide();
            }
        } catch (URISyntaxException e) {
            throw ApiException.lienInvalide();
        }
    }
}
