package historywowa.domain.member.application.service.impl;

import historywowa.domain.heritage.domain.entity.Heritage;
import historywowa.domain.heritage.domain.repository.HeritageRepository;
import historywowa.domain.member.application.service.MemberService;
import historywowa.domain.member.domain.entity.Member;
import historywowa.domain.member.domain.repository.MemberRepository;
import historywowa.domain.member.presentation.dto.member.res.HeritageDto;
import historywowa.domain.member.presentation.dto.member.res.MyPageRes;
import historywowa.global.infra.exception.error.ErrorCode;
import historywowa.global.infra.exception.error.HistoryException;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    private final HeritageRepository heritageRepository;


    @Override
    public MyPageRes infoMyPage(String userId) {

        Member member = getMemberOrThrow(userId);

        List<HeritageDto> heritageDtos = heritageRepository.findByMember(member).stream()
                .map(h -> HeritageDto.of(h.getHeritageUrl(), h.getHeritageText()))
                .toList();

        return MyPageRes.of(
                member.getName(),
                member.getProfile(),
                heritageDtos
        );
    }

    private Member getMemberOrThrow(String userId) {
        return memberRepository.findById(userId)
                .orElseThrow(() -> new HistoryException(ErrorCode.USER_NOT_EXIST));
    }
}
