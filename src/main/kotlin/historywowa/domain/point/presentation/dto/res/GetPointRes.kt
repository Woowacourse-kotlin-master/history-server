package historywowa.domain.point.presentation.dto.res

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "회원 포인트 잔액 응답 DTO")
data class GetPointRes(

        @Schema(description = "현재 포인트 잔액", example = "120")
        val point: Long,

        val profile: String
) {
    companion object {
        fun of(point: Long, profile: String): GetPointRes =
                GetPointRes(point, profile)
    }
}
