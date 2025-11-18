package historywowa.domain.point.application;

import historywowa.domain.member.domain.entity.Member;
import historywowa.domain.point.presentation.dto.res.GetPointRes;

public interface PointService {

    void usePointForHeritage(Member member);

    GetPointRes getMemberPoint(String userId);

}
