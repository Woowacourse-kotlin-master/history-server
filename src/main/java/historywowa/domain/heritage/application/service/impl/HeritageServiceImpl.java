package historywowa.domain.heritage.application.service.impl;

import historywowa.domain.heritage.application.service.HeritageService;
import historywowa.domain.heritage.domain.entity.Heritage;
import historywowa.domain.heritage.domain.repository.HeritageRepository;
import historywowa.domain.heritage.presentation.dto.res.HeritageImageRes;
import historywowa.domain.member.domain.entity.Member;
import historywowa.domain.member.domain.repository.MemberRepository;
import historywowa.domain.openai.application.service.OpenAIVisionService;
import historywowa.domain.point.application.PointService;
import historywowa.global.infra.exception.error.ErrorCode;
import historywowa.global.infra.exception.error.HistoryException;
import historywowa.global.infra.s3.presentation.application.S3Service;
import jakarta.transaction.Transactional;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class HeritageServiceImpl implements HeritageService {

    private final S3Service s3Service;
    private final HeritageRepository heritageRepository;
    private final OpenAIVisionService openAIVisionService;
    private final MemberRepository memberRepository;
    private final PointService pointService;

    @Override
    public HeritageImageRes recognize(MultipartFile heritageImage, String userId) throws IOException {

        Member member = getMemberOrThrow(userId);

        validatePoint(member);

        String imageUrl = uploadImageToS3(heritageImage, userId);
        String analysisText = analyzeImage(imageUrl);

        saveHeritageRecord(member, imageUrl, analysisText);
        pointService.usePointForHeritage(member);

        return HeritageImageRes.of(imageUrl, analysisText);
    }

    private void validatePoint(Member member) {
        Long point = pointService.getMemberPoint(member.getId()).point();
        if (point == 0) {
            throw new HistoryException(ErrorCode.INSUFFICIENT_POINT_FOR_HERITAGE);
        }
    }


    private String uploadImageToS3(MultipartFile file, String userId) throws IOException {
        return s3Service.storeImage(file, userId);
    }

    private String analyzeImage(String imageUrl) {
        return openAIVisionService.analyzeHeritageImage(imageUrl);
    }

    private Heritage saveHeritageRecord(Member member, String url, String text) {
        Heritage heritage = Heritage.builder()
                .member(member)
                .heritageUrl(url)
                .heritageText(text)
                .build();

        return heritageRepository.save(heritage);
    }


    private Member getMemberOrThrow(String userId) {
        return memberRepository.findById(userId)
                .orElseThrow(() -> new HistoryException(ErrorCode.USER_NOT_EXIST));
    }

}
