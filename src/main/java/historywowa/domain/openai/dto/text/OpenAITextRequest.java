package historywowa.domain.openai.dto.text;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class OpenAITextRequest {
    private String model;
    private List<Message> messages;
    private double temperature;

    @JsonProperty("max_tokens")
    private Integer maxTokens;

    @Data
    @Builder
    public static class Message {
        private String role;
        private String content;
    }
}