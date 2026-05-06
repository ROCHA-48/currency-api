package com.currencyapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Requete de conversion de devises")
public class ConversionRequest {

    @NotBlank(message = "La devise source est obligatoire")
    @Size(min = 3, max = 3, message = "La devise source doit contenir exactement 3 lettres (ex: EUR)")
    @Schema(description = "Code de la devise source (ISO 4217)", example = "EUR", required = true)
    private String deviseSource;

    @NotBlank(message = "La devise cible est obligatoire")
    @Size(min = 3, max = 3, message = "La devise cible doit contenir exactement 3 lettres (ex: USD)")
    @Schema(description = "Code de la devise cible (ISO 4217)", example = "USD", required = true)
    private String deviseCible;

    @NotNull(message = "Le montant est obligatoire")
    @DecimalMin(value = "0.01", message = "Le montant doit etre superieur a 0")
    @Schema(description = "Montant a convertir", example = "100.00", required = true)
    private Double montant;
}
