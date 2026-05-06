package com.currencyapi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.Map;


 // Represente la reponse JSON de l'API externe Exchangerate-API.
// Structure : https://v6.exchangerate-api.com/v6/{key}/latest/{base}

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExchangeRateApiResponse {

    @JsonProperty("result")
    private String result;

    @JsonProperty("base_code")
    private String baseCode;

    @JsonProperty("conversion_rates")
    private Map<String, Double> conversionRates;

    @JsonProperty("error-type")
    private String errorType;
}
