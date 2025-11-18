package historywowa.domain.heritage.application.service.impl

import historywowa.domain.heritage.application.service.HeritageService
import historywowa.domain.heritage.domain.entity.Heritage
import historywowa.domain.heritage.domain.repository.HeritageRepository
import historywowa.domain.heritage.presentation.dto.res.HeritageImageRes
import historywowa.domain.member.domain.entity.Member
import historywowa.domain.member.domain.repository.MemberRepository
import historywowa.domain.openai.application.service.OpenAIVisionService
import historywowa.domain.point.application.PointService
import historywowa.global.infra.exception.error.ErrorCode
import historywowa.global.infra.exception.error.HistoryException
import historywowa.global.infra.s3.presentation.application.S3Service
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile

@Service
@Transactional
class HeritageServiceImpl(
        private val s3Service: S3Service,
        private val heritageRepository: HeritageRepository,
        private val openAIVisionService: OpenAIVisionService,
        private val memberRepository: MemberRepository,
        private val pointService: PointService
) : HeritageService {

    override fun recognize(heritageImage: MultipartFile, userId: String): HeritageImageRes {

        val member = getMemberOrThrow(userId)

        validatePoint(member)

        val imageUrl = uploadImageToS3(heritageImage, userId) ?: throw HistoryException(ErrorCode.IMAGE_UPLOAD_FAILED)
        val analysisText = analyzeImage(imageUrl)

        saveHeritageRecord(member, imageUrl, analysisText)
        pointService.usePointForHeritage(member)

        return HeritageImageRes.of(imageUrl, analysisText)
    }

    private fun validatePoint(member: Member) {
        val point = pointService.getMemberPoint(member.id).point
        if (point == 0L) {
            throw HistoryException(ErrorCode.INSUFFICIENT_POINT_FOR_HERITAGE)
        }
    }

    private fun uploadImageToS3(file: MultipartFile, userId: String): String? {
        return s3Service.storeImage(file, userId)
    }

    private fun analyzeImage(imageUrl: String): String {
        return openAIVisionService.analyzeHeritageImage(imageUrl)
    }

    private fun saveHeritageRecord(member: Member, url: String, text: String): Heritage {
        val heritage = Heritage(
                member = member,
                heritageUrl = url,
                heritageText = text
        )
        return heritageRepository.save(heritage)
    }

    private fun getMemberOrThrow(userId: String): Member {
        return memberRepository.findById(userId)
                .orElseThrow { HistoryException(ErrorCode.USER_NOT_EXIST) }
    }
}
