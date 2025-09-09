package com.modding.mp.domain.port.out;

import java.util.Optional;

import com.modding.mp.domain.model.Email;
import com.modding.mp.domain.model.User;
import com.modding.mp.domain.model.UserId;

public interface UserRepository {
    Optional<User> byEmail(Email email);
    Optional<User> byId(UserId id);
    Optional<User> byUsername(String username);
    User save(User user);
    boolean existsByEmail(Email email);
}
