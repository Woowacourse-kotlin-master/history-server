package historywowa.domain.oauth2.application.service.impl.auth;

import static historywowa.global.infra.exception.error.ErrorCode.*;

import historywowa.domain.member.domain.entity.Member;
import historywowa.domain.member.domain.repository.MemberRepository;
import historywowa.domain.oauth2.application.service.ReissueService;
import historywowa.domain.oauth2.presentation.dto.res.LoginToken;
import historywowa.domain.member.domain.entity.Role;
import historywowa.global.infra.exception.error.HistoryException;
import historywowa.global.jwt.domain.entity.JsonWebToken;
import historywowa.global.jwt.domain.repository.JsonWebTokenRepository;
import historywowa.global.jwt.util.JWTUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ReissueServiceImpl implements ReissueService {

    private final JWTUtil jwtUtil;
    private final JsonWebTokenRepository jsonWebTokenRepository;
    private final MemberRepository memberRepository;

    @Override
    public LoginToken reissue(String refreshToken) {
        if (!jwtUtil.jwtVerify(refreshToken, "refresh")) {
            log.info("Refresh token not valid");
            throw new HistoryException(INVALID_REFRESH_TOKEN);
        }

        JsonWebToken jsonWebToken = jsonWebTokenRepository.findById(refreshToken)
            .orElseThrow(() -> new HistoryException(REFRESH_TOKEN_NOT_EXIST));

        String userId = jsonWebToken.getProviderId();
        Role role = jsonWebToken.getRole();
        String email = jsonWebToken.getEmail();

        String newAccessToken = jwtUtil.createAccessToken(userId, role, email);
        String newRefreshToken = jwtUtil.createRefreshToken(userId, role, email);

        JsonWebToken newJsonWebToken = JsonWebToken.builder()
            .refreshToken(newRefreshToken)
            .providerId(userId)
            .email(email)
            .role(role)
            .build();

        jsonWebTokenRepository.delete(jsonWebToken);
        jsonWebTokenRepository.save(newJsonWebToken);

        return LoginToken.of(newAccessToken, newRefreshToken, onBoarding(userId));
    }

    public boolean onBoarding(String userId) {
        Member member = memberRepository.findById(userId).orElse(null);
        if (member == null)
            throw new HistoryException(USER_NOT_EXIST);

        return member.isNameFlag();
    }
}
