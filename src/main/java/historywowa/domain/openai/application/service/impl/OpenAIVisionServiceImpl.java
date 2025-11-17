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
        String prompt = buildVisionPrompt();

        return new OpenAIVisionReq(
                model,
                List.of(
                        new OpenAIVisionReq.Message(
                                "user",
                                List.of(
                                        new OpenAIVisionReq.Content("text", prompt, null),
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

    private String buildVisionPrompt() {
        return """
            너는 한국 문화재 전문가이자 시각 분석 전문가야.
            내가 제공하는 이미지를 정밀하게 분석해서 아래 기준에 따라 한국어로 답변해줘.

            [주의]
            - 이미지를 분석했을 때 한국 문화재로 보이지 않으면 "이 이미지는 한국 문화재로 추정되지 않습니다."라고 명확하게 말해줘.
            - 사물, 음식, 인물, 동물, 풍경 등 문화재와 무관한 사진이면 절대로 문화재라고 추정하지 마.
            - 비슷한 외형이 있어도 근거가 없으면 문화재로 단정하지 말고 "확신할 수 없음"과 이유를 반드시 설명해.

            [분석 기준]
            1) 어떤 문화재인지 추정할 수 있는 고유 특징 설명
            2) 형태, 재질, 구조, 색상 등 시각적 단서 분석
            3) 한국 문화재 명칭(정확한 공식 명칭 추정)
            4) 해당 문화재가 속하는 시대/지역/종류(사찰/탑/부도/불상 등) 분석
            5) 추가로 추정 가능한 역사적 맥락 또는 의미 설명

            [출력 형식]
            - 자연어 문장으로 자세하게 설명
            - 불필요한 말투·중복 제거
            - "확실하지 않은 경우"는 그 이유를 함께 설명
            - 문화재가 아닐 경우에도 정중하게 이유와 함께 설명

            이미지를 기반으로 문화재를 최대한 정확하고 신중하게 판단해줘.
            """;
    }

}
