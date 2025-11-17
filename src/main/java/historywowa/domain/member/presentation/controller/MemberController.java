package historywowa.domain.member.presentation.controller;


import historywowa.domain.member.application.service.MemberService;
import historywowa.domain.member.domain.entity.Member;
import historywowa.domain.member.presentation.dto.member.res.MyPageRes;
import historywowa.global.infra.exception.error.ErrorCode;
import historywowa.global.infra.exception.error.HistoryException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/member")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Member", description = "사용자 전용 API")
public class MemberController {

    private final MemberService memberService;

    @Operation(summary = "마이페이지 API", description = "마이페이지를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "닉네임 수정 성공"),
            @ApiResponse(responseCode = "404", description = "유저 존재하지 않음")
    })
    @GetMapping
    public ResponseEntity<MyPageRes> infoMyPage(@AuthenticationPrincipal String userId) {
        return ResponseEntity.ok(memberService.infoMyPage(userId));
    }


}