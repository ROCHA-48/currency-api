# API de Conversion de Devises - Spring Boot

API RESTful de conversion de devises en temps reel utilisant les taux de change de **Exchangerate-API**.

---

## Stack Technologique

| Technologie | Role |
|---|---|
| Spring Boot 3.2 | Framework principal |
| Spring WebClient | Appels HTTP vers l'API externe |
| Springdoc OpenAPI | Documentation Swagger |
| Lombok | Reduction du code boilerplate |

---

## Obtenir une Cle API GRATUITE

1. Va sur **https://www.exchangerate-api.com**
2. Clique sur **"Get Free Key"**
3. Cree un compte (email + mot de passe)
4. Copie ta cle API (format : `abc123def456...`)
5. Colle-la dans `application.properties` :
```properties
app.exchangerate.api-key=TA_CLE_ICI
```

> La cle gratuite permet **1500 requetes/mois** — largement suffisant pour les tests.

---

## Lancer l'API

```bash
git clone https://github.com/votre-username/currency-api.git
cd currency-api
mvn clean install -DskipTests
mvn spring-boot:run
```

---

## Tester l'API

### Via Swagger UI
```
http://localhost:8080/swagger-ui.html
```

### Via Postman ou curl

#### Conversion EUR vers USD
```bash
POST http://localhost:8080/api/v1/conversion
Content-Type: application/json

{
  "deviseSource": "EUR",
  "deviseCible": "USD",
  "montant": 100.00
}
```

Reponse :
```json
{
  "succes": true,
  "message": "100.00 EUR = 108.25 USD",
  "data": {
    "deviseSource": "EUR",
    "deviseCible": "USD",
    "montantOriginal": 100.0,
    "montantConverti": 108.25,
    "tauxDeChange": 1.0825,
    "dateConversion": "2026-05-04T10:00:00"
  }
}
```

#### USD vers XAF (Franc CFA)
```bash
POST http://localhost:8080/api/v1/conversion

{
  "deviseSource": "USD",
  "deviseCible": "XAF",
  "montant": 50.00
}
```

#### Conversion simple via URL (navigateur)
```
GET http://localhost:8080/api/v1/conversion?de=EUR&vers=XAF&montant=100
```

#### Voir tous les taux pour l'Euro
```
GET http://localhost:8080/api/v1/taux/EUR
```

---

## Endpoints

| Methode | URL | Description |
|---|---|---|
| POST | /api/v1/conversion | Convertir un montant (JSON) |
| GET | /api/v1/conversion?de=EUR&vers=USD&montant=100 | Conversion simple via URL |
| GET | /api/v1/taux/{devise} | Voir tous les taux d'une devise |

---

## Devises populaires supportees

| Code | Devise |
|---|---|
| EUR | Euro |
| USD | Dollar americain |
| XAF | Franc CFA BEAC (Cameroun, etc.) |
| XOF | Franc CFA BCEAO |
| GBP | Livre sterling |
| JPY | Yen japonais |
| CAD | Dollar canadien |
| CHF | Franc suisse |
| MAD | Dirham marocain |
| NGN | Naira nigerien |
| GHS | Cedi ghaneen |

---

## Gestion des erreurs

| Erreur | Code HTTP | Cause |
|---|---|---|
| Devise invalide | 400 | Code ISO inconnu (ex: "ABC") |
| Montant invalide | 400 | Montant <= 0 ou absent |
| API indisponible | 503 | Probleme de connexion internet |
| Cle API invalide | 503 | Cle API incorrecte ou expiree |
