package historywowa.domain.heritage.presentation.controller

import historywowa.domain.heritage.application.service.HeritageService
import historywowa.domain.heritage.presentation.dto.res.HeritageImageRes
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/api/heritage")
@Tag(name = "Heritage", description = "문화재 이미지 전용 API")
class HeritageController(
    private val heritageService: HeritageService
) {

    @Operation(
        summary = "문화재 이미지 인식 API",
        description = "업로드된 이미지로 문화재를 인식합니다."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "문화재 이미지 인식 성공"),
            ApiResponse(responseCode = "404", description = "유저 존재하지 않음")
        ]
    )
    @PostMapping(consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun recognizeHeritageImage(
        @AuthenticationPrincipal userId: String,
        @RequestPart heritageImage: MultipartFile
    ): ResponseEntity<HeritageImageRes> {
        return ResponseEntity.ok(heritageService.recognize(heritageImage, userId))
    }
}
