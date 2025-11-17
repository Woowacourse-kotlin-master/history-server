package historywowa.domain.member.presentation.dto.member.res;

public record HeritageDto(
        String heritageImage,

        String heritageText
) {
    public static HeritageDto of(String heritageImage, String heritageText){
        return new HeritageDto(heritageImage, heritageText);
    }
}
