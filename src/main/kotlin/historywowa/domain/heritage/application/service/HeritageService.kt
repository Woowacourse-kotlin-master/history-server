package historywowa.domain.heritage.application.service

import historywowa.domain.heritage.presentation.dto.res.HeritageImageRes
import org.springframework.web.multipart.MultipartFile

interface HeritageService {
    fun recognize(heritageImage: MultipartFile, userId: String): HeritageImageRes
}
