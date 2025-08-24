package com.example.crypto_tracker.entity;


import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Portfolio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userEmail;   // identify user
    private String coinId;      // e.g. "bitcoin"
    private double quantity;    // e.g. 0.5 BTC
}
