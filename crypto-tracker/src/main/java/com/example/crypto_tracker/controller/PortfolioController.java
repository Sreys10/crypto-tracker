package com.example.crypto_tracker.controller;


import com.example.crypto_tracker.dto.PortfolioSummaryDTO;
import com.example.crypto_tracker.entity.Portfolio;
import com.example.crypto_tracker.service.PortfolioService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/portfolio")
public class PortfolioController {

    private final PortfolioService portfolioService;

    public PortfolioController(PortfolioService portfolioService) {
        this.portfolioService = portfolioService;
    }

    // Add a coin to portfolio
    @PostMapping("/add")
    public Portfolio addCoin(@RequestBody Portfolio portfolio) {
        return portfolioService.addCoin(portfolio);
    }

    // Get user’s portfolio
    @GetMapping("/{userEmail}")
    public List<Portfolio> getPortfolio(@PathVariable String userEmail) {
        return portfolioService.getUserPortfolio(userEmail);
    }

    // Remove a coin by id
    @DeleteMapping("/{id}")
    public String removeCoin(@PathVariable Long id) {
        portfolioService.removeCoin(id);
        return "Coin removed from portfolio!";
    }

    @GetMapping("/value/{userEmail}")
    public PortfolioSummaryDTO getPortfolioValue(@PathVariable String userEmail) {
        return portfolioService.getPortfolioValue(userEmail);}

    @GetMapping("/insights/{userEmail}")
    public PortfolioSummaryDTO getPortfolioWithInsights(@PathVariable String userEmail) {
        return portfolioService.getPortfolioWithInsights(userEmail);
    }

}
