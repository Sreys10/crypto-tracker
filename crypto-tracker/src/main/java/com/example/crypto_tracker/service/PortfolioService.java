package com.example.crypto_tracker.service;

import com.example.crypto_tracker.dto.PortfolioSummaryDTO;
import com.example.crypto_tracker.dto.PortfolioValueDTO;
import com.example.crypto_tracker.entity.Portfolio;
import com.example.crypto_tracker.repository.PortfolioRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.*;

@Service
public class PortfolioService {

    private final PortfolioRepository portfolioRepository;
    private final WebClient.Builder webClientBuilder;
    private final AIService aiService;   // ✅ Inject AI service

    public PortfolioService(PortfolioRepository portfolioRepository,
                            WebClient.Builder webClientBuilder,
                            AIService aiService) {  // ✅ Constructor injection
        this.portfolioRepository = portfolioRepository;
        this.webClientBuilder = webClientBuilder;
        this.aiService = aiService;
    }

    public Portfolio addCoin(Portfolio portfolio) {
        return portfolioRepository.save(portfolio);
    }

    public List<Portfolio> getUserPortfolio(String userEmail) {
        return portfolioRepository.findByUserEmail(userEmail);
    }

    public void removeCoin(Long id) {
        portfolioRepository.deleteById(id);
    }

    // 🔹 Calculate portfolio value
    public PortfolioSummaryDTO getPortfolioValue(String userEmail) {
        List<Portfolio> portfolios = portfolioRepository.findByUserEmail(userEmail);

        if (portfolios.isEmpty()) {
            return new PortfolioSummaryDTO(userEmail, Collections.emptyList(), 0.0);
        }

        // Collect all coin IDs
        String coinIds = String.join(",", portfolios.stream()
                .map(Portfolio::getCoinId)
                .toList());

        // Call CoinGecko API
        String url = "https://api.coingecko.com/api/v3/simple/price?ids="
                + coinIds + "&vs_currencies=usd";

        @SuppressWarnings("unchecked")
        Map<String, Map<String, Object>> prices = (Map<String, Map<String, Object>>) webClientBuilder.build()
                .get()
                .uri(url)
                .retrieve()
                .bodyToMono(Map.class)
                .block();

        List<PortfolioValueDTO> holdings = new ArrayList<>();
        double totalValue = 0.0;

        for (Portfolio p : portfolios) {
            Map<String, Object> coinData = prices.get(p.getCoinId());

            if (coinData == null || !coinData.containsKey("usd")) {
                System.out.println("⚠️ Missing price for " + p.getCoinId());
                continue;
            }

            Number priceNumber = (Number) coinData.get("usd");
            double price = priceNumber.doubleValue();

            double currentValue = p.getQuantity() * price;
            totalValue += currentValue;

            holdings.add(new PortfolioValueDTO(
                    p.getCoinId(),
                    p.getQuantity(),
                    price,
                    currentValue
            ));
        }

        return new PortfolioSummaryDTO(userEmail, holdings, totalValue);
    }

    // 🔹 Portfolio with AI insights
    public PortfolioSummaryDTO getPortfolioWithInsights(String userEmail) {
        PortfolioSummaryDTO summary = getPortfolioValue(userEmail);

        StringBuilder prompt = new StringBuilder();
        prompt.append("Analyze this crypto portfolio:\n");
        summary.getHoldings().forEach(h ->
                prompt.append(h.getCoinId())
                        .append(" - Qty: ").append(h.getQuantity())
                        .append(", Price: $").append(h.getCurrentPrice())
                        .append(", Value: $").append(h.getCurrentValue()).append("\n")
        );
        prompt.append("\nTotal value: $").append(summary.getTotalValue());
        prompt.append("\nProvide risk analysis and investment advice in 3-4 sentences.");

        try {
            String aiResponse = aiService.generateInsight(prompt.toString());
            summary.setAiInsights(aiResponse);
        } catch (Exception e) {
            summary.setAiInsights("AI insights unavailable at the moment.");
            System.err.println("Error generating AI insights: " + e.getMessage());
        }

        return summary;
    }
}
