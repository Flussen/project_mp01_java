package com.modding.mp.application.usecase;

import java.util.Optional;

import com.modding.mp.domain.model.User;
import com.modding.mp.domain.model.UserId;
import com.modding.mp.domain.port.out.UserRepository;

public class LoginUserUseCase {
    private final UserRepository users;
    public LoginUserUseCase(UserRepository users) {this.users = users;}

    public UserId handle(String username, String passwordTry) {
        if(username.isEmpty() || passwordTry.isEmpty()) throw new Error("Error, empty parameters!");
        Optional<User> user = users.byUsername(username);
        if(!user.isPresent()) throw new Error("User not found!");
        throw new Error("NOT IMPLEMENTED");
    } 
}
