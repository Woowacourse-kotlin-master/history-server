package historywowa.domain.heritage.presentation.dto.req;

import org.springframework.web.multipart.MultipartFile;

public record HeritageImageReq(
        MultipartFile heritageImage
) {
}
