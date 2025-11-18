package historywowa.domain.member.application.service

import historywowa.domain.member.presentation.dto.member.res.MyPageRes

interface MemberService {
    fun infoMyPage(userId: String): MyPageRes
}
