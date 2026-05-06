package com.currencyapi.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;


 // Configuration du WebClient pour appeler l'API externe Exchangerate-API.

@Configuration
public class WebClientConfig {

    @Value("${app.exchangerate.base-url}")
    private String baseUrl;

    @Bean
    public WebClient exchangeRateWebClient() {
        return WebClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader("Accept", "application/json")
                .build();
    }
}
