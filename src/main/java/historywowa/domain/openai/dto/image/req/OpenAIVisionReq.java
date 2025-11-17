package historywowa.domain.openai.dto.image.req;

import java.util.List;

public record OpenAIVisionReq(
        String model,
        List<Message> messages
) {
    public static record Message(
            String role,
            List<Content> content
    ) {}

    public static record Content(
            String type,
            String text,
            ImageUrl image_url
    ) {}

    public static record ImageUrl(
            String url
    ) {}
}
