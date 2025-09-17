package com.modding.mp.domain.port.out;

public interface IPasswordHasher {
      boolean matches(String raw, String encoded);
      String hash(String raw);
}
