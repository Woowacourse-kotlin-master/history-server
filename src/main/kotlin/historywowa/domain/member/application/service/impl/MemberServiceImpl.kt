package historywowa.domain.member.application.service.impl

import historywowa.domain.heritage.domain.repository.HeritageRepository
import historywowa.domain.member.application.service.MemberService
import historywowa.domain.member.domain.entity.Member
import historywowa.domain.member.domain.repository.MemberRepository
import historywowa.domain.member.presentation.dto.member.res.HeritageDto
import historywowa.domain.member.presentation.dto.member.res.MyPageRes
import historywowa.global.infra.exception.error.ErrorCode
import historywowa.global.infra.exception.error.HistoryException
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
@Transactional
class MemberServiceImpl(
    private val memberRepository: MemberRepository,
    private val heritageRepository: HeritageRepository
) : MemberService {

    override fun infoMyPage(userId: String): MyPageRes {
        val member = getMemberOrThrow(userId)

        val heritageDtos = heritageRepository.findByMember(member)
            .map { HeritageDto.of(it.heritageUrl, it.heritageText) }

        return MyPageRes.of(
            member.name,
            member.profile,
            heritageDtos
        )
    }

    private fun getMemberOrThrow(userId: String): Member {
        return memberRepository.findById(userId)
            .orElseThrow { HistoryException(ErrorCode.USER_NOT_EXIST) }
    }
}
