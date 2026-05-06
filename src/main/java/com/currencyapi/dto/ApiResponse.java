package com.currencyapi.dto;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApiResponse<T> {
    private boolean succes;
    private String message;
    private T data;
    private LocalDateTime timestamp;

    public static <T> ApiResponse<T> succes(String message, T data) {
        return ApiResponse.<T>builder()
                .succes(true).message(message).data(data)
                .timestamp(LocalDateTime.now()).build();
    }

    public static <T> ApiResponse<T> erreur(String message) {
        return ApiResponse.<T>builder()
                .succes(false).message(message).data(null)
                .timestamp(LocalDateTime.now()).build();
    }
}
