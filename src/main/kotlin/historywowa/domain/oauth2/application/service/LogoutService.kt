package historywowa.domain.oauth2.application.service

interface LogoutService {
    fun logout(userId: String, refreshToken: String)
}
