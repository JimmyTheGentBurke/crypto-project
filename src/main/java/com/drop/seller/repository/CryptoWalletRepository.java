package com.drop.seller.repository;

import com.drop.seller.entity.CryptoWalletEntity;
import com.drop.seller.entity.PortfolioEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CryptoWalletRepository extends JpaRepository<CryptoWalletEntity, Long> {
    Optional<CryptoWalletEntity> findBySymbol(String symbol);
    Optional<CryptoWalletEntity> findBySymbolAndPortfolio(String symbol, PortfolioEntity portfolio);
    Optional<CryptoWalletEntity> findByUserIdAndSymbol(Long id, String symbol);
}
