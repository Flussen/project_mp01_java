package com.modding.mp.adapter.in.web.response;

import java.time.Instant;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;


@JsonInclude(JsonInclude.Include.NON_NULL)
public record StandardError(
    String error,
    String code,
    int status,
    String path,
    String message,
    List<FieldError> errors,
    Instant timestamp
) {
    public static StandardError of(String error, String code, int status, String path, String message, List<FieldError> errors) {
        return new StandardError(error, code, status, path, message, errors, Instant.now());
    }

    public record FieldError(String field, String reason){}
}
