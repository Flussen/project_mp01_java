package com.modding.mp.application.usecase;

import org.springframework.stereotype.Component;

@Component
public class EmailAlreadyUsedException extends RuntimeException {
    public EmailAlreadyUsedException(String email) {
        super("The email is already registered: " + email);
    }
}