package historywowa.domain.openai.dto.image.res;

import java.util.List;

public record OpenAIVisionRes(
        List<Choice> choices
) {
    public static record Choice(
            Message message
    ) {}

    public static record Message(
            String role,
            String content
    ) {}
}