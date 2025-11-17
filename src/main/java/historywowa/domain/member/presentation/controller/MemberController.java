package historywowa.domain.member.presentation.controller;


import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Member", description = "사용자 전용 API")
public class MemberController {



/*    @Operation(summary = "닉네임 수정 API", description = "닉네임을 수정합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "닉네임 수정 성공"),
        @ApiResponse(responseCode = "404", description = "유저 존재하지 않음")
    })
    @PatchMapping("/app/member/name")
    public ResponseEntity<BaseOKResponse<Void>> updateNickname(@AuthenticationPrincipal String userId, @RequestBody NicknameReq req) {
        memberService.updateNickname(userId, req.name());
        return ResponseEntity.ok(
            BaseOKResponse.of(
                HttpStatus.OK,
                POST_NICKNAME_SUCCESS));
    }*/



}