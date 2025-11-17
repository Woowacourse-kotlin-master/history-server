package historywowa.domain.point.presentation.controller;


import historywowa.domain.point.application.PointService;
import historywowa.domain.point.presentation.dto.res.GetPointRes;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/point")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Point", description = "포인트 관련 API")
public class PointController {

    private final PointService pointService;

    @Operation(summary = "포인트 헤더 내역 조회 API", description = "회원의 포인트 잔액과 거래 이력을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "포인트 내역 조회 성공"),
            @ApiResponse(responseCode = "404", description = "포인트 정보가 존재하지 않음")
    })
    @GetMapping
    public ResponseEntity<GetPointRes> getPointHistory(@AuthenticationPrincipal String memberId) {
        return ResponseEntity.ok(pointService.getMemberPoint(memberId));
    }

}