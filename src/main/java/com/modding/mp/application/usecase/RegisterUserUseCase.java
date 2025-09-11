package com.modding.mp.application.usecase;

import java.time.Instant;
import java.util.Set;


import com.modding.mp.domain.model.Email;
import com.modding.mp.domain.model.User;
import com.modding.mp.domain.model.UserId;
import com.modding.mp.domain.port.out.UserRepository;

public class RegisterUserUseCase {
    private final UserRepository users;
    public RegisterUserUseCase(UserRepository users) {this.users = users;}
     
    public UserId handle(String username, Email email, String passwordHash) {
        if(users.existsByEmail(email)) throw new IllegalArgumentException("email_en_uso");
        User user = new User(email, username, passwordHash, true, null, false, Instant.now());
        User createdUser = users.save(user);
        return createdUser.getId();
    }
}
