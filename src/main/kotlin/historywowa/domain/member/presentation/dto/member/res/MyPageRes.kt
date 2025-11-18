package historywowa.domain.member.presentation.dto.member.res;

import java.util.List;

public record MyPageRes(
        String userName,

        String profile,

        List<HeritageDto> heritageDtos
) {
    public static MyPageRes of(String userName, String profile, List<HeritageDto> heritageDtos) {
        return new MyPageRes(userName, profile, heritageDtos);
    }
}
