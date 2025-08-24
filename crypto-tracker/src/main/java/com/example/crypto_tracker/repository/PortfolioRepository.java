package com.example.crypto_tracker.repository;


import com.example.crypto_tracker.entity.Portfolio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PortfolioRepository extends JpaRepository<Portfolio, Long> {
    List<Portfolio> findByUserEmail(String userEmail);
}
