package com.currencyapi.exception;

public class ApiExterneException extends RuntimeException {
    public ApiExterneException(String message) {
        super("Erreur lors de l appel a l API externe de taux de change : " + message);
    }
}
