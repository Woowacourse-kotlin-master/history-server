package historywowa.domain.point.application.impl;

import static historywowa.global.infra.exception.error.ErrorCode.*;

import historywowa.domain.member.domain.entity.Member;
import historywowa.domain.member.domain.repository.MemberRepository;
import historywowa.domain.point.application.PointService;
import historywowa.domain.point.domain.entity.*;
import historywowa.domain.point.domain.entity.type.*;
import historywowa.domain.point.domain.repository.*;
import historywowa.domain.point.presentation.dto.req.*;
import historywowa.domain.point.presentation.dto.res.*;
import historywowa.global.infra.exception.error.HistoryException;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class PointServiceImpl implements PointService {

    private final PointRepository pointRepository;

    @Override
    public void usePointForHeritage(Member member) {
        Point point = getPointByMember(member);
        point.minusBalance();
    }

    @Override
    public Long getMemberPoint(Member member){
        Point pointByMember = getPointByMember(member);
        return pointByMember.getBalance();
    }

    private Point getPointByMember(Member member) {
        return pointRepository.findByMember(member)
                .orElseThrow(() -> new HistoryException(POINT_NOT_EXIST));
    }
}
