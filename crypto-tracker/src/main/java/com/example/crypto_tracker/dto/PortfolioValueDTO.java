package com.example.crypto_tracker.dto;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PortfolioValueDTO {
    private String coinId;
    private double quantity;
    private double currentPrice;
    private double currentValue;   // quantity * price
}
