package historywowa.domain.openai.dto.image.req

data class OpenAIVisionReq(
        val model: String,
        val messages: List<Message>
) {
    data class Message(
            val role: String,
            val content: List<Content>
    )

    sealed interface Content

    data class TextContent(
            val type: String,
            val text: String
    ) : Content

    data class ImageContent(
            val type: String,
            val image_url: ImageUrl
    ) : Content

    data class ImageUrl(
            val url: String
    )
}

