package historywowa.domain.heritage.presentation.dto.res;

import historywowa.domain.heritage.domain.entity.Heritage;

public record HeritageImageRes(
        String heritageUrl,
        String heritageText
) {
    public static HeritageImageRes of(String heritageUrl, String heritageText) {
        return new HeritageImageRes(heritageUrl, heritageText);
    }
}
