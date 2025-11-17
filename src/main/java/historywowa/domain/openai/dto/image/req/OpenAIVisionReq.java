package historywowa.domain.openai.dto.image.req;

import java.util.List;
public record OpenAIVisionReq(
        String model,
        List<Message> messages
) {

    public record Message(
            String role,
            List<Content> content
    ) {}

    public sealed interface Content
            permits TextContent, ImageContent {}

    public record TextContent(
            String type,
            String text
    ) implements Content {}

    public record ImageContent(
            String type,
            ImageUrl image_url
    ) implements Content {}

    public record ImageUrl(
            String url
    ) {}
}
