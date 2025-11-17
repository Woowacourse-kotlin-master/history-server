package historywowa.domain.oauth2.application.service;

import historywowa.domain.oauth2.presentation.dto.res.LoginToken;
import historywowa.domain.member.domain.entity.Role;

public interface CreateAccessTokenAndRefreshTokenService {
    LoginToken createAccessTokenAndRefreshToken(String userId, Role role, String email);
}