package historywowa.domain.oauth2.presentation.controller;


import historywowa.domain.oauth2.application.service.LogoutService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Tag(name = "Auth", description = "인증 관련 API")
public class LogoutController {

    private final LogoutService logoutService;


    @Operation(summary = "[APP] 로그아웃", description = "사용자를 로그아웃하고 토큰을 무효화합니다.")
    @PostMapping("/app/oauth2/logout")
    public ResponseEntity<Void> logout(
        @AuthenticationPrincipal String userId,
        @RequestBody LogoutRequest request
    ) {
        logoutService.logout(userId, request.refreshToken());

        return ResponseEntity.ok().build();
    }

    public record LogoutRequest(String refreshToken) {

    }
}