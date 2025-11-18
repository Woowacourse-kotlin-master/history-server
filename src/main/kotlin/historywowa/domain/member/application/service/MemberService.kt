package historywowa.domain.member.application.service;

import historywowa.domain.member.presentation.dto.member.res.MyPageRes;

public interface MemberService {

    MyPageRes infoMyPage(String userId);
}
