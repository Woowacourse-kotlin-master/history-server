package historywowa.domain.openai.application.service.impl;

import historywowa.domain.openai.application.service.OpenAIVisionService;
import historywowa.domain.openai.dto.image.req.OpenAIVisionReq;
import historywowa.domain.openai.dto.image.res.OpenAIVisionRes;
import historywowa.global.infra.exception.error.ErrorCode;
import historywowa.global.infra.exception.error.HistoryException;
import historywowa.global.infra.feignclient.openai.OpenAIVisionFeignClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class OpenAIVisionServiceImpl implements OpenAIVisionService {

    private final OpenAIVisionFeignClient openAIVisionFeignClient;

    @Value("${openai.api.key}")
    private String apiKey;

    @Value("${openai.text.model}")
    private String model;

    @Override
    public String analyzeHeritageImage(String imageUrl) {
        try {
            OpenAIVisionReq request = buildVisionRequest(imageUrl);
            OpenAIVisionRes response = callVisionAPI(request);
            return extractContent(response);
        } catch (Exception e) {
            log.error("문화재 이미지 분석 실패: {}", e.getMessage());
            throw new HistoryException(ErrorCode.OPENAI_NOT_EXIST);
        }
    }

    private OpenAIVisionReq buildVisionRequest(String imageUrl) {
        return new OpenAIVisionReq(
                model,
                List.of(
                        new OpenAIVisionReq.Message(
                                "user",
                                List.of(
                                        new OpenAIVisionReq.Content(
                                                "text",
                                                "이 이미지를 보고 어떤 문화재인지, 특징과 설명을 한국어로 자세히 알려줘.",
                                                null
                                        ),
                                        new OpenAIVisionReq.Content(
                                                "image_url",
                                                null,
                                                new OpenAIVisionReq.ImageUrl(imageUrl)
                                        )
                                )
                        )
                )
        );
    }

    private OpenAIVisionRes callVisionAPI(OpenAIVisionReq request) {
        return openAIVisionFeignClient.generateVision(
                "Bearer " + apiKey,
                request
        );
    }

    private String extractContent(OpenAIVisionRes response) {
        if (response.choices() == null || response.choices().isEmpty()) {
            throw new HistoryException(ErrorCode.OPENAI_NOT_EXIST);
        }
        return response.choices().get(0).message().content();
    }
}
