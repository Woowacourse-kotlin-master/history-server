package historywowa.domain.oauth2.application.service.impl

import historywowa.domain.member.domain.entity.Member
import historywowa.domain.member.domain.entity.Role
import historywowa.domain.member.domain.repository.MemberRepository
import historywowa.domain.oauth2.application.service.CreateAccessTokenAndRefreshTokenService
import historywowa.domain.oauth2.presentation.dto.res.LoginToken
import historywowa.global.infra.exception.error.ErrorCode
import historywowa.global.infra.exception.error.HistoryException
import historywowa.global.jwt.domain.entity.JsonWebToken
import historywowa.global.jwt.domain.repository.JsonWebTokenRepository
import historywowa.global.jwt.util.JWTUtil
import jakarta.transaction.Transactional
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
@Transactional
class CreateAccessTokenAndRefreshTokenServiceImpl(
    private val jwtUtil: JWTUtil,
    private val jsonWebTokenRepository: JsonWebTokenRepository,
    private val memberRepository: MemberRepository
) : CreateAccessTokenAndRefreshTokenService {

    private val log = LoggerFactory.getLogger(CreateAccessTokenAndRefreshTokenServiceImpl::class.java)

    override fun createAccessTokenAndRefreshToken(
        userId: String,
        role: Role,
        email: String
    ): LoginToken {
        val accessToken = jwtUtil.createAccessToken(userId, role, email)
        val refreshToken = jwtUtil.createRefreshToken(userId, role, email)

        val jsonWebToken = JsonWebToken.of(
            refreshToken = refreshToken,
            providerId = userId,
            email = email,
            role = role
        )

        jsonWebTokenRepository.save(jsonWebToken)

        val nameFlag = onBoarding(userId)

        return LoginToken.of(accessToken, refreshToken, nameFlag)
    }

    private fun onBoarding(userId: String): Boolean {
        val member: Member = memberRepository.findById(userId)
            .orElseThrow { HistoryException(ErrorCode.USER_NOT_EXIST) }

        return member.nameFlag
    }
}
