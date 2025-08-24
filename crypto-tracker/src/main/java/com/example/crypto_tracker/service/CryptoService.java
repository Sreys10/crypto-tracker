package com.example.crypto_tracker.service;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Service
public class CryptoService {

    private final WebClient.Builder webClientBuilder;

    public CryptoService(WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
    }

    public Map<String, Object> getCurrentPrices() {
        String url = "https://api.coingecko.com/api/v3/simple/price" +
                "?ids=bitcoin,ethereum&vs_currencies=usd";

        return webClientBuilder.build()
                .get()
                .uri(url)
                .retrieve()
                .bodyToMono(Map.class)
                .block();
    }
}
