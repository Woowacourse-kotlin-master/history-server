package historywowa.domain.openai.application.service;

public interface OpenAITextService {

    String recommendFood(String prompt);

    String recommendRestaurant(String address, String foodName, String city);
}
