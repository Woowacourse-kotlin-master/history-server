package historywowa.domain.openai.application.service

interface OpenAIVisionService {
    fun analyzeHeritageImage(imageUrl: String): String
}
