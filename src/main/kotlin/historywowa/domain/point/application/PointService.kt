package historywowa.domain.point.application

import historywowa.domain.member.domain.entity.Member
import historywowa.domain.point.presentation.dto.res.GetPointRes

interface PointService {
    fun usePointForHeritage(member: Member)
    fun getMemberPoint(userId: String): GetPointRes
}
