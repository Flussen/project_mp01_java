package com.modding.mp.domain.port.out;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.modding.mp.domain.model.UserId;

public interface ITokenService {
  String signAccess(UserId userId, String username);
  String signRefresh(UserId userId);
  DecodedJWT verify(String token);
  DecodedJWT verifyAccessToken(String token);
  void verifyRefreshToken(String token);
}