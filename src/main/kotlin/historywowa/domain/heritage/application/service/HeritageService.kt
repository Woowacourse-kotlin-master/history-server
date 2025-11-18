package historywowa.domain.heritage.application.service;

import historywowa.domain.heritage.presentation.dto.res.HeritageImageRes;
import java.io.IOException;
import org.springframework.web.multipart.MultipartFile;

public interface HeritageService {

    HeritageImageRes recognize(MultipartFile heritageImage, String userId) throws IOException;
}
