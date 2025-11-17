package historywowa.domain.point.application;

import historywowa.domain.member.domain.entity.Member;

public interface PointService {

    void usePointForHeritage(Member member);

    Long getMemberPoint(Member member);

}
