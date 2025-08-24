package com.example.crypto_tracker.controller;


import com.example.crypto_tracker.service.CryptoService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class CryptoController {

    private final CryptoService cryptoService;

    public CryptoController(CryptoService cryptoService) {
        this.cryptoService = cryptoService;
    }

    @GetMapping("/prices")
    public Map<String, Object> getPrices() {
        return cryptoService.getCurrentPrices();
    }
}
