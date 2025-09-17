package com.modding.mp.application.usecase.exceptions;

public class BadRequestException extends RuntimeException {
        public BadRequestException() {
        super("Bad request");
    }
    public BadRequestException(String message) {
        super(message);
    }
}
