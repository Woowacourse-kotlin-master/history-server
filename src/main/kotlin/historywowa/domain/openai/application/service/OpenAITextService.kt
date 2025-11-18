package historywowa.domain.openai.application.service

interface OpenAITextService {

    fun recommendFood(prompt: String): String

    fun recommendRestaurant(address: String, foodName: String, city: String): String
}
