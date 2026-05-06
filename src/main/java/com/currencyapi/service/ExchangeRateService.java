package com.currencyapi.service;

import com.currencyapi.dto.ConversionRequest;
import com.currencyapi.dto.ConversionResponse;
import com.currencyapi.dto.ExchangeRateApiResponse;
import com.currencyapi.exception.ApiExterneException;
import com.currencyapi.exception.DeviseInvalideException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.time.LocalDateTime;
import java.util.Map;


// Service principal gerant la conversion de devises.
 // Utilise Spring WebClient pour appeler l'API externe Exchangerate-API.

@Service
@RequiredArgsConstructor
@Slf4j
public class ExchangeRateService {

    private final WebClient exchangeRateWebClient;

    @Value("${app.exchangerate.api-key}")
    private String apiKey;


    // Convertit un montant d'une devise a une autre.

    public ConversionResponse convertir(ConversionRequest request) {
        String source = request.getDeviseSource().toUpperCase();
        String cible = request.getDeviseCible().toUpperCase();
        Double montant = request.getMontant();

        log.info("Conversion demandee : {} {} -> {}", montant, source, cible);

        // Recuperer les taux depuis l'API externe
         ExchangeRateApiResponse apiResponse = getTauxDeChange(source);

        // Verifier que la devise cible existe
        Map<String, Double> taux = apiResponse.getConversionRates();
        if (taux == null || !taux.containsKey(cible)) {
            throw new DeviseInvalideException(cible);
        }

        double tauxApplique = taux.get(cible);
        double montantConverti = Math.round(montant * tauxApplique * 100.0) / 100.0;

        log.info("Conversion reussie : {} {} = {} {} (taux: {})",
                montant, source, montantConverti, cible, tauxApplique);

        return ConversionResponse.builder()
                .deviseSource(source)
                .deviseCible(cible)
                .montantOriginal(montant)
                .montantConverti(montantConverti)
                .tauxDeChange(tauxApplique)
                .dateConversion(LocalDateTime.now())
                .build();
    }


     // Recupere tous les taux de change pour une devise donnee.
    public Map<String, Double> getTousLesTaux(String devise) {
        String code = devise.toUpperCase();
        log.info("Recuperation des taux pour : {}", code);
        ExchangeRateApiResponse response = getTauxDeChange(code);
        return response.getConversionRates();
    }


     //Appelle l'API externe Exchangerate-API pour obtenir les taux d'une devise.

    private ExchangeRateApiResponse getTauxDeChange(String deviseBase) {
        try {
            ExchangeRateApiResponse response = exchangeRateWebClient
                    .get()
                    .uri("/{apiKey}/latest/{base}", apiKey, deviseBase)
                    .retrieve()
                    .bodyToMono(ExchangeRateApiResponse.class)
                    .block();

            if (response == null) {
                throw new ApiExterneException("Reponse vide de l'API externe");
            }

            // Verifier le statut de la reponse
            if (!"success".equals(response.getResult())) {
                String errorType = response.getErrorType();
                if ("unknown-code".equals(errorType) || "invalid-key".equals(errorType)) {
                    throw new DeviseInvalideException(deviseBase);
                }
                throw new ApiExterneException("Erreur API : " + errorType);
            }

            return response;

        } catch (DeviseInvalideException e) {
            throw e;
        } catch (WebClientResponseException e) {
            log.error("Erreur HTTP lors de l'appel API : {}", e.getMessage());
            if (e.getStatusCode().value() == 404) {
                throw new DeviseInvalideException(deviseBase);
            }
            throw new ApiExterneException("Erreur HTTP " + e.getStatusCode().value());
        } catch (Exception e) {
            if (e instanceof DeviseInvalideException) throw e;
            log.error("Erreur de connexion a l'API externe : {}", e.getMessage());
            throw new ApiExterneException("Impossible de contacter le service de taux de change. Verifiez votre connexion internet.");
        }
    }
}
