package com.example.crypto_tracker.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PortfolioSummaryDTO {
    private String userEmail;
    private List<PortfolioValueDTO> holdings;
    private double totalValue;
    private String aiInsights;

    // 🔹 Extra constructor for backwards compatibility
    public PortfolioSummaryDTO(String userEmail, List<PortfolioValueDTO> holdings, double totalValue) {
        this.userEmail = userEmail;
        this.holdings = holdings;
        this.totalValue = totalValue;
        this.aiInsights = null; // default when no insights yet
    }
}
