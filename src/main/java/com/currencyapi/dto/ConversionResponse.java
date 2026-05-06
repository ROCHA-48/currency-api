package com.currencyapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Resultat de la conversion de devises")
public class ConversionResponse {

    @Schema(description = "Devise source", example = "EUR")
    private String deviseSource;

    @Schema(description = "Devise cible", example = "USD")
    private String deviseCible;

    @Schema(description = "Montant original", example = "100.0")
    private Double montantOriginal;

    @Schema(description = "Montant converti", example = "108.25")
    private Double montantConverti;

    @Schema(description = "Taux de change applique", example = "1.0825")
    private Double tauxDeChange;

    @Schema(description = "Date et heure de la conversion")
    private LocalDateTime dateConversion;
}
