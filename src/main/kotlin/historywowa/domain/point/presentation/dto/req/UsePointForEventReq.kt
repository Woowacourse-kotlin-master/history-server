package historywowa.domain.point.presentation.dto.req

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "이벤트 응모 포인트 차감 요청")
data class UsePointForEventReq(

    @Schema(description = "이벤트 캠페인 ID", example = "1")
    val eventCampaignId: Long,

    @Schema(description = "차감할 포인트 금액", example = "1000")
    val amount: Long
)
