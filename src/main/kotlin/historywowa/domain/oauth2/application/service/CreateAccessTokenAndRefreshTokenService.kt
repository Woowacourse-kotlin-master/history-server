package historywowa.domain.oauth2.application.service

import historywowa.domain.member.domain.entity.Role
import historywowa.domain.oauth2.presentation.dto.res.LoginToken

interface CreateAccessTokenAndRefreshTokenService {
    fun createAccessTokenAndRefreshToken(userId: String, role: Role, email: String): LoginToken
}
