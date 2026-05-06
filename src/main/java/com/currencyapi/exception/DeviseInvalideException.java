package com.currencyapi.exception;

public class DeviseInvalideException extends RuntimeException {
    public DeviseInvalideException(String devise) {
        super("Devise invalide ou non supportee : '" + devise + "'. Utilisez un code ISO 4217 valide (ex: EUR, USD, XAF).");
    }
}
