package com.example.crypto_tracker.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.Collections;
import java.util.Map;

@Service
public class AIService {

    private final WebClient webClient;

    // 🔹 Your Hugging Face API key from https://huggingface.co/settings/tokens
    @Value("${huggingface.api.key}")
    private String apiKey;

    public AIService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }

    public String generateInsight(String prompt) {
        String model = "distilgpt2"; // Free public model

        try {
            Map<String, Object> requestBody = Collections.singletonMap("inputs", prompt);

            Map<String, Object>[] response = webClient.post()
                    .uri("https://api-inference.huggingface.co/models/" + model)
                    .header("Authorization", "Bearer " + apiKey)
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(Map[].class)
                    .block();

            if (response != null && response.length > 0 && response[0].containsKey("generated_text")) {
                return response[0].get("generated_text").toString();
            } else {
                return "AI insights unavailable.";
            }

        } catch (WebClientResponseException e) {
            System.err.println("Hugging Face API error: " + e.getMessage());
            return "AI insights unavailable.";
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
            return "AI insights unavailable.";
        }
    }
}
