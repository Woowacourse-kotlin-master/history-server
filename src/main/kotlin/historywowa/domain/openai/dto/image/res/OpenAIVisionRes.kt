package historywowa.domain.openai.dto.image.res

data class OpenAIVisionRes(
        val choices: List<Choice>
) {
    data class Choice(
            val message: Message
    )

    data class Message(
            val role: String,
            val content: String
    )
}