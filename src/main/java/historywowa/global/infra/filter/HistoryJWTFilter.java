package historywowa.global.infra.filter;

import historywowa.domain.member.domain.entity.Role;
import historywowa.global.infra.exception.error.HistoryException;
import historywowa.global.infra.exception.error.ErrorCode;
import historywowa.global.jwt.util.JWTUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

@RequiredArgsConstructor
@Slf4j
public class HistoryJWTFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;
    private final List<String> excludedPaths;

    @Override
    protected void doFilterInternal(
        @NonNull HttpServletRequest request,
        @NonNull HttpServletResponse response,
        @NonNull FilterChain filterChain
    )
        throws ServletException, IOException {

        String requestURI = request.getRequestURI();

        String method = request.getMethod();

        // 소셜 로그인 요청에 대한 중복 로그인 체크
        if ((requestURI.contains("/api/oauth2/login") && "POST".equals(method)) ||
            (requestURI.contains("/api/oauth2/callback"))) {
            String accessToken = jwtUtil.getAccessTokenFromHeaders(request);
            if (accessToken != null && jwtUtil.jwtVerify(accessToken, "access")) {
                throw new HistoryException(ErrorCode.DUPLICATE_LOGIN_NOT_EXIST);
            }
            filterChain.doFilter(request, response);
            return;
        }

        String accessToken = jwtUtil.getAccessTokenFromHeaders(request);

        log.debug("Access Token: {}", accessToken);

        if (accessToken == null || accessToken.equals("undefined") || accessToken.equals("null")) {
            filterChain.doFilter(request, response);
            return;
        }

        if (!jwtUtil.jwtVerify(accessToken, "access")) {
            throw new HistoryException(ErrorCode.INVALID_ACCESS_TOKEN);
        }

        String userId = jwtUtil.getId(accessToken);
        log.debug("User ID: {}", userId);
        Role role = jwtUtil.getRole(accessToken);

        GrantedAuthority authority = new SimpleGrantedAuthority(role.getKey());
        log.debug("User Role: {}", role.getKey());

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userId, null, Collections.singleton(authority));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        log.debug("인증 설정 완료: {}", SecurityContextHolder.getContext().getAuthentication());
        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(
        @NonNull HttpServletRequest request
    ) {
        return excludedPaths.stream()
            .anyMatch(pattern ->
                new AntPathMatcher().match(pattern, request.getServletPath()));
    }
}