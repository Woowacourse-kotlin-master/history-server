/*
package historywowa.domain.openai.application.service.impl;

import babbuddy.domain.openai.application.service.OpenAITextService;
import babbuddy.domain.openai.dto.openai.OpenAITextRequest;
import babbuddy.domain.openai.dto.openai.OpenAITextResponse;
import babbuddy.global.infra.exception.error.BabbuddyException;
import babbuddy.global.infra.exception.error.ErrorCode;
import babbuddy.global.infra.feignclient.OpenAITextFeignClient;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class OpenAITextServiceImpl implements OpenAITextService {

    private final OpenAITextFeignClient openAITextFeignClient;

    @Value("${openai.api.key}")
    private String apiKey;

    @Value("${openai.text.model}")
    private String textModel;

    @Override
    public String recommendFood(String prompt) {
        try {
            List<OpenAITextRequest.Message> messages = new ArrayList<>();
            messages.add(OpenAITextRequest.Message.builder()
                    .role("system")
                    .content("너는 사용자의 취향과 상황에 맞춰 가장 적합한 음식을 추천해주는 전문가야. 친절하고 구체적으로 추천해줘.")
                    .build());
            messages.add(OpenAITextRequest.Message.builder()
                    .role("user")
                    .content(prompt)
                    .build());

            OpenAITextRequest request = OpenAITextRequest.builder()
                    .model(textModel)
                    .messages(messages)
                    .temperature(0.7)
                    .maxTokens(500)
                    .build();

            OpenAITextResponse response = openAITextFeignClient.generateText(
                    "Bearer " + apiKey,
                    request
            );

            if (response.getChoices() != null && !response.getChoices().isEmpty()) {
                return response.getChoices().get(0).getMessage().getContent();

            } else {
                throw new BabbuddyException(ErrorCode.OPENAI_NOT_EXIST);
            }
        } catch (Exception e) {
            log.error("에러 내용: {}", e.getMessage(), e);
            throw new BabbuddyException(ErrorCode.OPENAI_NOT_EXIST);
        }
    }


    @Override
    public String recommendRestaurant(String address, String foodName, String city) {
        */
/* ===== 1) 프롬프트 ===== *//*

        String prompt = String.format(
                """
                        %s 근처(반경 1 km)에서 운영 중인 인기 %s 음식점 3곳을 2025년 영업 기준으로 JSON으로만 추천해 줘.
                        - 1 km 이내에 없으면 3 km 이내에서 비슷한 종류라도 반드시 3곳을 추천해.
                        - 자연어 설명·``` 코드블록 없이 JSON 배열 **한 줄**로만 반환해.
                                      
                        각 객체는 정확히 아래와 같은 키를 포함해야 해:
                        [
                          {
                            "name": "매장명",
                            "rating": "4.5",
                            "restaurantType": "일식",
                            "address": "정확한 주소",
                            "latitude": 37.123456,
                            "longitude": 126.123456
                          },
                          …
                        ]
                        """,
                address, foodName
        );

        */
/* ===== 2) JSON 요청 객체 생성 ===== *//*

        try {
            ObjectMapper mapper = new ObjectMapper();

            Map<String, Object> body = new HashMap<>();
            body.put("model", "gpt-4o");
            body.put("input", prompt);

            Map<String, Object> userLocation = Map.of(
                    "type", "approximate",
                    "country", "KR",
                    "city", city,
                    "region", city
            );

            Map<String, Object> tool = Map.of(
                    "type", "web_search_preview",
                    "user_location", userLocation,
                    "search_context_size", "medium"
            );

            body.put("tools", List.of(tool));
            body.put("tool_choice", Map.of("type", "web_search_preview"));

            String jsonInput = mapper.writeValueAsString(body);


            */
/* ===== 3) OpenAI 호출 ===== *//*

            HttpURLConnection conn = (HttpURLConnection)
                    new URL("https://api.openai.com/v1/responses").openConnection();

            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", "Bearer " + apiKey);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            try (OutputStream os = conn.getOutputStream()) {
                os.write(jsonInput.getBytes(StandardCharsets.UTF_8));
            }

            int code = conn.getResponseCode();
            InputStream is = (code >= 200 && code < 300) ? conn.getInputStream() : conn.getErrorStream();

            StringBuilder sb = new StringBuilder();
            try (BufferedReader br = new BufferedReader(
                    new InputStreamReader(is, StandardCharsets.UTF_8))) {
                String line;
                while ((line = br.readLine()) != null) sb.append(line.trim());
            }

            log.info("📥 GPT 응답:\n{}", sb.toString());
            conn.disconnect();

            */
/* ===== 4) 결과 파싱 ===== *//*

            JsonNode root = mapper.readTree(sb.toString());
            for (JsonNode item : root.path("output")) {
                if ("message".equals(item.path("type").asText())) {
                    for (JsonNode c : item.path("content")) {
                        if ("output_text".equals(c.path("type").asText())) {
                            String jsonText = cleanJsonBlock(c.path("text").asText());
                            log.info(jsonText);
                            return jsonText;
                        }
                    }
                }
            }

        } catch (Exception e) {
            log.error("❌ OpenAI 호출 실패", e);
            throw new BabbuddyException(ErrorCode.OPENAI_NOT_EXIST);
        }

        return "[]"; // 실패 시 빈 배열 반환
    }

    private String cleanJsonBlock(String raw) {
        raw = raw.trim();
        if (raw.startsWith("```")) {
            int start = raw.indexOf("\n") + 1;
            int end = raw.lastIndexOf("```");
            if (start > 0 && end > start) {
                return raw.substring(start, end).trim();
            }
        }
        return raw;
    }


}*/
