package com.modding.mp.adapter.in.web.response;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record StandardResponse<T>(
    String message,      
    T data,              
    Instant timestamp
) {
    public static <T> StandardResponse<T> ok(T data) {
        return new StandardResponse<>(null, data, Instant.now());
    }
    public static <T> StandardResponse<T> ok(T data, String message) {
        return new StandardResponse<>(message, data, Instant.now());
    }
    public static <T> StandardResponse<T> okMsg(String message) {
        return new StandardResponse<>(message, null, Instant.now());
    }
    /** Response of creation with "CREATED" message */
    public static <T> StandardResponse<T> created(T data) {
        return new StandardResponse<>("CREATED", data, Instant.now());
    }
    /** Response of creation with "CREATED" message */
    public static <T> StandardResponse<T> created() {
        return new StandardResponse<>("CREATED", null, Instant.now());
    }
}