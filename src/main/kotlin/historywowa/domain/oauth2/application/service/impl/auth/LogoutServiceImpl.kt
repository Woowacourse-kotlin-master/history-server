package historywowa.domain.oauth2.application.service.impl.auth

import historywowa.domain.oauth2.application.service.LogoutService
import historywowa.global.infra.exception.error.ErrorCode
import historywowa.global.infra.exception.error.HistoryException
import historywowa.global.jwt.domain.repository.JsonWebTokenRepository
import historywowa.global.jwt.domain.repository.SocialTokenRepository
import historywowa.global.jwt.util.JWTUtil
import jakarta.transaction.Transactional
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
@Transactional
class LogoutServiceImpl(
        private val jwtUtil: JWTUtil,
        private val jsonWebTokenRepository: JsonWebTokenRepository,
        private val socialTokenRepository: SocialTokenRepository
) : LogoutService {

    private val log = LoggerFactory.getLogger(LogoutServiceImpl::class.java)

    override fun logout(userId: String, refreshToken: String) {

        if (!jwtUtil.jwtVerify(refreshToken, "refresh")) {
            throw HistoryException(ErrorCode.JWT_ERROR_TOKEN)
        }

        val jsonWebToken = jsonWebTokenRepository.findById(refreshToken)
                .orElseThrow { HistoryException(ErrorCode.REFRESH_TOKEN_NOT_EXIST) }

        socialTokenRepository.deleteByUserId(jsonWebToken.providerId)
        jsonWebTokenRepository.delete(jsonWebToken)

        log.info("사용자 로그아웃 완료: userId={}", jsonWebToken.providerId)
    }
}
