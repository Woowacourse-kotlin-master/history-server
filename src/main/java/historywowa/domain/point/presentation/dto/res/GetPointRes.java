package historywowa.domain.point.presentation.dto.res;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;


@Schema(description = "회원 포인트 잔액 응답 DTO")
public record GetPointRes(

        @Schema(description = "현재 포인트 잔액", example = "120")
        Long point
) {

    public static GetPointRes of(Long point) {
        return new GetPointRes(point);
    }
}
