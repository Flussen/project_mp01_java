package com.modding.mp.application.usecase.exceptions;

public class EmailAlreadyUsedException extends RuntimeException {
    public EmailAlreadyUsedException(String email) {
        super("The email is already registered: " + email);
    }
}