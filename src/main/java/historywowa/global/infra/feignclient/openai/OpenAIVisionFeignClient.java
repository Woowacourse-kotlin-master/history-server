package historywowa.global.infra.feignclient.openai;

import historywowa.domain.openai.dto.image.req.OpenAIVisionReq;
import historywowa.domain.openai.dto.image.res.OpenAIVisionRes;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(
        name = "OpenAIVisionAPI",
        url = "${openai.api.url}"
)
public interface OpenAIVisionFeignClient {

    @PostMapping(value = "/v1/chat/completions")
    OpenAIVisionRes generateVision(
            @RequestHeader("Authorization") String apiKey,
            @RequestBody OpenAIVisionReq request
    );
}
