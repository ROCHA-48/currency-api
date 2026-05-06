package com.currencyapi.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API de Conversion de Devises")
                        .version("1.0.0")
                        .description("""
                                 Description
                                API RESTful de conversion de devises en temps reel.
                                Les taux de change sont recuperes dynamiquement depuis Exchangerate-API.

                                 Comment utiliser
                                1. Appelez `POST /api/v1/conversion` avec la devise source, cible et le montant
                                2. Appelez `GET /api/v1/taux/{devise}` pour voir tous les taux d'une devise

                                 Codes de devises populaires
                                | Code | Devise |
                                |------|--------|
                                | EUR  | Euro |
                                | USD  | Dollar americain |
                                | XAF  | Franc CFA (BEAC) |
                                | GBP  | Livre sterling |
                                | JPY  | Yen japonais |
                                | CAD  | Dollar canadien |
                                | CHF  | Franc suisse |
                                | MAD  | Dirham marocain |

                                 Note
                                Obtenir une cle API gratuite sur [exchangerate-api.com](https://www.exchangerate-api.com)
                                """)
                        .contact(new Contact().name("Equipe Dev").email("dev@currencyapi.com")))
                .servers(List.of(
                        new Server().url("http://localhost:8080").description("Serveur local")));
    }
}
