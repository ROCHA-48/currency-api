package com.currencyapi.controller;

import com.currencyapi.dto.ApiResponse;
import com.currencyapi.dto.ConversionRequest;
import com.currencyapi.dto.ConversionResponse;
import com.currencyapi.service.ExchangeRateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


//Controleur REST exposant les endpoints de conversion de devises.

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Tag(name = "Conversion de Devises", description = "Convertit des montants entre devises en temps reel")
public class CurrencyController {

    private final ExchangeRateService exchangeRateService;


    // POST /api/v1/conversion  →  Convertir un montant

    @PostMapping("/conversion")
    @Operation(
            summary = "Convertir un montant",
            description = "Convertit un montant d'une devise source vers une devise cible en utilisant les taux de change en temps reel.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            examples = {
                                    @ExampleObject(name = "EUR vers USD", value = """
                                            {
                                              "deviseSource": "EUR",
                                              "deviseCible": "USD",
                                              "montant": 100.00
                                            }"""),
                                    @ExampleObject(name = "USD vers XAF", value = """
                                            {
                                              "deviseSource": "USD",
                                              "deviseCible": "XAF",
                                              "montant": 50.00
                                            }"""),
                                    @ExampleObject(name = "XAF vers EUR", value = """
                                            {
                                              "deviseSource": "XAF",
                                              "deviseCible": "EUR",
                                              "montant": 10000.00
                                            }""")
                            }
                    )
            )
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Conversion reussie"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Devise invalide ou montant incorrect", content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "503", description = "API externe indisponible", content = @Content)
    })
    public ResponseEntity<ApiResponse<ConversionResponse>> convertir(
            @Valid @RequestBody ConversionRequest request) {

        ConversionResponse result = exchangeRateService.convertir(request);
        String message = String.format("%.2f %s = %.2f %s",
                result.getMontantOriginal(), result.getDeviseSource(),
                result.getMontantConverti(), result.getDeviseCible());

        return ResponseEntity.ok(ApiResponse.succes(message, result));
    }


    // GET /api/v1/taux/{devise}  →  Voir tous les taux

    @GetMapping("/taux/{devise}")
    @Operation(
            summary = "Obtenir tous les taux de change",
            description = "Retourne tous les taux de change disponibles pour une devise donnee."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Taux recuperes avec succes"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Code de devise invalide", content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "503", description = "API externe indisponible", content = @Content)
    })
    public ResponseEntity<ApiResponse<Map<String, Double>>> getTaux(
            @Parameter(description = "Code ISO 4217 de la devise (ex: EUR, USD, XAF)", example = "EUR")
            @PathVariable
            @Size(min = 3, max = 3, message = "Le code devise doit contenir 3 lettres")
            String devise) {

        Map<String, Double> taux = exchangeRateService.getTousLesTaux(devise);
        return ResponseEntity.ok(ApiResponse.succes(
                taux.size() + " taux de change disponibles pour " + devise.toUpperCase(), taux));
    }


    // GET /api/v1/conversion/simple  →  Conversion via query params

    @GetMapping("/conversion")
    @Operation(
            summary = "Conversion simple via parametres URL",
            description = "Version simplifiee pour tester rapidement dans le navigateur."
    )
    public ResponseEntity<ApiResponse<ConversionResponse>> convertirSimple(
            @Parameter(description = "Devise source", example = "EUR") @RequestParam String de,
            @Parameter(description = "Devise cible", example = "USD") @RequestParam String vers,
            @Parameter(description = "Montant", example = "100") @RequestParam Double montant) {

        ConversionRequest request = ConversionRequest.builder()
                .deviseSource(de)
                .deviseCible(vers)
                .montant(montant)
                .build();

        ConversionResponse result = exchangeRateService.convertir(request);
        String message = String.format("%.2f %s = %.2f %s",
                result.getMontantOriginal(), result.getDeviseSource(),
                result.getMontantConverti(), result.getDeviseCible());

        return ResponseEntity.ok(ApiResponse.succes(message, result));
    }
}
