package historywowa.domain.point.application.impl

import historywowa.domain.member.domain.entity.Member
import historywowa.domain.member.domain.repository.MemberRepository
import historywowa.domain.point.application.PointService
import historywowa.domain.point.domain.entity.Point
import historywowa.domain.point.domain.repository.PointRepository
import historywowa.domain.point.presentation.dto.res.GetPointRes
import historywowa.global.infra.exception.error.ErrorCode
import historywowa.global.infra.exception.error.HistoryException
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class PointServiceImpl(
    private val pointRepository: PointRepository,
    private val memberRepository: MemberRepository
) : PointService {

    private val log = LoggerFactory.getLogger(javaClass)

    override fun usePointForHeritage(member: Member) {
        val point = getPointByMember(member)
        point.minusBalance()
    }

    override fun getMemberPoint(userId: String): GetPointRes {
        val findMember = getMemberOrThrow(userId)
        val pointByMember = getPointByMember(findMember)
        val profile = findMember.profile
        if (profile != null) return GetPointRes.of(pointByMember.balance, profile)

        return GetPointRes.of(pointByMember.balance, "test")
    }

    private fun getPointByMember(member: Member): Point {
        return pointRepository.findByMember(member)
            .orElseThrow { HistoryException(ErrorCode.POINT_NOT_EXIST) }
    }

    private fun getMemberOrThrow(userId: String): Member {
        return memberRepository.findById(userId)
            .orElseThrow { HistoryException(ErrorCode.USER_NOT_EXIST) }
    }
}
