package com.modding.mp.adapter.out.security;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.modding.mp.domain.port.out.IPasswordHasher;

@Component
public class StringPasswordHasher implements IPasswordHasher {
    private final PasswordEncoder encoder;

    public StringPasswordHasher(PasswordEncoder encoder) {
          this.encoder = encoder;
    }

      @Override
      public boolean matches(String raw, String encoded) {
            return encoder.matches(raw, encoded);
      }

      @Override
      public String hash(String raw) {
            return encoder.encode(raw);
      }
}
