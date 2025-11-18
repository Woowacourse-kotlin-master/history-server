package historywowa.global.infra.feignclient.openai;

import historywowa.domain.openai.dto.text.OpenAITextRequest;
import historywowa.domain.openai.dto.text.OpenAITextResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(
        name = "OpenAITextAPI",
        url = "${openai.api.url}"
)
public interface OpenAITextFeignClient {
    @PostMapping(value = "/v1/chat/completions")
    OpenAITextResponse generateText(
            @RequestHeader("Authorization") String apiKey,
            @RequestBody OpenAITextRequest request
    );
}
