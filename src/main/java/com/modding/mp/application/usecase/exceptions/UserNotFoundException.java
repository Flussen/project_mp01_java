package com.modding.mp.application.usecase.exceptions;

import org.springframework.stereotype.Component;

@Component
public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String id) {
        super("User not found: " + id);
    }
}